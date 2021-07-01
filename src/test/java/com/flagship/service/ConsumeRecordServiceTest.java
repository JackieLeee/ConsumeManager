package com.flagship.service;

import com.flagship.MainView;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @Author Flagship
 * @Date 2021/7/1 11:34
 * @Description
 */
public class ConsumeRecordServiceTest {

    @Test
    public void adminQuery() {
        UserService userService = new UserService();
//        userService.initUserList();
        userService.adminLogin("admin", "admin");
        System.out.println(MainView.currentUser);
        ConsumeRecordService consumeRecordService = new ConsumeRecordService();
        consumeRecordService.initRecords(false);
        consumeRecordService.getConsumeRecords().forEach(System.out::println);
    }

    @Test
    public void userQuery() {
        UserService userService = new UserService();
        userService.initUserList();
        userService.userLogin("user", "user");
        System.out.println(MainView.currentUser);
        ConsumeRecordService consumeRecordService = new ConsumeRecordService();
        consumeRecordService.initRecords(true);
        consumeRecordService.getConsumeRecords().forEach(System.out::println);
    }
}