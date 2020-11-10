package com.aeta.competition.controller;



import com.aeta.competition.service.DownloadService;
import com.aeta.competition.util.CompetitionConstant;
import com.aeta.competition.util.CompetitionUtil;
import org.apache.commons.io.FileUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Controller
public class DownloadController implements CompetitionConstant {

    @Autowired
    private DownloadService downloadService;

    /**
     * 下载某一天的数据，选择时间，下载下来就是一个文件
     * pathname 是存放文件的位置
     * 单天的文件不进行压缩
     * 找不到返回bad request对吗？
     *
     */
    @RequestMapping(path ="/downloadDay")
    public ResponseEntity<byte[]> downloadDay(HttpServletRequest request, String day) throws IOException {
        //String day = "20201108";
        String resourceName = day+".csv";
        File file = new File(CompetitionConstant.DATA_PATH + resourceName);
        if (!file.exists())
            //？？？？？？？
            return new ResponseEntity<byte[]>(HttpStatus. BAD_REQUEST);
        HttpHeaders headers = new HttpHeaders();
        String filename = new String(resourceName.getBytes("utf-8"), "iso-8859-1");
        headers.setContentDispositionFormData("attachment", filename);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.OK);
    }

    /**
     * 下载多天的 就把所有的文件压缩成一个zip 下载这个zip
     * 先从存放的位置找有没有这个zip 如果已经存在则不再进行压缩 直接下载
     * @param request
     * @return
     * @throws IOException
     */

    @RequestMapping("/downloadAll")
    public ResponseEntity<byte[]> downloadAll(HttpServletRequest request) throws IOException {
        //得到昨天的日期
        Date yesterday = CompetitionUtil.getYesterday(new Date());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String dateString = formatter.format(yesterday);
        //压缩后的文件名
        String resourcesName = dateString + ".zip";
        //看zip文件夹里有没有已经打包好了的文件，有的话直接返回
        File zipFile = downloadService.findFiles(CompetitionConstant.DATA_ZIP_PATH, resourcesName);
        if (zipFile != null) {
            HttpHeaders headers = new HttpHeaders();
            String filename = new String(resourcesName.getBytes("utf-8"), "iso-8859-1");
            headers.setContentDispositionFormData("attachment", filename);
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(zipFile), headers, HttpStatus.OK);
        } else {

            //需要压缩的文件
            List<String> list = downloadService.listFiles(CompetitionConstant.DATA_PATH);

            ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(CompetitionConstant.DATA_ZIP_PATH+ resourcesName));
            InputStream input = null;

            for (String str : list) {
                String name = CompetitionConstant.DATA_PATH+ str;
                input = new FileInputStream(new File(name));
                zipOut.putNextEntry(new ZipEntry(str));
                int temp = 0;
                while ((temp = input.read()) != -1) {
                    zipOut.write(temp);
                }
                input.close();
            }
            zipOut.close();
            File file = new File(CompetitionConstant.DATA_ZIP_PATH + resourcesName);
            HttpHeaders headers = new HttpHeaders();
            String filename = new String(resourcesName.getBytes("utf-8"), "iso-8859-1");
            headers.setContentDispositionFormData("attachment", filename);
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.OK);
        }
    }

}
