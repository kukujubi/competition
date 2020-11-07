package com.aeta.competition.service;

import com.aeta.competition.dao.LoginTicketMapper;
import com.aeta.competition.dao.UserMapper;
import com.aeta.competition.entity.LoginTicket;
import com.aeta.competition.entity.User;
import com.aeta.competition.util.CompetitionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

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
        user.setCreateTime(new Date());
        userMapper.insertUser(user);//插入库里

        return map;
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

}
