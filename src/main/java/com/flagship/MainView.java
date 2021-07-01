package com.flagship;

import com.flagship.pojo.ConsumeRecord;
import com.flagship.pojo.RechargeRecord;
import com.flagship.pojo.User;
import com.flagship.service.ConsumeRecordService;
import com.flagship.service.RechargeRecordService;
import com.flagship.service.UserService;
import com.flagship.util.ExceptionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

/**
 * @Author Flagship
 * @Date 2021/7/1 8:18
 * @Description
 */
public class MainView {
    public static User currentUser;
    private UserService userService;
    private ConsumeRecordService consumeRecordService;
    private RechargeRecordService rechargeRecordService;

    /**
     * 启动方法
     */
    public static void main(String[] args) {
        //初始化
        MainView mainView = new MainView();
        mainView.userService = new UserService();
        mainView.consumeRecordService = new ConsumeRecordService();
        mainView.userService.initUserList();
        mainView.rechargeRecordService = new RechargeRecordService();

        //循环等待
        mainView.printWelcomeMessage();
        mainView.welcomeView();
    }

    /**
     * 欢迎界面
     */
    private void welcomeView() {
        try (Scanner in = new Scanner(System.in)) {
            while (true) {
                try {
                    int choose = Integer.parseInt(in.next());
                    switch (choose) {
                        case 1:
                            adminLogin(in);
                            break;
                        case 2:
                            userLogin(in);
                            break;
                        case 3:
                            register(in);
                            break;
                        case 4:
                            System.exit(0);
                            break;
                        default:
                            System.out.print("输入有误，请重新输入1：");
                            printWelcomeMessage();
                    }
                } catch (NumberFormatException e) {
                    System.out.print("输入有误，请重新输入：");
                    ExceptionUtils.recordException("欢迎界面输入异常");
                }
            }
        }
    }

    /**
     * 管理员登录控制器
     */
    private void adminLogin(Scanner in) {
        System.out.print("请输入用户名：");
        String adminUserName = in.next();
        System.out.print("请输入密码：");
        String adminPassword = in.next();
        if (userService.adminLogin(adminUserName, adminPassword)) {
            consumeRecordService.initRecords(false);
            rechargeRecordService.initRecords(false);
            adminView(in);
        } else {
            System.out.println("登录失败，用户名或密码错误");
            printWelcomeMessage();
        }
    }

    /**
     * 管理员界面
     */
    private void adminView(Scanner in) {
        printAdminMessage();
        //TODO...管理员业务逻辑
        adminLoop: while (true) {
            try {
                int choose = Integer.parseInt(in.next());
                switch (choose) {
                    case 1:
                        updatePassword(in);
                        printAdminMessage();
                        break;
                    case 2:
                        userRegister(in);
                        printAdminMessage();
                        break;
                    case 3:
                        updateUserName(in);
                        printAdminMessage();
                        break;
                    case 4:
                        freezeUser(in);
                        printAdminMessage();
                        break;
                    case 5:
                        printUserList();
                        printAdminMessage();
                        break;
                    case 6:
                        printRechargeRecords();
                        printAdminMessage();
                        break;
                    case 7:
                        printConsumeRecords();
                        printAdminMessage();
                        break;
                    case 8:
                        MainView.currentUser = null;
                        printWelcomeMessage();
                        break adminLoop;
                    default:
                        System.out.print("输入有误，请重新输入：");
                        printWelcomeMessage();
                }
            } catch (NumberFormatException e) {
                System.out.print("输入有误，请重新输入：");
                ExceptionUtils.recordException("管理员界面输入异常");
            }
        }
    }

    private void printConsumeRecords() {
        List<ConsumeRecord> consumeRecords = consumeRecordService.getConsumeRecords();
        System.out.println("消费记录列表如下：");
        System.out.println("消费记录ID\t消费用户ID\t消费金额\t\t消费时间");
        consumeRecords.forEach(consumeRecord -> System.out.println(consumeRecord.getRecordId()
                + "\t\t\t" + consumeRecord.getUserId()
                + "\t\t\t" + consumeRecord.getMoney()
                + "\t\t\t" + consumeRecord.getDate()));
    }

    private void printRechargeRecords() {
        List<RechargeRecord> rechargeRecords = rechargeRecordService.getRechargeRecords();
        System.out.println("充值记录列表如下：");
        System.out.println("充值记录ID\t充值用户ID\t充值金额\t\t充值时间");
        rechargeRecords.forEach(rechargeRecord -> System.out.println(rechargeRecord.getRecordId()
                + "\t\t\t" + rechargeRecord.getUserId()
                + "\t\t\t" + rechargeRecord.getMoney()
                + "\t\t\t" + rechargeRecord.getDate()));
    }

    private void printUserList() {
        System.out.println("用户列表如下：");
        System.out.println("用户ID\t用户名\t用户状态\t用户余额");
        userService.getUserList().forEach(user -> System.out.println(user.getId()
                + "\t\t" + user.getUserName()
                + "\t" + user.getStatus()
                + "\t\t" + user.getAccount()));
    }

    private void freezeUser(Scanner in) {
        System.out.print("请输入要冻结的用户ID：");
        Integer userId = Integer.parseInt(in.next());
        if (userService.freezeUser(userId)) {
            System.out.println("冻结成功！");
        } else {
            System.out.println("冻结失败！");
        }
    }

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

    private void updatePassword(Scanner in) {
        System.out.print("请输入新密码：");
        String newPassword = in.next();
        if (userService.updatePassword(newPassword)) {
            System.out.println("修改成功");
        } else {
            System.out.println("修改失败");
        }
    }

    private void printAdminMessage() {
        System.out.println("---管理员，你好，欢迎使用消费管理系统---");
        System.out.println("---1.修改密码------------------------");
        System.out.println("---2.添加会员------------------------");
        System.out.println("---3.修改会员用户名-------------------");
        System.out.println("---4.冻结会员------------------------");
        System.out.println("---5.查看用户列表---------------------");
        System.out.println("---6.查看充值记录---------------------");
        System.out.println("---7.查看消费记录---------------------");
        System.out.println("---8.退出登录------------------------");
        System.out.print("请选择你的操作：");
    }

    /**
     * 用户登录控制器
     */
    private void userLogin(Scanner in) {
        System.out.print("请输入用户名：");
        String userName = in.next();
        System.out.print("请输入密码：");
        String password = in.next();
        if (userService.userLogin(userName, password)) {
            consumeRecordService.initRecords(true);
            rechargeRecordService.initRecords(true);
            userView(in);
        } else {
            System.out.println("登录失败，用户名或密码错误");
            printWelcomeMessage();
            System.out.print("请选择你的操作：");
        }
    }


    /**
     * 用户注册控制器
     */
    private void register(Scanner in) {
        System.out.print("请输入用户名：");
        String newUserName = in.next();
        System.out.print("请输入密码：");
        String newPassword = in.next();
        if (userService.register(newUserName, newPassword)) {
            System.out.println("注册成功，正在进行自动登录...");
            if (userService.userLogin(newUserName, newPassword)) {
                consumeRecordService.initRecords(true);
                rechargeRecordService.initRecords(true);
                System.out.println("登录成功！");
                userView(in);
            } else {
                System.out.println("登录失败，用户名或密码错误");
            }
        } else {
            System.out.println("注册失败！");
            printWelcomeMessage();
        }
    }

    /**
     * 用户界面
     */
    private void userView(Scanner in) {
        //TODO...
        printUserMessage();
        userLoop: while (true) {
            try {
                int choose = Integer.parseInt(in.next());
                switch (choose) {
                    case 1:
                        System.out.println("用户信息如下：");
                        System.out.println("用户ID：" + MainView.currentUser.getId());
                        System.out.println("用户名：" + MainView.currentUser.getUserName());
                        System.out.println("余额：" + MainView.currentUser.getAccount());
                        printUserMessage();
                        break;
                    case 2:
                        updateUserView(in);
                        printUserMessage();
                        break;
                    case 3:
                        consume(in);
                        printUserMessage();
                        break;
                    case 4:
                        recharge(in);
                        printUserMessage();
                        break;
                    case 5:
                        printRechargeRecords();
                        printUserMessage();
                        break;
                    case 6:
                        printConsumeRecords();
                        printUserMessage();
                        break;
                    case 7:
                        MainView.currentUser = null;
                        printWelcomeMessage();
                        break userLoop;
                    default:
                        System.out.print("输入有误，请重新输入：");
                        printUserMessage();
                }
            } catch (NumberFormatException e) {
                System.out.print("输入有误，请重新输入：");
                ExceptionUtils.recordException("用户界面输入异常");
            }
        }
    }

    /**
     * 更新个人信息界面
     */
    private void updateUserView(Scanner in) {
        printUpdateMessage();
        updateLoop: while (true) {
            try {
                int choose = Integer.parseInt(in.next());
                switch (choose) {
                    case 1:
                        updateCurrentUserName(in);
                        printUpdateMessage();
                        break;
                    case 2:
                        updatePassword(in);
                        printUpdateMessage();
                        break;
                    case 3:
                        printUserMessage();
                        break updateLoop;
                    default:
                        System.out.print("输入有误，请重新输入：");
                        printUpdateMessage();
                }
            } catch (NumberFormatException e) {
                System.out.print("输入有误，请重新输入：");
                ExceptionUtils.recordException("更新信息界面输入异常");
            }
        }
    }

    private void updateCurrentUserName(Scanner in) {
        System.out.print("请输入新用户名：");
        String userName = in.next();
        if (userService.updateUserName(MainView.currentUser.getId(), userName)) {
            System.out.println("修改成功！");
        } else {
            System.out.println("修改失败！");
        }
    }

    private void consume(Scanner in) {
        System.out.print("请输入消费金额：");
        BigDecimal money = new BigDecimal(in.next());
        if (userService.consume(money)) {
            consumeRecordService.addRecord(money);
            System.out.println("消费成功！");
        } else {
            System.out.println("消费失败！");
        }
    }

    private void recharge(Scanner in) {
        System.out.print("请输入充值金额：");
        BigDecimal money = new BigDecimal(in.next());
        if (userService.recharge(money)) {
            rechargeRecordService.addRecord(money);
            System.out.println("充值成功！");
        } else {
            System.out.println("充值失败！");
        }
    }

    /**
     * 用户界面消息打印
     */
    private void printUserMessage() {
        System.out.println("---欢迎使用消费管理系统---");
        System.out.println("---1.查看个人信息--------");
        System.out.println("---2.修改个人信息--------");
        System.out.println("---3.消费---------------");
        System.out.println("---4.充值---------------");
        System.out.println("---5.查看个人充值记录-----");
        System.out.println("---6.查看个人消费记录-----");
        System.out.println("---7.退出登录------------");
        System.out.print("请选择你的操作：");
    }

    /**
     * 更新信息界面消息打印
     */
    private void printUpdateMessage() {
        System.out.println("---欢迎使用消费管理系统---");
        System.out.println("---1.修改用户名----------");
        System.out.println("---2.修改密码------------");
        System.out.println("---3.返回上一层----------");
        System.out.print("请选择你的操作：");
    }

    /**
     * 欢迎界面消息打印
     */
    private void printWelcomeMessage() {
        System.out.println("---欢迎使用消费管理系统---");
        System.out.println("---1.管理员登录----------");
        System.out.println("---2.会员登录------------");
        System.out.println("---3.会员注册------------");
        System.out.println("---4.退出系统------------");
        System.out.print("请选择你的操作：");
    }
}
