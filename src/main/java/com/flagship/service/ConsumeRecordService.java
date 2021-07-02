package com.flagship.service;

import com.flagship.common.Constant;
import com.flagship.exception.ExceptionEnum;
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
 * @Description 消费记录Service
 */
public class ConsumeRecordService extends BaseService {
    /**
     * 消费记录列表
     */
    private final List<ConsumeRecord> consumeRecords = new ArrayList<>();
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
        String filePath = Objects.requireNonNull(UserService.class.getClassLoader().getResource(Constant.FilePath.CONSUME_RECORD_DB)).getPath();
        file = new File(filePath);
        //读取文件
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (reader.ready()) {
                //实现ID自增
                ++nextId;
                //解析消费数据并加入到集合中
                String[] dataArr = reader.readLine().split(Constant.DELIMITER);
                ConsumeRecord record = resolveStringArr(dataArr);
                //如果是当前用户不是管理员，则只能获取自己的消费记录
                if (user.getRole().equals(Constant.UserRole.USER) && !user.getId().equals(record.getUserId())) {
                    continue;
                }
                //加入列表
                consumeRecords.add(record);
            }
        } catch (IOException e) {
            ExceptionUtils.recordException(ExceptionEnum.INIT_CONSUME_RECORD_ERR.getErrMsg());
        }
    }

    /**
     * 清空列表
     */
    @Override
    public void clear() {
        this.consumeRecords.clear();
    }

    /**
     * 从字符串数组中解析出消费记录数据
     */
    private ConsumeRecord resolveStringArr(String[] dataArr) {
        ConsumeRecord consumeRecord = new ConsumeRecord();
        consumeRecord.setRecordId(Integer.valueOf(dataArr[0]));
        consumeRecord.setUserId(Integer.valueOf(dataArr[1]));
        consumeRecord.setMoney(new BigDecimal(dataArr[2]));
        consumeRecord.setDate(DateUtils.getCurrentDateString());
        return consumeRecord;
    }

    /**
     * 增加一条消费记录
     */
    public void addRecord(BigDecimal money) {
        User user = UserService.currentUser;
        //用户需要登录且金额为正整数
        if (user != null && money.compareTo(BigDecimal.ZERO) > 0) {
            //构造消费记录
            ConsumeRecord consumeRecord = new ConsumeRecord();
            consumeRecord.setRecordId(nextId);
            consumeRecord.setUserId(user.getId());
            consumeRecord.setMoney(money);
            consumeRecord.setDate(DateUtils.getCurrentDateString());
            //输出流
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
                //写入数据文件
                writer.write(resolveConsume(consumeRecord));
                writer.newLine();
                writer.flush();
                //id自增
                ++nextId;
                //加入列表
                consumeRecords.add(consumeRecord);
            } catch (Exception e) {
                ExceptionUtils.recordException(ExceptionEnum.ADD_CONSUME_RECORD_ERR.getErrMsg());
            }
        }
    }

    /**
     * 从消费记录对象构造出字符串
     */
    private String resolveConsume(ConsumeRecord consumeRecord) {
        return consumeRecord.getRecordId() + Constant.DELIMITER +
                consumeRecord.getUserId() + Constant.DELIMITER +
                consumeRecord.getMoney() + Constant.DELIMITER +
                consumeRecord.getDate();
    }

    /**
     * 获取消费数据列表
     */
    public List<ConsumeRecord> getConsumeRecords() {
        return consumeRecords;
    }
}
