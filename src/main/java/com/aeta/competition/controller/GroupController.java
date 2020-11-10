package com.aeta.competition.controller;

import com.aeta.competition.entity.GroupInfo;
import com.aeta.competition.entity.UrlMessageEntity;
import com.aeta.competition.entity.User;
import com.aeta.competition.service.GroupService;
import com.aeta.competition.service.UserService;
import com.aeta.competition.util.CompetitionUtil;
import com.aeta.competition.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.Map;

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
//        User user=hostHolder.getUser();
//        if(user==null){
//            String url = "/site/login";
//            return UrlMessageEntity.getResponse(url);
//        }

//        int leaderId = user.getId();
        int leaderId = 10;
        groupInfo.setLeaderId(leaderId);
        groupInfo.setCreateTime(new Date());
        Map<String,Object> map = groupService.createGroup(groupInfo);
        if(map==null||map.isEmpty()){//创建成功，回到主页？
            String url = "/site/index";
            return UrlMessageEntity.getResponse(url);
        }
        else
        {
           String url = "/site/createGroup";
            return UrlMessageEntity.getResponse(url,map);
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
        if(user==null){
            String url = "/site/login";
            return UrlMessageEntity.getResponse(url);
        }

        int userId = user.getId();

        Map<String,Object> map = groupService.addMember(groupId,userId);
        if(map==null||map.isEmpty()){//加入成功，回到主页？
            String url = "/site/index";
            return UrlMessageEntity.getResponse(url);
        }
        else
        {
            String url = "/site/joinGroup";
           return UrlMessageEntity.getResponse(url,map);

        }


    }


}
