package com.coder520.attend.service;

import com.coder520.attend.dao.AttendMapper;
import com.coder520.attend.entity.Attend;
import com.coder520.attend.vo.QueryCondition;
import com.coder520.common.page.PageQueryBean;
import com.coder520.common.utils.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zrkj on 2017/7/31.
 */
@Service("attendServiceImpl")
public class AttendServiceImpl implements AttendService {

    // 中午12点 判定上下午
    private static final int NOON_HOUR = 12 ;
    private static final int NOON_MINUTE = 00;

    // 上下班时间判定
    private static final int MORNING_HOUR = 9;
    private static final int MORNING_MINUTE = 30;
    private static final int EVENING_HOUR = 18;
    private static final int EVENING_MINUTE = 30;


    // 缺勤一整天
    private static final Integer ABSENCE_DAY =480 ;

    // 考勤状态
    private static final Byte ATTEND_STATUS_ABNORMAL = 2;
    private static final Byte ATTEND_STATUS_NORMAL = 1;

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private Log log = LogFactory.getLog(AttendServiceImpl.class);

    @Autowired
    private AttendMapper attendMapper;

    @Override
    public void signAttend(Attend attend) throws Exception {
        try {
            Date today = new Date();
            attend.setAttendDate(today);
            attend.setAttendWeek((byte)DateUtils.getTodayWeek());
            //查询当天 此人有没有打卡记录
            Attend todayRecord=attendMapper.selectTodaySignRecord(attend.getUserId());
            Date noon = DateUtils.getDate(NOON_HOUR,NOON_MINUTE);
            Date morningAttend = DateUtils.getDate(MORNING_HOUR,MORNING_MINUTE);
            if(todayRecord==null){
                //打卡记录还不存在
                if(today.compareTo(noon)<=0){
                    //打卡时间 早于12点 上午打卡
                    attend.setAttendMoring(today);
                    //计算打卡时间是不是迟到
                    if(today.compareTo(morningAttend)>0){
                        //大于九点半迟到了
                        attend.setAttendStatus(ATTEND_STATUS_ABNORMAL);
                        attend.setAbsence(DateUtils.getMunite(morningAttend,today));
                    }

                }else {
                    attend.setAttendEvening(today);
                }
                attendMapper.insertSelective(attend);
            }else{
                // 已经有记录
                if(today.compareTo(noon)<=0){
                    //打卡时间 早于12点 上午打卡
                    return;
                }else {
                    //晚上打卡
                    todayRecord.setAttendEvening(today);
                    //判断打卡时间是不是18.30以后是不是早退
                    Date eveningAttend = DateUtils.getDate(EVENING_HOUR,EVENING_MINUTE);
                    if(today.compareTo(eveningAttend)<0){
                        //早于下午六点半 早退
                        todayRecord.setAttendStatus(ATTEND_STATUS_ABNORMAL);
                        todayRecord.setAbsence(DateUtils.getMunite(today,eveningAttend));
                    }else {
                        todayRecord.setAttendStatus(ATTEND_STATUS_NORMAL);
                        todayRecord.setAbsence(0);
                    }
                    attendMapper.updateByPrimaryKeySelective(todayRecord);
                }
            }

        }catch (Exception e){
            log.error("用户签到异常",e);
            throw e;
        }
    }

    @Override
    public void createAttend(Attend attend) {
        attendMapper.insertSelective(attend);
    }

    @Override
    public PageQueryBean listAttend(QueryCondition condition) {
        // 根据条件查询 count记录数目
        int count = attendMapper.countByCondition(condition);
        PageQueryBean pageResult = new PageQueryBean();
        if (count > 0) {
            pageResult.setTotalRows(count);
            pageResult.setCurrentPage(condition.getCurrentPage());
            pageResult.setPageSize(condition.getPageSize());
            // 查询到的数据
            List<Attend> attendList = attendMapper.selectAttendPage(condition);
            pageResult.setItems(attendList);
        }
        // 如果有记录 查询分页数据

        return pageResult;
    }


    public void checkAttend() {
        // 查询缺勤用户ID  插入打卡记录 设置异常 缺勤480min
        List<Long> userIdList = attendMapper.selectTodayAbsence();

        if(CollectionUtils.isNotEmpty(userIdList)){

            List<Attend> attendList = new ArrayList<Attend>();
            for (Long userID: userIdList){
                Attend attend = new Attend();
                attend.setUserId(userID);
                attend.setAttendDate(new Date());
                attend.setAttendWeek((byte) DateUtils.getTodayWeek());
                attend.setAbsence(ABSENCE_DAY);
                attend.setAttendStatus(ATTEND_STATUS_ABNORMAL);
                attendList.add(attend);
            }

            // 批量插入
            attendMapper.batchInsert(attendList);
        }

        // 检查下班打卡时间
        List<Attend> absenceList = attendMapper.selectTodayEveningAbsence();
        if(CollectionUtils.isNotEmpty(absenceList)){
            for(Attend attend : absenceList){
                attend.setAbsence(ABSENCE_DAY);
                attend.setAttendStatus(ATTEND_STATUS_ABNORMAL);
                attendMapper.updateByPrimaryKeySelective(attend);
            }
        }
    }

}
