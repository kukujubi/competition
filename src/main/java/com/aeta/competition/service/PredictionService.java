package com.aeta.competition.service;

import com.aeta.competition.dao.GroupMapper;
import com.aeta.competition.dao.PredictionMapper;
import com.aeta.competition.entity.LoginTicket;
import com.aeta.competition.entity.PredictionResult;
import com.aeta.competition.entity.User;
import com.aeta.competition.entity.UserGroup;
import com.aeta.competition.util.CompetitionUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class PredictionService {
   @Autowired
   private PredictionMapper predictionMapper;

   @Autowired
   private GroupMapper groupMapper;

    /**
     *
     *这周新的预测结果
     * 如果一周内重复提交了，覆盖之前的结果
     * 一定要注意设置数据库表的时候 startTime endTime 时间戳要设为不根据当前时间戳更新
     */
    public Map<String,Object> predict(int userId,int whether,Double latitude,Double longitude){
       Map<String,Object> map = new HashMap<>();
        //如果这个人还没创建团队，则弹出信息
        UserGroup userGroup = groupMapper.selectUserGroupByUserId(userId);
        if (userGroup == null){
            map.put("groupMsg","您需要加入队伍");
            return map;
        }
        int groupId = userGroup.getGroupId();
        String groupName = groupMapper.selectGroupNameByGroupId(groupId);
        Date startTime = CompetitionUtil.getCurrentWeekDayStartTime();
        Date endTime = CompetitionUtil.getCurrentWeekDayEndTime();
        PredictionResult res = predictionMapper.selectByGroupIdAndTime(groupId,startTime,endTime);
        Date createTime = new Date();
        if (latitude==null)
            latitude=0.0;
        if (longitude==null)
            longitude=0.0;
        if (res!=null){
            predictionMapper.updatePrediction(groupId,createTime,whether,latitude,longitude);
            map.put("predictionMsg","您已成功修改您的结果");
            return map;
        }

       predictionMapper.insertPrediction(groupId,groupName,startTime,endTime,createTime,whether,latitude,longitude);
        map.put("predictionMsg","您已成功提交您的结果");
        return map;
    }


}

