package com.flagship.view;

import com.flagship.common.ServiceInstanceEnum;
import com.flagship.pojo.ConsumeRecord;
import com.flagship.pojo.RechargeRecord;
import com.flagship.pojo.User;
import com.flagship.service.ConsumeRecordService;
import com.flagship.service.RechargeRecordService;
import com.flagship.service.UserService;
import com.flagship.util.ExceptionUtils;

import java.util.List;
import java.util.Scanner;

/**
 * @Author Flagship
 * @Date 2021/7/2 8:20
 * @Description 管理员界面类
 */
public class AdminView implements BaseView {
    private final UserService userService ;
    private final ConsumeRecordService consumeRecordService ;
    private final RechargeRecordService rechargeRecordService ;

    /**
     * 构造器，初始化数据
     */
    public AdminView() {
        userService = (UserService) ServiceInstanceEnum.getViewByName("userService");
        consumeRecordService = (ConsumeRecordService) ServiceInstanceEnum.getViewByName("consumeRecordService");
        rechargeRecordService = (RechargeRecordService) ServiceInstanceEnum.getViewByName("rechargeRecordService");
    }

    /**
     * 管理员界面
     */
    @Override
    public void show(Scanner in) {
        printAdminMessage();
        adminLoop: while (true) {
            try {
                //获取输入
                int choose = Integer.parseInt(in.next());
                //判断输入
                switch (choose) {
                    case 1:
                        //修改密码
                        updatePassword(in);
                        printAdminMessage();
                        break;
                    case 2:
                        //用户注册
                        userRegister(in);
                        printAdminMessage();
                        break;
                    case 3:
                        //修改用户名
                        updateUserName(in);
                        printAdminMessage();
                        break;
                    case 4:
                        //冻结用户
                        freezeUser(in);
                        printAdminMessage();
                        break;
                    case 5:
                        //解冻用户
                        activeUser(in);
                        printAdminMessage();
                        break;
                    case 6:
                        //查看用户列表
                        printUserList(userService.getUserList());
                        printAdminMessage();
                        break;
                    case 7:
                        //查看充值记录
                        printRechargeRecords(rechargeRecordService.getRechargeRecords());
                        printAdminMessage();
                        break;
                    case 8:
                        //查看消费记录
                        printConsumeRecords(consumeRecordService.getConsumeRecords());
                        printAdminMessage();
                        break;
                    case 9:
                        //退出登录
                        logout();
                        break adminLoop;
                    default:
                        printAdminMessage();
                        System.out.print("输入有误，请重新输入：");
                }
            } catch (NumberFormatException e) {
                printAdminMessage();
                System.out.print("输入有误，请重新输入：");
                ExceptionUtils.recordException("管理员界面输入异常");
            }
        }
    }

    /**
     * 退出登录
     */
    private void logout() {
        UserService.currentUser = null;
        MainView.printWelcomeMessage();
        rechargeRecordService.clear();
        consumeRecordService.clear();
    }

    /**
     * 打印管理员界面信息
     */
    public static void printAdminMessage() {
        System.out.println("---管理员，你好，欢迎使用消费管理系统---");
        System.out.println("---1.修改密码------------------------");
        System.out.println("---2.添加会员------------------------");
        System.out.println("---3.修改会员用户名-------------------");
        System.out.println("---4.冻结会员------------------------");
        System.out.println("---5.解冻会员------------------------");
        System.out.println("---6.查看用户列表---------------------");
        System.out.println("---8.查看充值记录---------------------");
        System.out.println("---8.查看消费记录---------------------");
        System.out.println("---9.退出登录------------------------");
        System.out.print("请选择你的操作：");
    }

    /**
     * 用户注册
     */
    private void userRegister(Scanner in) {
        System.out.print("请输入用户名：");
        String userName = in.next();
        System.out.print("请输入密码：");
        String password = in.next();
        if (userService.register(userName, password)) {
            System.out.println("创建成功！");
        } else {
            System.out.println("创建失败，该用户名已存在！");
        }
    }

    /**
     * 修改密码
     */
    private void updatePassword(Scanner in) {
        System.out.print("请输入新密码：");
        String newPassword = in.next();
        if (userService.updatePassword(newPassword)) {
            System.out.println("修改成功");
        } else {
            System.out.println("修改失败");
        }
    }

    /**
     * 修改用户名
     */
    private void updateUserName(Scanner in) {
        System.out.print("请输入用户ID：");
        Integer userId = Integer.parseInt(in.next());
        System.out.print("请输入新用户名：");
        String userName = in.next();
        if (userService.updateUserName(userId, userName)) {
            System.out.println("修改成功！");
        } else {
            System.out.println("修改失败！");
        }
    }

    /**
     * 冻结用户
     */
    private void freezeUser(Scanner in) {
        System.out.print("请输入要冻结的用户ID：");
        Integer userId = Integer.parseInt(in.next());
        if (userService.freezeUser(userId)) {
            System.out.println("冻结成功！");
        } else {
            System.out.println("冻结失败！");
        }
    }

    /**
     * 解冻用户
     */
    private void activeUser(Scanner in) {
        System.out.print("请输入要解冻的用户ID：");
        Integer userId = Integer.parseInt(in.next());
        if (userService.activeUser(userId)) {
            System.out.println("解冻成功！");
        } else {
            System.out.println("解冻失败！");
        }
    }

    /**
     * 打印用户列表
     */
    public void printUserList(List<User> userList) {
        System.out.println("用户列表如下：");
        System.out.println("用户ID\t用户名\t用户状态\t用户余额");
        userList.forEach(user -> System.out.println(user.getId()
                + "\t\t" + user.getUserName()
                + "\t" + user.getStatus()
                + "\t\t" + user.getAccount()));
    }

    /**
     * 打印充值记录
     */
    public void printRechargeRecords(List<RechargeRecord> rechargeRecords) {
        System.out.println("充值记录列表如下：");
        System.out.println("充值记录ID\t充值用户ID\t充值用户名\t\t充值金额\t\t充值时间");
        rechargeRecords.forEach(rechargeRecord -> System.out.println(rechargeRecord.getRecordId()
                + "\t\t\t" + rechargeRecord.getUserId()
                + "\t\t\t" + userService.findUserByUserId(rechargeRecord.getUserId()).getUserName()
                + "\t\t\t" + rechargeRecord.getMoney()
                + "\t\t\t" + rechargeRecord.getDate()));
    }

    /**
     * 打印消费记录
     */
    public void printConsumeRecords(List<ConsumeRecord> consumeRecords) {
        System.out.println("消费记录列表如下：");
        System.out.println("消费记录ID\t消费用户ID\t消费用户名\t\t消费金额\t\t消费时间");
        consumeRecords.forEach(consumeRecord -> System.out.println(consumeRecord.getRecordId()
                + "\t\t\t" + consumeRecord.getUserId()
                + "\t\t\t" + userService.findUserByUserId(consumeRecord.getUserId()).getUserName()
                + "\t\t\t" + consumeRecord.getMoney()
                + "\t\t\t" + consumeRecord.getDate()));
    }
}
