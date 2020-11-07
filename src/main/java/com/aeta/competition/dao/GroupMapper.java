package com.aeta.competition.dao;

import com.aeta.competition.entity.GroupInfo;

import com.aeta.competition.entity.UserGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface GroupMapper {

    String selectGroupNameByGroupId(int groupId);

    int selectGroupIdByUserId(int userId);

    UserGroup selectUserGroupByUserId(int userId);

    GroupInfo selectGroupInfoByGroupName(String name);

    int createGroup(@Param("data") GroupInfo groupInfo);

    int addMember(@Param("groupId") int groupId,@Param("userId") int userId);

}
