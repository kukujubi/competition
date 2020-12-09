package com.aeta.competition;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = CompetitionApplication.class)
public class ControllerTest {
    protected MockMvc mockMvc;

    @Autowired
    protected WebApplicationContext wac;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();  //初始化MockMvc对象
    }

    @Test
    public void predictionControllerTest() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                post("/prediction")          //请求的url,请求的方法是get
                        .contentType(MediaType.APPLICATION_JSON)//数据的格式
                        .param("whether", "1")   //添加参数(可以添加多个)
                        .param("latitude", "")   //添加参数(可以添加多个)
                        .param("longitude","")
                        .param("userId","1")
        )
                .andExpect(status().isOk()) ;   //返回的状态是200
        MockHttpServletResponse response = resultActions.andReturn().getResponse();

        response.setCharacterEncoding("UTF-8");


                resultActions.andDo(print());         //打印出请求和相应的内容
        String responseString = response.getContentAsString();   //将相应的数据转换为字符串
        System.out.println("-----返回的json = " + responseString);
    }


    @Test
    public void downloadDayControllerTest() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                post("/downloadDay")          //请求的url,请求的方法是get
                        .contentType(MediaType.APPLICATION_JSON)//数据的格式
                        .param("day", "20201108")   //添加参数(可以添加多个)

        )
                .andExpect(status().isOk()) ;   //返回的状态是200
        MockHttpServletResponse response = resultActions.andReturn().getResponse();

        response.setCharacterEncoding("UTF-8");


        resultActions.andDo(print());         //打印出请求和相应的内容
        String responseString = response.getContentAsString();   //将相应的数据转换为字符串
        System.out.println("-----返回的json = " + responseString);
    }
    @Test
    public void registerControllerTest() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                post("/register")          //请求的url,请求的方法是get
                        .contentType(MediaType.APPLICATION_JSON)//数据的格式
                        .param("username", "yao")   //添加参数(可以添加多个)
                        .param("password", "123456")   //添加参数(可以添加多个)
                        .param("email","123@qq.com")

        )
                .andExpect(status().isOk()) ;   //返回的状态是200
        MockHttpServletResponse response = resultActions.andReturn().getResponse();

        response.setCharacterEncoding("UTF-8");


        resultActions.andDo(print());         //打印出请求和相应的内容
        String responseString = response.getContentAsString();   //将相应的数据转换为字符串
        System.out.println("-----返回的json = " + responseString);
    }

    @Test
    public void createGroupControllerTest() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                post("/createGroup")          //请求的url,请求的方法是get
                        .contentType(MediaType.APPLICATION_JSON)//数据的格式
                        .param("groupName", "123")   //添加参数(可以添加多个)
                        .param("introduce", "123456")   //添加参数(可以添加多个)


        )
                .andExpect(status().isOk()) ;   //返回的状态是200
        MockHttpServletResponse response = resultActions.andReturn().getResponse();

        response.setCharacterEncoding("UTF-8");


        resultActions.andDo(print());         //打印出请求和相应的内容
        String responseString = response.getContentAsString();   //将相应的数据转换为字符串
        System.out.println("-----返回的json = " + responseString);
    }
    @Test
    public void joinGroupControllerTest() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                post("/joinGroup")          //请求的url,请求的方法是get
                        .contentType(MediaType.APPLICATION_JSON)//数据的格式
                        .param("groupId", "9")   //添加参数(可以添加多个)



        )
                .andExpect(status().isOk()) ;   //返回的状态是200
        MockHttpServletResponse response = resultActions.andReturn().getResponse();

        response.setCharacterEncoding("UTF-8");


        resultActions.andDo(print());         //打印出请求和相应的内容
        String responseString = response.getContentAsString();   //将相应的数据转换为字符串
        System.out.println("-----返回的json = " + responseString);
    }

    @Test
    public void confirmGroupControllerTest() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                post("/confirmGroup")          //请求的url,请求的方法是get
                        .contentType(MediaType.APPLICATION_JSON)//数据的格式
                        .param("memberId", "11")   //添加参数(可以添加多个)
                        .param("status", "1")


        )
                .andExpect(status().isOk()) ;   //返回的状态是200
        MockHttpServletResponse response = resultActions.andReturn().getResponse();

        response.setCharacterEncoding("UTF-8");


        resultActions.andDo(print());         //打印出请求和相应的内容
        String responseString = response.getContentAsString();   //将相应的数据转换为字符串
        System.out.println("-----返回的json = " + responseString);
    }


    @Test
    public void selectGroupInfo() throws Exception {
        String mvcResultGet = mockMvc.perform(MockMvcRequestBuilders.get("/GroupInfo/world")
                // .param("msg", "getRequest param msg") // 给get请求添加参数
                .contentType(MediaType.APPLICATION_JSON))// 设置数据格式
                .andDo(MockMvcResultHandlers.print())     // 打印输出发出请求的详细信息
                .andExpect(status().isOk())     // 对返回值进行断言
                .andReturn().getResponse().getContentAsString();        // 获取方法的返回值

        System.out.println("mvcResultGet  ====" + mvcResultGet);
    }

    @Test
    public void selectAllGroupInfo() throws Exception {
        String mvcResultGet = mockMvc.perform(MockMvcRequestBuilders.get("/GroupInfo")
                // .param("msg", "getRequest param msg") // 给get请求添加参数
                .contentType(MediaType.APPLICATION_JSON))// 设置数据格式
                .andDo(MockMvcResultHandlers.print())     // 打印输出发出请求的详细信息
                .andExpect(status().isOk())     // 对返回值进行断言
                .andReturn().getResponse().getContentAsString();        // 获取方法的返回值

        System.out.println("mvcResultGet  ====" + mvcResultGet);
    }
    @Test
    public void addComment() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                post("/addComment")          //请求的url,请求的方法是get
                        .contentType(MediaType.APPLICATION_JSON)//数据的格式
                        .param("entityType", "2")   //添加参数(可以添加多个)
                        .param("entityId", "1")   //添加参数(可以添加多个)
                        .param("targetId","10")
                        .param("content","hihihi")


        )
                .andExpect(status().isOk()) ;   //返回的状态是200
        MockHttpServletResponse response = resultActions.andReturn().getResponse();

        response.setCharacterEncoding("UTF-8");


        resultActions.andDo(print());         //打印出请求和相应的内容
        String responseString = response.getContentAsString();   //将相应的数据转换为字符串
        System.out.println("-----返回的json = " + responseString);
    }

    @Test
    public void selectComment() throws Exception {
        String mvcResultGet = mockMvc.perform(MockMvcRequestBuilders.get("/comments")
               // .param("msg", "getRequest param msg") // 给get请求添加参数
                .contentType(MediaType.APPLICATION_JSON))// 设置数据格式
                .andDo(MockMvcResultHandlers.print())     // 打印输出发出请求的详细信息
                .andExpect(status().isOk())     // 对返回值进行断言
                .andReturn().getResponse().getContentAsString();        // 获取方法的返回值

        System.out.println("mvcResultGet  ====" + mvcResultGet);
    }


    @Test
    public void testgit() throws Exception {


        System.out.println("mvcResultGet  ====" );
    }
}

