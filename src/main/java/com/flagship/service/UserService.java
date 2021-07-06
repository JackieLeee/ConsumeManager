package com.flagship.service;

import com.flagship.common.Constant;
import com.flagship.exception.ExceptionEnum;
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
 * @Description 用户Service类
 */
public class UserService implements BaseService {
    /**
     * 当前用户
     */
    public static User currentUser;
    /**
     * 用户列表
     */
    private final List<User> userList = new ArrayList<>();
    /**
     * 自增id
     */
    private Integer nextId = 1;
    /**
     * 数据文件
     */
    private File file;

    /**
     * 初始化数据
     */
    @Override
    public void initData() {
        //获取文件路径
        String filePath = Objects.requireNonNull(UserService.class.getClassLoader().getResource(Constant.FilePath.USER_DB)).getPath();
        file = new File(filePath);
        //读取文件
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (reader.ready()) {
                //实现ID自增
                ++nextId;
                //解析用户数据并加入到集合中
                String[] dataArr = reader.readLine().split(Constant.DELIMITER);
                User user = resolveStringArr(dataArr);
                //加入列表
                userList.add(user);
            }
        } catch (IOException e) {
            ExceptionUtils.recordException(ExceptionEnum.INIT_USER_ERR.getErrMsg());
        }
    }

    /**
     * 清空用户列表
     */
    @Override
    public void clear() {
        this.userList.clear();
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
     * 从用户对象构造出字符串
     */
    private String resolveUser(User user) {
        return user.getId() + Constant.DELIMITER +
                user.getUserName() + Constant.DELIMITER +
                user.getPassword() + Constant.DELIMITER +
                user.getRole() + Constant.DELIMITER +
                user.getStatus() + Constant.DELIMITER +
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
    public User findUserByUserId(Integer userId) {
        for (User user : userList) {
            if (user.getId().equals(userId)) {
                return user;
            }
        }
        return null;
    }

    /**
     * 根据用户身份进行登录
     */
    private boolean login(String userName, String password, Integer role) {
        User user = findUserByUserName(userName);
        if (user != null
                && user.getStatus().equals(Constant.UserStatus.ACTIVE)
                && user.getRole().equals(role)
                && user.getPassword().equals(password)) {
            UserService.currentUser = user;
            return true;
        }
        return false;
    }

    /**
     * 管理员登录
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
        //输出流
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            //写入数据
            writer.write(resolveUser(user));
            writer.newLine();
            writer.flush();
            //id自增
            ++nextId;
            //加入列表
            userList.add(user);
        } catch (IOException e) {
            ExceptionUtils.recordException(ExceptionEnum.ADD_USER_ERR.getErrMsg());
        }
        return true;
    }

    /**
     * 更新用户数据
     */
    private boolean updateUser(User newUser) {
        User oldUser = findUserByUserId(newUser.getId());
        if (oldUser != null) {
            String oldStr = resolveUser(oldUser);
            String newStr = resolveUser(newUser);
            //输出流
            try(BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                //构造数据
                StringBuilder stringBuilder = new StringBuilder();
                for (User user : userList) {
                    stringBuilder.append(resolveUser(user)).append("\n");
                }
                //替换字符串内容
                String content = stringBuilder.toString().replaceAll(oldStr, newStr);
                //写入数据
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
        //先找到用户，然后判断是否重名
        User user = findUserByUserId(userId);
        User anotherUser = findUserByUserName(userName);
        if (user != null && anotherUser == null) {
            user.setUserName(userName);
            return updateUser(user);
        }
        return false;
    }

    /**
     * 更新当前用户密码
     */
    public boolean updatePassword(String password) {
        User user = UserService.currentUser;
        if (user != null) {
            user.setPassword(password);
            return updateUser(user);
        }
        return false;
    }

    /**
     * 冻结会员
     */
    public boolean freezeUser(Integer userId) {
        User user = findUserByUserId(userId);
        //判断当前用户身份是否为管理员
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
        //判断当前用户身份是否为管理员
        if (user != null && user.getRole().equals(Constant.UserRole.USER)) {
            user.setStatus(Constant.UserStatus.ACTIVE);
            return updateUser(user);
        }
        return false;
    }

    /**
     * 用户充值
     */
    public boolean recharge(BigDecimal money) {
        User user = UserService.currentUser;
        //判断当前用户身份是否为普通用户
        if (user != null && user.getRole().equals(Constant.UserRole.USER)) {
            //充值的金额是正数
            if (money.compareTo(BigDecimal.ZERO) > 0) {
                user.setAccount(user.getAccount().add(money));
                return updateUser(user);
            }
        }
        return false;
    }

    /**
     * 用户消费
     */
    public boolean consume(BigDecimal money) {
        User user = UserService.currentUser;
        //判断当前用户身份是否为普通用户
        if (user != null && user.getRole().equals(Constant.UserRole.USER)) {
            //消费的金额是正数
            BigDecimal tempMoney = user.getAccount().subtract(money);
            if (money.compareTo(BigDecimal.ZERO) > 0 && tempMoney.compareTo(BigDecimal.ZERO) > -1) {
                user.setAccount(tempMoney);
                return updateUser(user);
            }
        }
        return false;
    }

    /**
     * 获取用户列表
     */
    public List<User> getUserList() {
        User currentUser = UserService.currentUser;
        List<User> list = new ArrayList<>();
        //判断当前用户身份是否为管理员
        if (currentUser != null && currentUser.getRole().equals(Constant.UserRole.ADMIN)) {
            for (User user : userList) {
                //跳过管理员用户
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
