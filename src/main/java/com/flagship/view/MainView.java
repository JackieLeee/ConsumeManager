package com.flagship.view;

import com.flagship.common.ServiceInstanceEnum;
import com.flagship.common.ViewInstanceEnum;
import com.flagship.service.ConsumeRecordService;
import com.flagship.service.RechargeRecordService;
import com.flagship.service.UserService;
import com.flagship.util.ExceptionUtils;
import com.flagship.util.ValidatorUtils;

import java.util.Objects;
import java.util.Scanner;

/**
 * @Author Flagship
 * @Date 2021/7/1 8:18
 * @Description 主界面类
 */
public class MainView implements BaseView {
    private final UserService userService;
    private final ConsumeRecordService consumeRecordService ;
    private final RechargeRecordService rechargeRecordService ;

    public MainView() {
        userService = (UserService) ServiceInstanceEnum.getViewByName("userService");
        consumeRecordService = (ConsumeRecordService) ServiceInstanceEnum.getViewByName("consumeRecordService");
        rechargeRecordService = (RechargeRecordService) ServiceInstanceEnum.getViewByName("rechargeRecordService");
        Objects.requireNonNull(userService).initData();
    }

    /**
     * 展示主界面
     */
    @Override
    public void show(Scanner in) {
        if (in == null) {
            in = new Scanner(System.in);
        }
        printWelcomeMessage();
        try {
            mainLoop: while (true) {
                try {
                    int choose = Integer.parseInt(in.next());
                    switch (choose) {
                        case 1:
                            //管理员登录
                            adminLogin(in);
                            break;
                        case 2:
                            //用户登录
                            userLogin(in);
                            break;
                        case 3:
                            //用户注册
                            register(in);
                            break;
                        case 4:
                            //退出系统
                            break mainLoop;
                        default:
                            System.out.print("输入有误，请重新输入：");
                    }
                } catch (NumberFormatException e) {
                    System.out.print("输入有误，请重新输入：");
                    ExceptionUtils.recordException("欢迎界面输入异常");
                }
            }
        } finally {
            //关闭流
            in.close();
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
            //展示管理员界面
            showOtherView(in, "adminView");
        } else {
            System.out.println("登录失败，用户名或密码错误");
            printWelcomeMessage();
        }
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
            //展示用户界面
            showOtherView(in, "userView");
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

        if (!ValidatorUtils.validUserName(newUserName)) {
            System.out.println("注册失败，用户名必须由4位到10位的数字或字母组成，不能包含特殊字符！");
            printWelcomeMessage();
        } else if (!ValidatorUtils.validPassword(newPassword)) {
            System.out.println("注册失败，密码必须由8位及以上的数字或字母组成，不能包含特殊字符！");
            printWelcomeMessage();
        } else {
            if (userService.register(newUserName, newPassword)) {
                System.out.println("注册成功，正在进行自动登录...");
                if (userService.userLogin(newUserName, newPassword)) {
                    //展示用户界面
                    showOtherView(in, "userView");
                } else {
                    System.out.println("登录失败，用户名或密码错误");
                    printWelcomeMessage();
                }
            } else {
                System.out.println("注册失败！");
                printWelcomeMessage();
            }
        }
    }

    /**
     * 初始化数据并展示其他界面
     */
    private void showOtherView(Scanner in, String viewName) {
        //初始化充值消费记录
        consumeRecordService.initData();
        rechargeRecordService.initData();
        System.out.println("登录成功！");
        //获取界面对象
        BaseView view = ViewInstanceEnum.getViewByName(viewName);
        if (view != null) {
            //展示界面
            view.show(in);
        }
    }

    /**
     * 欢迎界面消息打印
     */
    public static void printWelcomeMessage() {
        System.out.println("---欢迎使用消费管理系统---");
        System.out.println("---1.管理员登录----------");
        System.out.println("---2.会员登录------------");
        System.out.println("---3.会员注册------------");
        System.out.println("---4.退出系统------------");
        System.out.print("请选择你的操作：");
    }
}
