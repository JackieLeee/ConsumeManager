package com.flagship.service;

import com.flagship.MainView;
import com.flagship.common.Constant;
import com.flagship.pojo.User;
import com.flagship.util.ExceptionUtils;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Author Flagship
 * @Date 2021/7/1 9:09
 * @Description
 */
public class UserService {
    private final List<User> userList = new ArrayList<>();
    private Integer nextId = 1;
    private File file;

    /**
     * 初始化用户数据
     */
    public void initUserList() {
        //获取文件路径
        String filePath = Objects.requireNonNull(UserService.class.getClassLoader().getResource(Constant.FilePath.USER_DB)).getPath();
        file = new File(filePath);
        //读取文件
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (reader.ready()) {
                //实现ID自增
                ++nextId;
                //解析用户数据并加入到集合中
                String[] dataArr = reader.readLine().split("#");
                User user = resolveStringArr(dataArr);
                userList.add(user);
            }
        } catch (IOException e) {
            ExceptionUtils.recordException("用户数据初始化异常");
        }
    }

    /**
     * 从字符串数组中解析出用户数据
     */
    private User resolveStringArr(String[] dataArr) {
        User user = new User();
        user.setId(Integer.valueOf(dataArr[0]));
        user.setUserName(dataArr[1]);
        user.setPassword(dataArr[2]);
        user.setRole(Integer.valueOf(dataArr[3]));
        user.setStatus(Integer.valueOf(dataArr[4]));
        user.setAccount(new BigDecimal(dataArr[5]));
        return user;
    }

    /**
     * 从字符串数组中解析出用户数据
     */
    private String resolveUser(User user) {
        return user.getId() + "#" +
                user.getUserName() + "#" +
                user.getPassword() + "#" +
                user.getRole() + "#" +
                user.getStatus() + "#" +
                user.getAccount();
    }

    /**
     * 通过用户名找到用户
     */
    private User findUserByUserName(String userName) {
        for (User user : userList) {
            if (user.getUserName().equals(userName)) {
                return user;
            }
        }
        return null;
    }

    /**
     * 通过id找到用户
     */
    private User findUserByUserId(Integer userId) {
        for (User user : userList) {
            if (user.getId().equals(userId)) {
                return user;
            }
        }
        return null;
    }

    /**
     * 登录
     */
    private boolean login(String userName, String password, Integer role) {
        User user = findUserByUserName(userName);
        if (user != null && user.getStatus().equals(Constant.UserStatus.ACTIVE) && user.getRole().equals(role) && user.getPassword().equals(password)) {
            MainView.currentUser = user;
            return true;
        }
        return false;
    }

    /**
     * 管路员登录
     */
    public boolean adminLogin(String userName, String password) {
        return login(userName, password,  Constant.UserRole.ADMIN);
    }

    /**
     * 用户登录
     */
    public boolean userLogin(String userName, String password) {
        return login(userName, password, Constant.UserRole.USER);
    }

    /**
     * 用户注册
     */
    public boolean register(String userName, String password) {
        //判断用户名是否已存在
        if (findUserByUserName(userName) != null) {
            return false;
        }
        //构造用户
        User user = new User();
        user.setId(nextId);
        user.setUserName(userName);
        user.setPassword(password);
        user.setRole(Constant.UserRole.USER);
        user.setStatus(Constant.UserStatus.ACTIVE);
        user.setAccount(BigDecimal.ZERO);
        //写入数据
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(resolveUser(user));
            writer.newLine();
            writer.flush();
            //id自增
            ++nextId;
            userList.add(user);
        } catch (IOException e) {
            ExceptionUtils.recordException("注册用户数据保存异常");
        }
        return true;
    }

    /**
     * 更新用户
     */
    private boolean updateUser(User newUser) {
        User oldUser = findUserByUserId(newUser.getId());
        if (oldUser != null) {
            String oldStr = resolveUser(oldUser);
            String newStr = resolveUser(newUser);
            try(BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                //构造数据
                StringBuilder stringBuilder = new StringBuilder();
                for (User user : userList) {
                    stringBuilder.append(resolveUser(user)).append("\n");
                }
                String content = stringBuilder.toString().replaceAll(oldStr, newStr);
                writer.write(content);
                writer.flush();
                return true;
            } catch (IOException e) {
                ExceptionUtils.recordException("更新用户数据保存异常");
            }
        }
        return false;
    }

    /**
     * 更新用户名
     */
    public boolean updateUserName(Integer userId, String userName) {
        User user = findUserByUserId(userId);
        User anotherUser = findUserByUserName(userName);
        if (user != null && anotherUser == null) {
            user.setUserName(userName);
            return updateUser(user);
        }
        return false;
    }

    /**
     * 更新用户名
     */
    public boolean updatePassword(String password) {
        User user = MainView.currentUser;
        if (user != null) {
            user.setPassword(password);
            return updateUser(user);
        }
        return false;
    }

    /**
     * 更新余额
     */
    private boolean updateAccount(Integer userId, BigDecimal account) {
        User user = findUserByUserId(userId);
        if (user != null) {
            user.setAccount(account);
            if (updateUser(user)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 冻结会员
     */
    public boolean freezeUser(Integer userId) {
        User user = findUserByUserId(userId);
        if (user != null && user.getRole().equals(Constant.UserRole.USER)) {
            user.setStatus(Constant.UserStatus.FROZEN);
            return updateUser(user);
        }
        return false;
    }

    /**
     * 解冻会员
     */
    public boolean activeUser(Integer userId) {
        User user = findUserByUserId(userId);
        if (user != null && user.getRole().equals(Constant.UserRole.USER)) {
            user.setStatus(Constant.UserStatus.ACTIVE);
            return updateUser(user);
        }
        return false;
    }

    /**
     * 充值
     */
    public boolean recharge(BigDecimal money) {
        if (MainView.currentUser != null && MainView.currentUser.getRole().equals(Constant.UserRole.USER)) {
            User user = MainView.currentUser;
            //充值的金额是正数
            if (money.equals(money.abs())) {
                user.setAccount(user.getAccount().add(money));
                if (updateUser(user)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 消费
     */
    public boolean consume(BigDecimal money) {
        if (MainView.currentUser != null && MainView.currentUser.getRole().equals(Constant.UserRole.USER)) {
            User user = MainView.currentUser;
            //消费的金额是正数
            BigDecimal tempMoney = user.getAccount().subtract(money);
            if (money.equals(money.abs()) && tempMoney.compareTo(BigDecimal.ZERO) > -1) {
                user.setAccount(tempMoney);
                if (updateUser(user)) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<User> getUserList() {
        User currentUser = MainView.currentUser;
        List<User> list = new ArrayList<>();
        if (currentUser != null && currentUser.getRole().equals(Constant.UserRole.ADMIN)) {
            for (User user : userList) {
                if (user.getRole().equals(Constant.UserRole.ADMIN)) {
                    continue;
                }
                list.add(user);
            }
        }
        list.remove(currentUser);
        return list;
    }
}
