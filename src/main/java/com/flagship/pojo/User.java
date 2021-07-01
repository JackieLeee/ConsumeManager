package com.flagship.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @Author Flagship
 * @Date 2021/7/1 8:21
 * @Description 用户
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Integer id;
    private String userName;
    private String password;
    private Integer role;
    private Integer status;
    private BigDecimal  account;
}
