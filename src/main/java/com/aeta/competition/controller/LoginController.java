package com.aeta.competition.controller;

import com.aeta.competition.entity.UrlMessageEntity;
import com.aeta.competition.entity.User;
import com.aeta.competition.service.UserService;
import com.aeta.competition.util.CompetitionConstant;
import com.aeta.competition.util.CompetitionUtil;
import com.aeta.competition.util.MailClient;
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
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
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

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

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
     * @param user
     * @return
     */
    @RequestMapping(path = "/register",method = RequestMethod.POST)
    @ResponseBody
    public UrlMessageEntity register(User user){
        Map<String,Object> map = userService.register(user);
        String url;
        if(map==null||map.isEmpty()){//注册成功，先弹出这个operate-result,激活成功后才跳转到登录界面？
            url = "/site/operate-result";
            map.put("msg","注册成功，我们已经向您的邮箱发送了一封激活邮件，请尽快激活！");
            map.put("target","/site/login");
        }
        else
        {
            url = "/site/register";
        }
        return UrlMessageEntity.getResponse(url,map);

    }

    /**
     * 邮箱激活
     * @param userId
     * @param code
     * @return
     */
    //http://localhost:8080/competition/activation/101/code
    @RequestMapping(path = "/activation/{userId}/{code}",method=RequestMethod.GET)
    @ResponseBody
    public UrlMessageEntity activation(@PathVariable("userId") int userId,@PathVariable("code") String code){
        int result=userService.activation(userId,code);
        Map<String,Object> map = new HashMap<>();
        String url;
        if(result==ACTIVATION_SUCCESS){
            map.put("msg","激活成功，您的账号已经可以正常使用了！");
            url = "/site/login";
        }
        else if(result==ACTIVATION_REPEAT){
            map.put("msg","无效操作，该账号已经激活过了！");
            url = "/site/login";
        }
        else{
            map.put("msg","激活失败，您提供的激活码不正确");
            url = "/site/register";
        }
        return  UrlMessageEntity.getResponse(url,map);
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
     *网页测的时候是正确的 没有用单元测试
     * @param username
     * @param password
     * @param code
     * @param rememberme
     * @param response
     * @return
     */
    @RequestMapping(path ="/login",method =RequestMethod.POST )
    @ResponseBody
    public UrlMessageEntity login(String username, String password, String code, boolean rememberme,
                        HttpSession session,HttpServletResponse response
                       ){
        //先判断验证码对不对 直接判断 业务层不管
        String kaptcha = (String)session.getAttribute("kaptcha");

        if(StringUtils.isBlank(kaptcha)||StringUtils.isBlank(code)||!kaptcha.equalsIgnoreCase(code)) {//忽略大小写
            Map<String,Object> message = new HashMap<>();
            message.put("codeMsg","验证码不正确!");
            String url = "/site/login";
            return UrlMessageEntity.getResponse(url,message);
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
            String url = "redirect:/index";
            return UrlMessageEntity.getResponse(url);
        }
        else
        {
            Map<String,Object> message = new HashMap<>();
            message.put("usernameMsg",map.get("usernameMsg"));
            message.put("passwordMsg",map.get("passwordMsg"));
            String url = "/site/login";
            return UrlMessageEntity.getResponse(url,message);
        }


    }

    //还没测过
    @RequestMapping(path="/logout",method = RequestMethod.GET)
    public String logout(@CookieValue("ticket") String ticket){
        userService.logout(ticket);
        //试一下
        return "redirect:/login";
    }



    /*
    忘记密码
     */
    @RequestMapping(path = "/forget",method = RequestMethod.GET)
    public String getForgetPage(){
        return "/site/forget";
    }


    // 获取验证码
    @RequestMapping(path = "/forget/code", method = RequestMethod.GET)
    @ResponseBody
    public String getForgetCode(String email, HttpSession session) {
        if (StringUtils.isBlank(email)) {
            return CompetitionUtil.getJSONString(1, "邮箱不能为空！");
        }

        // 发送邮件
        Context context = new Context();
        context.setVariable("email", email);
        String code = CompetitionUtil.generateUUID().substring(0, 4);
        context.setVariable("verifyCode", code);
        String content = templateEngine.process("/mail/forget", context);
        mailClient.sendMail(email, "找回密码", content);

        // 保存验证码
        session.setAttribute("verifyCode", code);

        return CompetitionUtil.getJSONString(0);
    }

    // 重置密码
    @RequestMapping(path = "/forget/password", method = RequestMethod.POST)
    @ResponseBody
    public UrlMessageEntity resetPassword(String email, String verifyCode, String password, HttpSession session) {
        String code = (String) session.getAttribute("verifyCode");
        Map<String, Object> message = new HashMap<>();
        String url;
        if (StringUtils.isBlank(verifyCode) || StringUtils.isBlank(code) || !code.equalsIgnoreCase(verifyCode)) {

            message.put("codeMsg", "验证码错误!");
            url = "/site/forget";
            return UrlMessageEntity.getResponse(url, message);
        }

        message = userService.resetPassword(email, password);
        if (message.containsKey("user")) {
            url = "redirect:/login";
            return UrlMessageEntity.getResponse(url);
        } else {
            url = "/site/forget";
            return UrlMessageEntity.getResponse(url, message);
        }
    }



}
