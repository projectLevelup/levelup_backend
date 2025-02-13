package com.sparta.levelup_backend.config;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class FilterResponse {

    public void responseMsg(HttpServletResponse response,int statusCode,String msg){
        try {
            response.setStatus(statusCode);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");

            response.getWriter().write("{\n"
                + "    \"success\": false,\n"
                + "    \"message\": \""+msg+"\"\n"
                + "    \"data\": null\n"
                + "}");
            response.getWriter().flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void responseMsg(HttpServletResponse response,int statusCode,String msg, String data){
        try {
            response.setStatus(statusCode);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");

            response.getWriter().write("{\n"
                + "    \"success\": true,\n"
                + "    \"message\": \""+msg+"\",\n"
                + "    \"data\": \""+data+"\"\n"
                + "}");
            response.getWriter().flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
