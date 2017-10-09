package com.coder520.common.task;

import com.coder520.attend.service.AttendService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by zrkj on 2017/7/31.
 */
public class AttendCheckTask {

    @Autowired
    private AttendService attendService;

    public void checkAttend() {
//        System.out.println("hello");
        // 首先获取当天没打卡的人 插入一条记录 并设置缺勤480分钟
        // 如果有打卡记录 检查上下班打卡时间是否正常打卡
        attendService.checkAttend();
    }

}
