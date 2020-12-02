package com.aeta.competition.controller;

import com.aeta.competition.entity.*;
import com.aeta.competition.service.GroupService;
import com.aeta.competition.service.UserService;
import com.aeta.competition.util.CompetitionUtil;
import com.aeta.competition.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
public class GroupController {
    @Autowired
    private GroupService groupService;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;


    @RequestMapping(path = "/createGroup",method = RequestMethod.GET)
    public String getCreateGroupPage()
    {
        return "/site/createGroup";
    }
    @RequestMapping(path = "/joinGroup",method = RequestMethod.GET)
    public String getJoinGroupPage()
    {
        return "/site/joinGroup";
    }



    /**
     * 创建一个团队,创建者要加入队中
     */
    @RequestMapping(path="/createGroup",method = RequestMethod.POST)
    @ResponseBody
    public UrlMessageEntity createGroup(GroupInfo groupInfo){
        User user=hostHolder.getUser();
        if(user==null){
            String url = "/site/login";
            return UrlMessageEntity.getResponse(url);
        }

        int leaderId = user.getId();
       // int leaderId = 10;
        groupInfo.setLeaderId(leaderId);
        groupInfo.setCreateTime(new Date());
        Map<String,Object> map = groupService.createGroup(groupInfo);
        String codeRes;
        if(map==null||map.isEmpty()){//创建成功，回到主页？
            String url = "/site/index";
            codeRes = "success";
            return UrlMessageEntity.getResponse(url,codeRes);
        }
        else
        {
            codeRes = "failure";
           String url = "/site/createGroup";
            return UrlMessageEntity.getResponse(url,codeRes,map);
        }

    }

    /**
     * 用户加入团队
     * 还要做用户退队吗？
     */
    @RequestMapping(path="/joinGroup",method = RequestMethod.POST)
    @ResponseBody
    public UrlMessageEntity joinGroup(Integer groupId){
        User user=hostHolder.getUser();
        String codeRes;
        if(user==null){
            String url = "/site/login";
            codeRes = "failure";
            return UrlMessageEntity.getResponse(url,codeRes);
        }

        int userId = user.getId();
        //int userId = 11;
        Map<String,Object> map = groupService.addMember(groupId,userId);
        if(map==null||map.isEmpty()){//加入成功，回到主页？
            String url = "/site/index";
            codeRes = "success";
            map.put("groupMsg","等待队长确认");
            return UrlMessageEntity.getResponse(url,codeRes,map);
        }
        else
        {
            String url = "/site/joinGroup";
            codeRes = "failure";
           return UrlMessageEntity.getResponse(url,codeRes,map);

        }


    }

    /**
     * 队长确认队员加入
     * @param memberId
     * @param status 1确认加入 2拒绝加入
     * @return
     */
    @RequestMapping(path="/confirmGroup",method = RequestMethod.POST)
    @ResponseBody
    public UrlMessageEntity confirmGroup(Integer memberId,Integer status){
        User user=hostHolder.getUser();
        String codeRes;
        if(user==null){
            String url = "/site/login";
            codeRes = "failure";
            return UrlMessageEntity.getResponse(url,codeRes);
        }
        int userId = user.getId();
      //  int userId = 1;
        Map<String,Object> map = new HashMap<>();
        //如果不是leader则无权限
        GroupInfo groupInfo = groupService.selectGroupInfoByLeaderId(userId);
        if (groupInfo == null){
            map.put("userMsg","没有权限");
            codeRes = "failure";
            return UrlMessageEntity.getResponse(codeRes,map);
        }
        int groupId = groupInfo.getGroupId();

         groupService.confirmMember(groupId,memberId,status);
        codeRes = "success";
        return UrlMessageEntity.getResponse(codeRes);

    }

    /**
     *根据groupName,查出一个队的所有信息
     */
    @RequestMapping(path="/GroupInfo/{groupName}",method = RequestMethod.GET)
    @ResponseBody
    public UrlMessageEntity selectGroupInfo(@PathVariable("groupName") String groupName){
        GroupInfo groupInfo = groupService.selectGroupInfoByGroupName(groupName);
        Map<String,Object> map = new HashMap<>();
        map.put("groupInfo",groupInfo);
        List<Member> members = new ArrayList<>();
        int groupId = groupInfo.getGroupId();
        //查出这一队的人各自的信息
        List<UserGroup> membersId = groupService.selectUserGroupByGroupId(groupId);
        for (UserGroup userGroup: membersId){
            int userId = userGroup.getUserId();
            User user = userService.findUserById(userId);
            Member member = new Member();
            member.setUserId(userId);
            member.setStatus(userGroup.getStatus());
            member.setGroupId(groupId);
            member.setUserName(user.getUsername());
            member.setEmail(user.getEmail());
            members.add(member);
        }
        map.put("members",members);
        String codeRes = "success";
        return UrlMessageEntity.getResponse(codeRes,map);

    }


}
