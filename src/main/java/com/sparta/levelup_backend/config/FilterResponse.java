package com.sparta.levelup_backend.config;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class FilterResponse {

    public void responseErrorMsg(HttpServletResponse response,int statusCode,String code, String msg){
        try {
            response.setStatus(statusCode);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");

            response.getWriter().write("{\n"
                + "    \"errorCode\": \""+code+"\",\n"
                + "    \"detail\": \""+msg+"\",\n"
                + "    \"errorMessage\": \""+msg+"\"\n"
                + "}");
            response.getWriter().flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void responseSuccessMsg(HttpServletResponse response,int statusCode,String msg, String data){
        try {
            response.setStatus(statusCode);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");

            response.getWriter().write("{\n"
                + "    \"success\": \"OK\",\n"
                + "    \"message\": \""+msg+"\",\n"
                + "    \"data\": \""+data+"\"\n"
                + "}");
            response.getWriter().flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
