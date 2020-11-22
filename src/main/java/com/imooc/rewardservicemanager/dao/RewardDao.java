package com.imooc.rewardservicemanager.dao;

import com.imooc.rewardservicemanager.po.RewardPO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.springframework.stereotype.Repository;

/**
 * @Description：
 * @Author： Rhine
 * @Date： 2020/11/23 0:10
 **/
@Mapper
@Repository
public interface RewardDao {


    @Insert("INSERT INTO reward (order_id, amount, status, date) VALUES(#{orderId}, #{amount}, #{status}, #{date})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(RewardPO rewardPO);
}
