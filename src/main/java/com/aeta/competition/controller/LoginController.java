package com.aeta.competition.controller;

import com.aeta.competition.entity.UrlMessageEntity;
import com.aeta.competition.entity.User;
import com.aeta.competition.service.UserService;
import com.aeta.competition.util.CompetitionConstant;
import com.mysql.cj.x.protobuf.MysqlxDatatypes;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import com.google.code.kaptcha.Producer;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;


@Controller
public class LoginController implements CompetitionConstant {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    private UserService userService;

    @Autowired(required=true)
    private Producer kaptchaProducer;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    /**
     * 注册界面
     * @return
     */
    @RequestMapping(path = "/register",method = RequestMethod.GET)
    public String getRegisterPage(){
        //试一下看能不能直接打开
        return "/site/register";
    }

    /**
     * 登录界面
     * @return
     */
    @RequestMapping(path = "/login",method = RequestMethod.GET)
    public String getLoginPage()
    {
        return "/site/login";
    }

    /**
     * 注册
     * @param model
     * @param user
     * @return
     */
    @RequestMapping(path = "/register",method = RequestMethod.POST)
    @ResponseBody
    public UrlMessageEntity register(User user){
        Map<String,Object> map = userService.register(user);
        if(map==null||map.isEmpty()){//注册成功，跳转到登录界面
            String url = "/site/login";
            return UrlMessageEntity.getResponse(url);
        }
        else
        {
            String url = "/site/register";
            return UrlMessageEntity.getResponse(url,map);
        }

    }

    /**
     * 生成验证码方法
     * @param response
     */
    @RequestMapping(path="/kaptcha",method = RequestMethod.GET)
    public void getKaptcha(HttpServletResponse response, HttpSession session){
        String text = kaptchaProducer.createText();//生成验证码
        BufferedImage image = kaptchaProducer.createImage(text);
        //将验证码存入session
        session.setAttribute("kaptcha",text);

        //将图片输出给浏览器
        response.setContentType("image/png");
        try {
            OutputStream os = response.getOutputStream();
            ImageIO.write(image,"png",os);
        } catch (IOException e) {
            logger.error("响应验证码失败："+e.getMessage());
        }

    }

    /**
     *
     * @param username
     * @param password
     * @param code
     * @param rememberme
     * @param model
     * @param response
     * @return
     */
    @RequestMapping(path ="/login",method =RequestMethod.POST )
    public String login(String username, String password, String code, boolean rememberme,
                        Model model,HttpSession session,HttpServletResponse response
                       ){
        //先判断验证码对不对 直接判断 业务层不管
        String kaptcha = (String)session.getAttribute("kaptcha");

        if(StringUtils.isBlank(kaptcha)||StringUtils.isBlank(code)||!kaptcha.equalsIgnoreCase(code)) {//忽略大小写
            model.addAttribute("codeMsg","验证码不正确!");
            return "/site/login";
        }
        //检查账号，密码
        //没有勾记住我 就存的时间短一点
        int expiredSeconds=rememberme?REMEMBER_EXPIRED_SECONDS:DEFAULT_EXPIRED_SECONDS;
        Map<String,Object> map=userService.login(username,password,expiredSeconds);
        if(map.containsKey("ticket")){
            Cookie cookie = new Cookie("ticket",map.get("ticket").toString());
            cookie.setPath("contextPath");//整个项目里都有效
            cookie.setMaxAge(expiredSeconds);
            response.addCookie(cookie);
            //转到首页
            return "redirect:/index";
        }
        else
        {
            model.addAttribute("usernameMsg",map.get("usernameMsg"));
            model.addAttribute("passwordMsg",map.get("passwordMsg"));
            return "/site/login";
        }


    }
    @RequestMapping(path="/logout",method = RequestMethod.GET)
    public String logout(@CookieValue("ticket") String ticket){
        userService.logout(ticket);
        //试一下
        return "redirect:/login";
    }




}
