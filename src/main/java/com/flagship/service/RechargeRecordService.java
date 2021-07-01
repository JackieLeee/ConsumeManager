package com.flagship.service;

import com.flagship.MainView;
import com.flagship.common.Constant;
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
 * @Description
 */
public class RechargeRecordService {
    private final List<RechargeRecord> rechargeRecords = new ArrayList<>();
    private Integer nextId = 1;
    private File file;

    public void initRecords(boolean onlyCurrentUser) {
        User user = MainView.currentUser;
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
                String[] dataArr = reader.readLine().split("#");
                RechargeRecord record = resolveStringArr(dataArr);
                //是否只保存当前用户的充值记录
                if (onlyCurrentUser) {
                    if (user.getRole().equals(1) || !user.getId().equals(record.getUserId())) {
                        continue;
                    }
                }
                rechargeRecords.add(record);
            }
        } catch (IOException e) {
            ExceptionUtils.recordException("充值数据初始化异常");
        }
    }

    private RechargeRecord resolveStringArr(String[] dataArr) {
        RechargeRecord rechargeRecord = new RechargeRecord();
        rechargeRecord.setRecordId(Integer.valueOf(dataArr[0]));
        rechargeRecord.setUserId(Integer.valueOf(dataArr[1]));
        rechargeRecord.setMoney(new BigDecimal(dataArr[2]));
        rechargeRecord.setDate(DateUtils.getCurrentDateString());
        return rechargeRecord;
    }

    public boolean addRecord(BigDecimal money) {
        User user = MainView.currentUser;
        //用户需要登录且金额为正整数
        if (user != null && money.compareTo(BigDecimal.ZERO) > 0) {
            RechargeRecord rechargeRecord = new RechargeRecord();
            rechargeRecord.setRecordId(nextId);
            rechargeRecord.setUserId(user.getId());
            rechargeRecord.setMoney(money);
            rechargeRecord.setDate(DateUtils.getCurrentDateString());
            //写入数据文件
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
                writer.write(resolveRecharge(rechargeRecord));
                writer.newLine();
                writer.flush();
                //id自增
                ++nextId;
                rechargeRecords.add(rechargeRecord);
            } catch (Exception e) {
                ExceptionUtils.recordException("新增充值数据保存异常");
            }
        }
        return false;
    }

    private String resolveRecharge(RechargeRecord rechargeRecord) {
        return rechargeRecord.getRecordId() + "#" +
                rechargeRecord.getUserId() + "#" +
                rechargeRecord.getMoney() + "#" +
                rechargeRecord.getDate();
    }

    public List<RechargeRecord> getRechargeRecords() {
        return rechargeRecords;
    }
}
