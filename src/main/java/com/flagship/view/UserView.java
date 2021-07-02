package com.flagship.view;

import com.flagship.common.ServiceInstanceEnum;
import com.flagship.common.ViewInstanceEnum;
import com.flagship.pojo.ConsumeRecord;
import com.flagship.pojo.RechargeRecord;
import com.flagship.service.ConsumeRecordService;
import com.flagship.service.RechargeRecordService;
import com.flagship.service.UserService;
import com.flagship.util.ExceptionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

/**
 * @Author Flagship
 * @Date 2021/7/2 8:41
 * @Description 用户界面类
 */
public class UserView implements BaseView {
    private final UserService userService ;
    private final ConsumeRecordService consumeRecordService ;
    private final RechargeRecordService rechargeRecordService ;

    public UserView() {
        userService = (UserService) ServiceInstanceEnum.getViewByName("userService");
        consumeRecordService = (ConsumeRecordService) ServiceInstanceEnum.getViewByName("consumeRecordService");
        rechargeRecordService = (RechargeRecordService) ServiceInstanceEnum.getViewByName("rechargeRecordService");
    }

    @Override
    public void show(Scanner in) {
        printUserMessage();
        userLoop: while (true) {
            try {
                int choose = Integer.parseInt(in.next());
                switch (choose) {
                    case 1:
                        //打印用户信息
                        printUserData();
                        printUserMessage();
                        break;
                    case 2:
                        //获取更新信息界面对象
                        BaseView updateView = ViewInstanceEnum.getViewByName("updateView");
                        if (updateView != null) {
                            //展示更新信息界面
                            updateView.show(in);
                        }
                        printUserMessage();
                        break;
                    case 3:
                        //消费
                        consume(in);
                        printUserMessage();
                        break;
                    case 4:
                        //充值
                        recharge(in);
                        printUserMessage();
                        break;
                    case 5:
                        //打印个人充值记录
                        printRechargeRecords(rechargeRecordService.getRechargeRecords());
                        printUserMessage();
                        break;
                    case 6:
                        //打印个人消费记录
                        printConsumeRecords(consumeRecordService.getConsumeRecords());
                        printUserMessage();
                        break;
                    case 7:
                        //退出登录
                        logout();
                        break userLoop;
                    default:
                        System.out.print("输入有误，请重新输入：");
                }
            } catch (NumberFormatException e) {
                System.out.print("输入有误，请重新输入：");
                ExceptionUtils.recordException("用户界面输入异常");
            }
        }
    }

    /**
     * 打印用户信息数据
     */
    private void printUserData() {
        System.out.println("用户信息如下：");
        System.out.println("-------------------------");
        System.out.println("用户名：" + UserService.currentUser.getUserName());
        System.out.println("余额：" + UserService.currentUser.getAccount());
        System.out.println("-------------------------");
    }

    /**
     * 退出登录
     */
    private void logout() {
        UserService.currentUser = null;
        MainView.printWelcomeMessage();
        consumeRecordService.clear();
        rechargeRecordService.clear();
    }

    /**
     * 打印用户界面信息
     */
    public static void printUserMessage() {
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
     * 充值
     */
    private void recharge(Scanner in) {
        System.out.print("请输入充值金额：");
        BigDecimal money = new BigDecimal(in.next());
        if (userService.recharge(money)) {
            //新增充值记录
            rechargeRecordService.addRecord(money);
            System.out.println("充值成功！");
        } else {
            System.out.println("充值失败！");
        }
    }

    /**
     * 消费
     */
    private void consume(Scanner in) {
        System.out.print("请输入消费金额：");
        BigDecimal money = new BigDecimal(in.next());
        if (userService.consume(money)) {
            //新增消费记录
            consumeRecordService.addRecord(money);
            System.out.println("消费成功！");
        } else {
            System.out.println("消费失败！");
        }
    }

    /**
     * 打印个人充值记录
     */
    public void printRechargeRecords(List<RechargeRecord> rechargeRecords) {
        System.out.println("充值记录列表如下：");
        System.out.println("充值记录ID\t充值金额\t\t充值时间");
        rechargeRecords.forEach(rechargeRecord -> System.out.println(rechargeRecord.getRecordId()
                + "\t\t\t" + rechargeRecord.getMoney()
                + "\t\t\t" + rechargeRecord.getDate()));
    }

    /**
     * 打印个人消费记录
     */
    public void printConsumeRecords(List<ConsumeRecord> consumeRecords) {
        System.out.println("消费记录列表如下：");
        System.out.println("消费记录ID\t消费金额\t\t消费时间");
        consumeRecords.forEach(consumeRecord -> System.out.println(consumeRecord.getRecordId()
                + "\t\t\t" + consumeRecord.getMoney()
                + "\t\t\t" + consumeRecord.getDate()));
    }
}
