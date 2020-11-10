package com.aeta.competition.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.text.SimpleDateFormat;
import java.util.*;

public class CompetitionUtil {

    private final static SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");

    private final static SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //生成随机字符串 把UUID里的-弄成空的 只要字母和数字
    public static String generateUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }
    //MD5加密（存的密码要加密）
    //MD5只能加密 不能解密
    //password+一个随机字符串（salt）再加密
    public static String md5(String key){
        if(StringUtils.isBlank(key)){//只是一个空格 null 空字符串也会是空的
            return null;
        }
        else{
            return DigestUtils.md5DigestAsHex(key.getBytes());
        }
    }

    public static String getJSONString(int code, String msg, Map<String,Object> map){
        JSONObject json = new JSONObject();
        json.put("code",code);
        json.put("msg",msg);
        if(map!=null){
            for(String key:map.keySet()){
                json.put(key,map.get(key));
            }
        }
        return json.toJSONString();

    }

    public static String getJSONString(int code, String msg){
        return getJSONString(code,msg,null);

    }
    public static String getJSONString(int code){
        return getJSONString(code,null,null);

    }

    public static Date getYesterday(Date today) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);
        return calendar.getTime();
    }



    /**
     * 获得本周的第一天，周一
     *
     * @return
     */
    public static Date getCurrentWeekDayStartTime() {
        Calendar c = Calendar.getInstance();
        try {
            int weekday = c.get(Calendar.DAY_OF_WEEK) - 2;
            c.add(Calendar.DATE, -weekday);
            c.setTime(longSdf.parse(shortSdf.format(c.getTime()) + " 00:00:00"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c.getTime();
    }

    /**
     * 获得本周的最后一天，周日
     *
     * @return
     */
    public static Date getCurrentWeekDayEndTime() {
        Calendar c = Calendar.getInstance();
        try {
            int weekday = c.get(Calendar.DAY_OF_WEEK);
            c.add(Calendar.DATE, 8 - weekday);
            c.setTime(longSdf.parse(shortSdf.format(c.getTime()) + " 23:59:59"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c.getTime();
    }

    public static void main(String[] args) {

        System.out.println("当前周开始："+getCurrentWeekDayStartTime().toString());
        System.out.println("当前周结束："+getCurrentWeekDayEndTime().toString());

    }
//    public static void main(String[] args){
//        Map<String,Object> map=new HashMap<>();
//        map.put("name","zhang");
//        map.put("age",25);
//        System.out.println(getJSONString(0,"ok",map));
//    }
}
