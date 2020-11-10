package com.aeta.competition;


import com.aeta.competition.controller.PredictionController;
import com.aeta.competition.dao.GroupMapper;
import com.aeta.competition.entity.GroupInfo;
import com.aeta.competition.service.GroupService;
import com.aeta.competition.service.PredictionService;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Date;
import java.util.Map;

import static org.junit.Assert.assertEquals;


@SpringBootTest
@ContextConfiguration(classes = CompetitionApplication.class)
public class PredictTest {

    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    private GroupService groupService;

    @Autowired
    private PredictionService predictionService;
    @Test
    public void testPrediction(){
       int userId = 10;
       int whether = 1;
       Double latitude = null;
       Double longitude = null;
        Map<String,Object> res = predictionService.predict(userId,whether,latitude,longitude);


        for (Map.Entry<String, Object> entry : res.entrySet()) {
            System.out.println("key = " + entry.getKey() + ", value = " + entry.getValue());
        }
    }

    }






