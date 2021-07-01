package com.flagship.service;

import com.flagship.pojo.User;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @Author Flagship
 * @Date 2021/7/1 14:52
 * @Description
 */
public class UserServiceTest {

    @Test
    public void freezeUser() {
        UserService userService = new UserService();
        userService.initUserList();
        userService.freezeUser(3);
    }

    @Test
    public void getUserList() {
        UserService userService = new UserService();
        userService.initUserList();
        userService.adminLogin("admin", "admin");
        List<User> userList = userService.getUserList();
        userList.forEach(System.out::println);
    }
}