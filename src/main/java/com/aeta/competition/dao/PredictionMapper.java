package com.aeta.competition.dao;

import com.aeta.competition.entity.PredictionResult;
import com.aeta.competition.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

@Mapper
public interface PredictionMapper {
    PredictionResult selectByGroupId(int groupId);

    PredictionResult selectByGroupIdAndTime(
            @Param("groupId")int groupId,@Param("startTime")Date startTime,@Param("endTime") Date endTime);

    PredictionResult selectByTime(@Param("startTime")Date startTime,@Param("endTime") Date endTime);

    int insertPrediction(int groupId,String groupName,Date startTime,Date endTime,Date createTime,int whether,
                         double latitude,double longitude);

    int updatePrediction(@Param("groupId") int groupId,@Param("createTime") Date createTime,@Param("whether") int whether,
                         @Param("latitude") double latitude,@Param("longitude") double longitude);
}
