package com.aeta.competition.controller;

import com.aeta.competition.entity.*;
import com.aeta.competition.service.GroupService;
import com.aeta.competition.service.PredictionService;
import com.aeta.competition.service.UserService;
import com.aeta.competition.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.Map;

@Controller
public class PredictionController {
    @Autowired
    private PredictionService predictionService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @RequestMapping(path = "/prediction",method = RequestMethod.GET)
    public String getPredictionPage()
    {
        return "/site/prediciton";
    }




    /**
     * 提交预测结果
     */
    @RequestMapping(path="/prediction",method = RequestMethod.POST)
    @ResponseBody
    public UrlMessageEntity prediction(@RequestParam("whether") Integer whether,
                                             @RequestParam(value = "latitude",required = false) Double latitude,@RequestParam(value = "longitude",required = false)Double longitude){
        User user=hostHolder.getUser();
        if(user==null){
            String url = "/site/login";
            return UrlMessageEntity.getResponse(url);
        }

        int userId = user.getId();

        Map<String,Object> res = predictionService.predict(userId,whether,latitude,longitude);
        return UrlMessageEntity.getResponse(res);
    }

}
