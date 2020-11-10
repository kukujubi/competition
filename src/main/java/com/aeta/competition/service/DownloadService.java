package com.aeta.competition.service;

import com.aeta.competition.controller.LoginController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class DownloadService {
    private static final Logger logger = LoggerFactory.getLogger(DownloadService.class);
    /**
     * 递归查找文件
     * @param baseDirName  查找的文件夹路径
     * @param targetFileName  需要查找的文件名
     */
    public File findFiles(String baseDirName, String targetFileName) {

        File file = null;
        File baseDir = new File(baseDirName);       // 创建一个File对象
        if (!baseDir.exists() || !baseDir.isDirectory()) {  // 判断目录是否存在
            logger.info("文件查找失败：" + baseDirName + "不是一个目录！");
        }
        String tempName = null;
        //判断目录是否存在
        File tempFile;
        File[] files = baseDir.listFiles();
        for (int i = 0; i < files.length; i++) {
            tempFile = files[i];
            if(tempFile.isDirectory()){
                file = findFiles(tempFile.getAbsolutePath(), targetFileName);
                if (file != null) {
                    return  file;
                }
            }else if(tempFile.isFile()){
                tempName = tempFile.getName();
                if(tempName.equals(targetFileName)){
                    return tempFile.getAbsoluteFile();
                }
            }
        }
        return file;
    }

    /**
     * 得到文件夹下所有以.csv结尾的单天文件
     * @param directory
     * @return
     */
    public List<String> listFiles(String directory) {

        List<String> textFiles = new ArrayList<String>();

        File dir = new File(directory);

        for (File file : dir.listFiles()) {

            if (file.getName().toLowerCase().endsWith((".csv"))) {

                textFiles.add(file.getName());

            }

        }

        return textFiles;

    }

}
