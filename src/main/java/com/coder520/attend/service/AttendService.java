package com.coder520.attend.service;

import com.coder520.attend.entity.Attend;
import com.coder520.attend.vo.QueryCondition;
import com.coder520.common.page.PageQueryBean;

/**
 * Created by zrkj on 2017/7/31.
 */
public interface AttendService {

    void signAttend(Attend attend) throws Exception;

    void createAttend(Attend attend);

    PageQueryBean listAttend(QueryCondition queryCondition);

    void checkAttend();

}
