package com.imooc.rewardservicemanager.po;

import com.imooc.rewardservicemanager.enummeration.RewardStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description：
 * @Author： Rhine
 * @Date： 2020/11/23 0:09
 **/
@Getter
@Setter
@ToString
public class RewardPO {

    private Integer id;
    private Integer orderId;
    private BigDecimal amount;
    private RewardStatus status;
    private Date date;



}
