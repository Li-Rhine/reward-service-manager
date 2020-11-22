package com.imooc.rewardservicemanager.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.rewardservicemanager.dao.RewardDao;
import com.imooc.rewardservicemanager.dto.OrderMessageDTO;
import com.imooc.rewardservicemanager.po.RewardPO;
import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * @Description：
 * @Author： Rhine
 * @Date： 2020/11/22 16:49
 **/
@Slf4j
@Service
public class OrderMessageService {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    RewardDao rewardDao;

    @Async
    public void handleMessage() throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("101.132.104.74");
        try(Connection connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel();) {

            channel.exchangeDeclare(
                    "exchange.order.reward",
                    BuiltinExchangeType.TOPIC,
                    true,
                    false,
                    null
            );
            channel.queueDeclare(
                    "queue.reward",
                    true,
                    false,
                    false,
                    null
            );
            channel.queueBind(
                    "queue.reward",
                    "exchange.order.reward",
                    "key.reward"
            );

            channel.basicConsume("queue.reward", true, deliverCallback, consumerTag -> {});
            while (true) {
                Thread.sleep(100000);
            }
        }
    }


    //具体消费逻辑
    DeliverCallback deliverCallback = ((consumerTag, message) -> {
        String messageBody = new String(message.getBody());

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("101.132.104.74");

        try{
            //将消息反序列化
            OrderMessageDTO orderMessageDTO = objectMapper.readValue(messageBody, OrderMessageDTO.class);

            //业务代码
            RewardPO rewardPO = new RewardPO();
            rewardPO.setOrderId(orderMessageDTO.getOrderId());
            rewardPO.setAmount(orderMessageDTO.getPrice());
            rewardPO.setDate(new Date());
            rewardDao.insert(rewardPO);

            orderMessageDTO.setRewardId(rewardPO.getId());


            log.info("onMessage:{}", orderMessageDTO);

            try(Connection connection = connectionFactory.newConnection();
                Channel channel = connection.createChannel();) {

                String messageToSend = objectMapper.writeValueAsString(orderMessageDTO);
                //发送消息
                channel.basicPublish(
                        "exchange.order.reward",
                        "key.order",
                        null,
                        messageToSend.getBytes());
            }


        }catch (Exception e) {
            log.error(e.getMessage(), e);
        }


    });
            
            
            
}
