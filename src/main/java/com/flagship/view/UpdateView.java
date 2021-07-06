package com.flagship.view;

import com.flagship.common.ServiceInstanceEnum;
import com.flagship.service.UserService;
import com.flagship.util.ExceptionUtils;
import com.flagship.util.ValidatorUtils;

import java.util.Scanner;

/**
 * @Author Flagship
 * @Date 2021/7/2 8:44
 * @Description 更新信息界面类
 */
public class UpdateView implements BaseView {
    private final UserService userService;

    public UpdateView() {
        userService = (UserService) ServiceInstanceEnum.getViewByName("userService");
    }

    /**
     * 展示更新信息界面
     */
    @Override
    public void show(Scanner in) {
        printUpdateMessage();
        updateLoop: while (true) {
            try {
                int choose = Integer.parseInt(in.next());
                switch (choose) {
                    case 1:
                        //修改用户名
                        updateCurrentUserName(in);
                        printUpdateMessage();
                        break;
                    case 2:
                        //修改密码
                        updatePassword(in);
                        printUpdateMessage();
                        break;
                    case 3:
                        //返回上一层
                        UserView.printUserMessage();
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

    /**
     * 更新信息界面消息打印
     */
    public static void printUpdateMessage() {
        System.out.println("---欢迎使用消费管理系统---");
        System.out.println("---1.修改用户名----------");
        System.out.println("---2.修改密码------------");
        System.out.println("---3.返回上一层----------");
        System.out.print("请选择你的操作：");
    }

    /**
     * 更新当前用户名
     */
    private void updateCurrentUserName(Scanner in) {
        System.out.print("请输入新用户名：");
        String userName = in.next();
        if (ValidatorUtils.validUserName(userName)) {
            if (userService.updateUserName(UserService.currentUser.getId(), userName)) {
                System.out.println("修改成功！");
            } else {
                System.out.println("修改失败！");
            }
        } else {
            System.out.println("修改失败，用户名必须由4位到10位的数字或字母组成，不能包含特殊字符！");
        }
    }

    /**
     * 更新密码
     */
    private void updatePassword(Scanner in) {
        System.out.print("请输入新密码：");
        String newPassword = in.next();
        if (ValidatorUtils.validPassword(newPassword)) {
            if (userService.updatePassword(newPassword)) {
                System.out.println("修改成功");
            } else {
                System.out.println("修改失败");
            }
        } else {
            System.out.println("修改失败，密码必须由8位到16位的数字或字母组成，不能包含特殊字符！");
        }
    }
}
