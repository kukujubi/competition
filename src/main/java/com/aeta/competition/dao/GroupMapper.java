package com.aeta.competition.dao;

import com.aeta.competition.entity.GroupInfo;

import com.aeta.competition.entity.UserGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GroupMapper {

    String selectGroupNameByGroupId(int groupId);

    int selectGroupIdByUserId(int userId);

    UserGroup selectUserGroupByUserId(int userId);

    GroupInfo selectGroupInfoByGroupName(String name);

    GroupInfo selectGroupInfoByLeaderId(int leaderId);

    GroupInfo selectGroupInfoByGroupId(int groupId);

    int createGroup(@Param("data") GroupInfo groupInfo);

    int addMember(@Param("groupId") int groupId,@Param("userId") int userId,@Param("status") int status);

    int confirmMember(@Param("groupId") int groupId,@Param("userId") int userId,@Param("status") int status);

    List<UserGroup> selectUserGroupByGroupId(int groupId);

    List<GroupInfo> selectAllGroupInfo();

}
