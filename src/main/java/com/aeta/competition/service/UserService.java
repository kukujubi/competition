package com.aeta.competition.service;

import com.aeta.competition.dao.LoginTicketMapper;
import com.aeta.competition.dao.UserMapper;
import com.aeta.competition.entity.LoginTicket;
import com.aeta.competition.entity.User;
import com.aeta.competition.util.CompetitionUtil;
import com.aeta.competition.util.MailClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.apache.commons.lang3.StringUtils;

import java.lang.module.Configuration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.aeta.competition.util.CompetitionConstant.*;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Value("${competition.path.domain}")
    private String domain;
    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    /**
     * 用户注册
     * @param user
     * @return 返回封装多个内容的错误
     */
    public Map<String,Object> register(User user){
        Map<String,Object> map = new HashMap<>();
        //先对空值做一个判断处理
        if(user==null){
            throw new IllegalArgumentException("参数不能为空");
        }

        if(StringUtils.isBlank(user.getUsername()))
        {
            map.put("usernameMsg","账号不能为空!");
            return map;
        }
        if(StringUtils.isBlank(user.getPassword())){
            map.put("passwordMsg","密码不能为空!");
            return map;
        }
        if(StringUtils.isBlank(user.getEmail())){
            map.put("emailMsg","邮箱不能为空!");
            return map;
        }
        //要验证这些信息是否已存在
        User u = userMapper.selectByName(user.getUsername());
        if(u!=null)
        {
            map.put("usernameMsg","该账号已存在");
            return map;

        }
        u = userMapper.selectByEmail(user.getEmail());
        if(u!=null)
        {
            map.put("emailMsg","该邮箱已存在");
            return map;

        }
        //这时候可以注册用户了 把这些信息存到库里 先密码加密
        user.setSalt(CompetitionUtil.generateUUID().substring(0,5));
        user.setPassword(CompetitionUtil.md5(user.getPassword()+user.getSalt()));
        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(CompetitionUtil.generateUUID());//激活码
        user.setCreateTime(new Date());
        userMapper.insertUser(user);//插入库里

        //激活邮件
        Context context = new Context();
        context.setVariable("email",user.getEmail());
        //http://localhost:8080/community/activation/101/code
        String url = domain+contextPath+"/activation/"+user.getId()+"/"+user.getActivationCode();
        context.setVariable("url",url);
        String content = templateEngine.process("/mail/activation",context);
        mailClient.sendMail(user.getEmail(),"激活账号",content);

        return map;
    }

    /**
     * 邮件激活
     * @param userId
     * @param code
     * @return
     */
    public int activation(int userId,String code) {
        User user = userMapper.selectById(userId);
        if (user.getStatus() == 1) {
            return ACTIVATION_REPEAT;
        } else if (user.getActivationCode().equals(code)) {
            userMapper.updateStatus(userId, 1);
            return ACTIVATION_SUCCESS;
        } else {
            return ACTIVATION_FAILURE;
        }
    }

    /**
     * 用户登录
     * @param username
     * @param password
     * @param expiredSeconds
     * @return 错误信息或者登录凭证
     */
    public Map<String,Object> login(String username,String password,long expiredSeconds){
        Map<String,Object> map =new HashMap<>();
        //空值处理
        if(StringUtils.isBlank(username)) {
            map.put("usernameMsg", "账号不能为空");
            return map;
        }
        if(StringUtils.isBlank(password)) {
            map.put("passwordMsg", "密码不能为空");
            return map;
        }
        //验证账号
        User user=userMapper.selectByName(username);
        if(user==null)
        {
            map.put("usernameMsg", "该账号不存在");
            return map;
        }
        if(user.getStatus()==0){//没有激活
            map.put("usernameMsg", "该账号未激活");
            return map;
        }

        //验证密码 要先加密 再比较
        password =CompetitionUtil.md5(password+user.getSalt());
        if(!user.getPassword().equals(password)){
            map.put("passwordMsg", "密码不正确");
            return map;
        }
        //生成登录凭证
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(CompetitionUtil.generateUUID());
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis()+expiredSeconds*1000));
        loginTicketMapper.insertLoginTicket(loginTicket);

        map.put("ticket",loginTicket.getTicket());
        return map;
    }

    /**
     * 用户退出登录
     * @param ticket
     */
    public void logout(String ticket){
        loginTicketMapper.updateStatus(ticket,1);
    }




    /**
     * 查询登录凭证
     * @param ticket
     * @return
     */
    public LoginTicket findLoginTicket(String ticket){
        return loginTicketMapper.selectByTicket(ticket);

    }

    /**
     * 根据userId找User
     */
    public User findUserById(int id){
        return userMapper.selectById(id);
    }


    /**
     * 重置密码
     * @param email
     * @param password
     * @return
     */
    public Map<String,Object> resetPassword(String email,String password){
        Map<String,Object> map = new HashMap<>();
        //空值处理
        if (StringUtils.isBlank(email)){
            map.put("emailMsg","邮箱不能为空");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("passwordMsg","密码不能为空");
            return map;
        }
        //验证邮箱
        User user = userMapper.selectByEmail(email);
        if(user==null)
        {
            map.put("emailMsg","邮箱尚未注册");
            return map;
        }
        //重置密码
        password=CompetitionUtil.md5(password+user.getSalt());
        userMapper.updatePassword(user.getId(),password);
        map.put("user",user);
        return map;


    }

    /**
     * 修改密码（暂时不用）
     * @param userId
     * @param oldPassword
     * @param newPassword
     * @return
     */
    public Map<String,Object> updatePassword(int userId,String oldPassword,String newPassword){
        Map<String, Object> map = new HashMap<>();

        // 空值处理
        if (StringUtils.isBlank(oldPassword)) {
            map.put("oldPasswordMsg", "原密码不能为空!");
            return map;
        }
        if (StringUtils.isBlank(newPassword)) {
            map.put("newPasswordMsg", "新密码不能为空!");
            return map;
        }
        //验证原始密码
        User user=userMapper.selectById(userId);
        oldPassword = CompetitionUtil.md5(oldPassword + user.getSalt());
        if (!user.getPassword().equals(oldPassword)) {
            map.put("oldPasswordMsg", "原密码输入有误!");
            return map;
        }
        // 更新密码
        newPassword = CompetitionUtil.md5(newPassword + user.getSalt());
        userMapper.updatePassword(userId, newPassword);

        return map;
    }

}
