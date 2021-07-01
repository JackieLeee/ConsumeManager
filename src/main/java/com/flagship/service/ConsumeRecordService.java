package com.flagship.service;

import com.flagship.MainView;
import com.flagship.common.Constant;
import com.flagship.pojo.ConsumeRecord;
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
public class ConsumeRecordService {
    private final List<ConsumeRecord> consumeRecords = new ArrayList<>();
    private Integer nextId = 1;
    private File file;

    public void initRecords(boolean onlyCurrentUser) {
        User user = MainView.currentUser;
        if (user == null) {
            return;
        }
        //获取文件路径
        String filePath = Objects.requireNonNull(UserService.class.getClassLoader().getResource(Constant.FilePath.CONSUME_RECORD_DB)).getPath();
        file = new File(filePath);
        //读取文件
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (reader.ready()) {
                //实现ID自增
                ++nextId;
                //解析消费数据并加入到集合中
                String[] dataArr = reader.readLine().split("#");
                ConsumeRecord record = resolveStringArr(dataArr);
                //是否只保存当前用户的消费记录
                if (onlyCurrentUser) {
                    if (user.getRole().equals(1) || !user.getId().equals(record.getUserId())) {
                        continue;
                    }
                }
                consumeRecords.add(record);
            }
        } catch (IOException e) {
            ExceptionUtils.recordException("消费数据初始化异常");
        }
    }

    private ConsumeRecord resolveStringArr(String[] dataArr) {
        ConsumeRecord consumeRecord = new ConsumeRecord();
        consumeRecord.setRecordId(Integer.valueOf(dataArr[0]));
        consumeRecord.setUserId(Integer.valueOf(dataArr[1]));
        consumeRecord.setMoney(new BigDecimal(dataArr[2]));
        consumeRecord.setDate(DateUtils.getCurrentDateString());
        return consumeRecord;
    }

    public boolean addRecord(BigDecimal money) {
        User user = MainView.currentUser;
        //用户需要登录且金额为正整数
        if (user != null && money.compareTo(BigDecimal.ZERO) > 0) {
            ConsumeRecord consumeRecord = new ConsumeRecord();
            consumeRecord.setRecordId(nextId);
            consumeRecord.setUserId(user.getId());
            consumeRecord.setMoney(money);
            consumeRecord.setDate(DateUtils.getCurrentDateString());
            //写入数据文件
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
                writer.write(resolveConsume(consumeRecord));
                writer.newLine();
                writer.flush();
                //id自增
                ++nextId;
                consumeRecords.add(consumeRecord);
            } catch (Exception e) {
                ExceptionUtils.recordException("新增消费数据保存异常");
            }
        }
        return false;
    }

    private String resolveConsume(ConsumeRecord consumeRecord) {
        return consumeRecord.getRecordId() + "#" +
                consumeRecord.getUserId() + "#" +
                consumeRecord.getMoney() + "#" +
                consumeRecord.getDate();
    }

    public List<ConsumeRecord> getConsumeRecords() {
        return consumeRecords;
    }
}
