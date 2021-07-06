package com.flagship.service;

import com.flagship.common.Constant;
import com.flagship.exception.ExceptionEnum;
import com.flagship.pojo.RechargeRecord;
import com.flagship.pojo.User;
import com.flagship.util.DateUtils;
import com.flagship.util.ExceptionUtils;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Author Flagship
 * @Date 2021/7/1 9:10
 * @Description 充值记录Service
 */
public class RechargeRecordService implements BaseService {
    /**
     * 充值记录列表
     */
    private final List<RechargeRecord> rechargeRecords = new ArrayList<>();
    /**
     * 自增ID
     */
    private Integer nextId = 1;
    /**
     * 数据文件
     */
    private File file;

    /**
     * 初始化
     */
    @Override
    public void initData() {
        User user = UserService.currentUser;
        if (user == null) {
            return;
        }
        //获取文件路径
        String filePath = Objects.requireNonNull(UserService.class.getClassLoader().getResource(Constant.FilePath.RECHARGE_RECORD_DB)).getPath();
        file = new File(filePath);
        //读取文件
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (reader.ready()) {
                //实现ID自增
                ++nextId;
                //解析充值数据并加入到集合中
                String[] dataArr = reader.readLine().split(Constant.DELIMITER);
                RechargeRecord record = resolveStringArr(dataArr);
                //如果是当前用户不是管理员，则只能获取自己的充值记录
                if (user.getRole().equals(0) && !user.getId().equals(record.getUserId())) {
                    continue;
                }
                //加入列表
                rechargeRecords.add(record);
            }
        } catch (IOException e) {
            ExceptionUtils.recordException(ExceptionEnum.INIT_RECHARGE_RECORD_ERR.getErrMsg());
        }
    }

    /**
     * 清空列表
     */
    @Override
    public void clear() {
        this.rechargeRecords.clear();
    }


    /**
     * 从字符串数组中解析出充值记录数据
     */
    private RechargeRecord resolveStringArr(String[] dataArr) {
        RechargeRecord rechargeRecord = new RechargeRecord();
        rechargeRecord.setRecordId(Integer.valueOf(dataArr[0]));
        rechargeRecord.setUserId(Integer.valueOf(dataArr[1]));
        rechargeRecord.setMoney(new BigDecimal(dataArr[2]));
        rechargeRecord.setDate(DateUtils.getCurrentDateString());
        return rechargeRecord;
    }

    /**
     * 添加一条充值记录
     */
    public void addRecord(BigDecimal money) {
        User user = UserService.currentUser;
        //用户需要登录且金额为正整数
        if (user != null && money.compareTo(BigDecimal.ZERO) > 0) {
            RechargeRecord rechargeRecord = new RechargeRecord();
            rechargeRecord.setRecordId(nextId);
            rechargeRecord.setUserId(user.getId());
            rechargeRecord.setMoney(money);
            rechargeRecord.setDate(DateUtils.getCurrentDateString());
            //数据流
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
                //写入数据文件
                writer.write(resolveRecharge(rechargeRecord));
                writer.newLine();
                writer.flush();
                //id自增
                ++nextId;
                //加入列表
                rechargeRecords.add(rechargeRecord);
            } catch (Exception e) {
                ExceptionUtils.recordException(ExceptionEnum.ADD_RECHARGE_RECORD_ERR.getErrMsg());
            }
        }
    }

    /**
     * 从充值记录对象构造出字符串
     */
    private String resolveRecharge(RechargeRecord rechargeRecord) {
        return rechargeRecord.getRecordId() + Constant.DELIMITER +
                rechargeRecord.getUserId() + Constant.DELIMITER +
                rechargeRecord.getMoney() + Constant.DELIMITER +
                rechargeRecord.getDate();
    }

    /**
     * 获取充值数据列表
     */
    public List<RechargeRecord> getRechargeRecords() {
        return rechargeRecords;
    }
}
