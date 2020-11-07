package com.aeta.competition.service;


import com.aeta.competition.dao.GroupMapper;
import com.aeta.competition.entity.GroupInfo;
import com.aeta.competition.entity.User;
import com.aeta.competition.entity.UserGroup;
import com.aeta.competition.util.CompetitionUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class GroupService {
    @Autowired
    private GroupMapper groupMapper;

    /**
     * 根据groupId查询队名
     */
    public String selectGroupNameByGroupId(int groupId){
        return groupMapper.selectGroupNameByGroupId(groupId);
    }

    /**
     * 创建一个队伍
     */
    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public Map<String,Object> createGroup(GroupInfo groupInfo){
            Map<String,Object> map = new HashMap<>();

            //先对空值做一个判断处理
            if(groupInfo==null){
                throw new IllegalArgumentException("参数不能为空");
            }

            if(StringUtils.isBlank(groupInfo.getGroupName()))
            {
                map.put("groupnameMsg","团队名字不能为空!");
                return map;
            }

            //要验证队名是否已存在
            GroupInfo groupInfo0 = groupMapper.selectGroupInfoByGroupName(groupInfo.getGroupName());
            if(groupInfo0!=null)
            {
                map.put("groupnameMsg","该团队名字已存在");
                return map;

            }
             int leaderId = groupInfo.getLeaderId();
            UserGroup userGroup = groupMapper.selectUserGroupByUserId(leaderId);
             if (userGroup != null){
                map.put("groupMsg","您已加入队伍");
                return map;
                }
             groupMapper.createGroup(groupInfo);

            int groupId = groupInfo.getGroupId();


            groupMapper.addMember(groupId,leaderId);

            return map;

    }

    /**
     * 增加一个成员
     * @param groupId
     * @param userId
     */
    public Map<String,Object> addMember(int groupId,int userId){
        Map<String,Object> map = new HashMap<>();
        //判断这个userId是不是已经在某个队里了
        UserGroup userGroup = groupMapper.selectUserGroupByUserId(userId);
        if (userGroup != null){
            map.put("groupMsg","您已加入队伍");
            return map;
        }
        groupMapper.addMember(groupId,userId);
        return map;
    }

    /**
     * 根据userId查询groupId
     */
    public int selectGroupIdByUserId(int userId){
        return groupMapper.selectGroupIdByUserId(userId);
    }
}
