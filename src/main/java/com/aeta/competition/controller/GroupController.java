package com.aeta.competition.controller;

import com.aeta.competition.entity.GroupInfo;
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

    /**
     * test
     */
    @RequestMapping(path = "/createGroup",method = RequestMethod.GET)
    public String getCreateGroupPage()
    {
        return "/demo/view";
    }
    @RequestMapping(path = "/joinGroup",method = RequestMethod.GET)
    public String getJoinGroupPage()
    {
        return "/demo/view";
    }



    /**
     * 创建一个团队,创建者要加入队中
     */
    @RequestMapping(path="/createGroup",method = RequestMethod.POST)
    public String createGroup(Model model, GroupInfo groupInfo){
        User user=hostHolder.getUser();
        if(user==null)
            return "/site/login";
        int leaderId = user.getId();
        groupInfo.setLeaderId(leaderId);
        groupInfo.setCreateTime(new Date());
        Map<String,Object> map = groupService.createGroup(groupInfo);
        if(map==null||map.isEmpty()){//创建成功，回到主页？

            return "/site/index";
        }
        else
        {
            model.addAttribute("groupnameMsg",map.get("groupnameMsg"));
            model.addAttribute("groupMsg",map.get("groupMsg"));
            return "/site/createGroup";
        }

    }

    /**
     * 用户加入团队
     * 还要做用户退队吗？
     */
    @RequestMapping(path="/joinGroup",method = RequestMethod.POST)
    public String joinGroup(Model model,int groupId,String groupName){
        User user=hostHolder.getUser();
        if(user==null)
            return "/site/login";

        int userId = user.getId();

        Map<String,Object> map = groupService.addMember(groupId,userId);
        if(map==null||map.isEmpty()){//创建成功，回到主页？

            return "/site/index";
        }
        else
        {
            model.addAttribute("groupMsg",map.get("groupMsg"));
            return "/site/joinGroup";
        }


    }


}
