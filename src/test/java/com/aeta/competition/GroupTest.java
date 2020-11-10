package com.aeta.competition;


import com.aeta.competition.dao.GroupMapper;
import com.aeta.competition.entity.GroupInfo;
import com.aeta.competition.service.GroupService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;

@SpringBootTest
@ContextConfiguration(classes = CompetitionApplication.class)
public class GroupTest {

    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    private GroupService groupService;
    @Test
    public void testCreateGroup(){
        GroupInfo groupInfo = new GroupInfo();
        groupInfo.setCreateTime(new Date());
        groupInfo.setLeaderId(1);
        groupInfo.setGroupName("world2");
        groupInfo.setIntroduce("hello world");
        Map<String,Object> res = groupService.createGroup(groupInfo);
        if (res == null){
            System.out.println("suc");
            return;
        }

        for (Map.Entry<String, Object> entry : res.entrySet()) {
            System.out.println("key = " + entry.getKey() + ", value = " + entry.getValue());
        }
    }

    @Test
    public void testEasy(){
        String name = groupMapper.selectGroupNameByGroupId(9);
        System.out.println(name);

        int groupId = groupMapper.selectGroupIdByUserId(1);
        System.out.println(groupId);

        GroupInfo groupInfo = groupMapper.selectGroupInfoByGroupName("world");
        System.out.println(groupInfo.getLeaderId());
    }

    @Test
    public void testAddMember(){
       Integer groupId = null;
       int userId = 11;
        Map<String,Object> res = groupService.addMember(groupId,userId);
        for (Map.Entry<String, Object> entry : res.entrySet()) {
            System.out.println("key = " + entry.getKey() + ", value = " + entry.getValue());
        }


    }
}
