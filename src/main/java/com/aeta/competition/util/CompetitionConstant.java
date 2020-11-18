package com.aeta.competition.util;

public interface CompetitionConstant {
    //默认状态的登录凭证的超时时间
    int DEFAULT_EXPIRED_SECONDS=3600*12;
    //记住状态下的登录凭证超时时间
    int REMEMBER_EXPIRED_SECONDS=3600*24*100;

    //数据文件位置
    String DATA_PATH = "D:/test/";
    //数据单天打包后的位置
    String DATA_SINGLE_PATH = "D:/test/single/";
    //打包了总的数据文件位置
    String DATA_ALL_PATH = "D:/test/all/";


    //激活成功
    int ACTIVATION_SUCCESS = 0;
    //重复激活
    int ACTIVATION_REPEAT = 1;
    //激活失败
    int ACTIVATION_FAILURE = 2;


    //实体类型：直接提问
    int ENTITY_TYPE_COMMENT = 1;
    //实体类型：提问下的回复
    int ENTITY_TYPE_REPLY = 2;

}
