package com.flagship.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @Author Flagship
 * @Date 2021/7/1 11:07
 * @Description 消费记录
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsumeRecord {
    private Integer recordId;
    private Integer userId;
    private BigDecimal money;
    private String date;
}
