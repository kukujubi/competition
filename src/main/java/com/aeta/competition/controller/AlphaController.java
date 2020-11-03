package com.aeta.competition.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Controller
@RequestMapping("/alpha")
public class AlphaController {

    @RequestMapping("/hello")
    @ResponseBody
    public String sayHello(){
        return "hello";
    }

    //直接对底层对象  很麻烦
    @RequestMapping("/http")
    public void http(HttpServletRequest request, HttpServletResponse response){
        System.out.println(request.getMethod());

        response.setContentType("text/html;charset=utf-8");
        try(PrintWriter writer = response.getWriter();) {

            writer.write("<h1>yao</h1>");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
