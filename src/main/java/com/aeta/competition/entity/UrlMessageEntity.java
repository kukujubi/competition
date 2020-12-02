package com.aeta.competition.entity;

import java.util.Map;

public class UrlMessageEntity {
    private String url;
    // codeRes = "success"; codeRes = "failure"; 两种情况
    private String codeRes;
    private Map<String,Object> message;

    public String getCodeRes() {
        return codeRes;
    }

    public void setCodeRes(String codeRes) {
        this.codeRes = codeRes;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, Object> getMessage() {
        return message;
    }

    public void setMessage(Map<String, Object> message) {
        this.message = message;
    }

    public static UrlMessageEntity getResponse(String url,String codeRes){
        return getResponse(url,codeRes,null);
    }


    public static UrlMessageEntity getResponse(String codeRes,Map<String,Object> message){
        return getResponse(null,codeRes,message);
    }

    public static UrlMessageEntity getResponse(String codeRes){
        return getResponse(null,codeRes,null);
    }
    public static UrlMessageEntity getResponse(String url,String codeRes, Map<String,Object> message) {
        UrlMessageEntity urlMessageEntity = new UrlMessageEntity();
        if (url!=null){
             urlMessageEntity.setUrl(url);
        }
        if (codeRes!=null){
            urlMessageEntity.setCodeRes(codeRes);
        }
        if (message != null) {
           urlMessageEntity.setMessage(message);
        }

        return urlMessageEntity;
    }
}
