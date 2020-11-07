package com.aeta.competition.entity;



import java.util.Map;

public class UrlMessageEntity {
    private String url;
    private Map<String,Object> message;

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

    public static UrlMessageEntity getResponse(String url){
        return getResponse(url,null);
    }

    public static UrlMessageEntity getResponse(String url, Map<String,Object> message) {
        UrlMessageEntity urlMessageEntity = new UrlMessageEntity();
        urlMessageEntity.setUrl(url);
        if (message != null) {
           urlMessageEntity.setMessage(message);
        }
        return urlMessageEntity;
    }
}
