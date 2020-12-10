package com.aeta.competition.controller;

import com.aeta.competition.entity.*;
import com.aeta.competition.service.GroupService;
import com.aeta.competition.service.UserService;
import com.aeta.competition.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class UserController {
    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private GroupService groupService;

    @Autowired
    private UserService userService;

    /**
     * 当前用户的基本信息
     */
    @RequestMapping(path="/OwnInfo",method = RequestMethod.GET)
    @ResponseBody
    public UrlMessageEntity selectOwnInfo(){
        User user=hostHolder.getUser();
        String codeRes;
        if(user==null){
            String url = "/site/login";
            codeRes = "failure";
            return UrlMessageEntity.getResponse(url,codeRes);
        }
        Map<String,Object> map = new HashMap<>();
        map.put("data",user);
        codeRes = "success";
        return UrlMessageEntity.getResponse(codeRes,map);
    }

    /**
     * 当前用户所在团队信息
     */
    @RequestMapping(path="/OwnGroupInfo",method = RequestMethod.GET)
    @ResponseBody
    public UrlMessageEntity selectOwnGroupInfo(){
         User user=hostHolder.getUser();
       // User user = userService.findUserById(7);
        String codeRes;
        HashMap<String,Object> map = new HashMap<>();
        if(user==null){
            String url = "/site/login";
            codeRes = "failure";
            return UrlMessageEntity.getResponse(url,codeRes);
        }

        UserGroup userGroup = groupService.selectUserGroupByUserId(user.getId());

        if (userGroup == null){
            map.put("msg","您未加入队伍");
            codeRes = "success";
            return UrlMessageEntity.getResponse(codeRes,map);
        }
        if(userGroup.getStatus()==0){
            map.put("msg","2您加入的队伍还需等待队长确认");
            codeRes = "success";
            return UrlMessageEntity.getResponse(codeRes,map);
        }

        int groupId = userGroup.getGroupId();
        GroupInfo groupInfo = groupService.selectGroupInfoByGroupId(groupId);
        map.put("groupInfo",groupInfo);
        List<Member> members = new ArrayList<>();
        //查出这一队的人各自的信息
        List<UserGroup> membersId = groupService.selectUserGroupByGroupId(groupId);
        for (UserGroup memberGroup: membersId){
            int groupMemberId = memberGroup.getUserId();
            User groupMember = userService.findUserById(groupMemberId);
            Member member = new Member();
            member.setUserId(groupMemberId);
            member.setStatus(memberGroup.getStatus());
            member.setGroupId(groupId);
            member.setUserName(groupMember.getUsername());
            member.setEmail(groupMember.getEmail());
            members.add(member);
        }
        map.put("members",members);
        Map<String,Object> mapFinal = new HashMap<>();
        mapFinal.put("data",map);
        codeRes = "success";
        return UrlMessageEntity.getResponse(codeRes,mapFinal);

    }
}
