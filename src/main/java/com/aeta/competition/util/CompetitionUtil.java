package com.aeta.competition.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CompetitionUtil {
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

//    public static void main(String[] args){
//        Map<String,Object> map=new HashMap<>();
//        map.put("name","zhang");
//        map.put("age",25);
//        System.out.println(getJSONString(0,"ok",map));
//    }
}
