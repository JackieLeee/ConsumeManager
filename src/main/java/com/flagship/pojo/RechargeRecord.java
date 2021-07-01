package com.flagship.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @Author Flagship
 * @Date 2021/7/1 15:07
 * @Description 充值记录
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RechargeRecord {
    private Integer recordId;
    private Integer userId;
    private BigDecimal money;
    private String date;
}
