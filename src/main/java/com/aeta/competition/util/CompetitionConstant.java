package com.aeta.competition.util;

public interface CompetitionConstant {
    //默认状态的登录凭证的超时时间
    int DEFAULT_EXPIRED_SECONDS=3600*12;
    //记住状态下的登录凭证超时时间
    int REMEMBER_EXPIRED_SECONDS=3600*24*100;

    //数据文件位置
    String DATA_PATH = "D:/test/";
    //打包了的数据文件位置
    String DATA_ZIP_PATH = "D:/test/zip/";

}
