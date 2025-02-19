package com.sparta.levelup_backend.config;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class FilterResponse {

    public void responseErrorMsg(HttpServletResponse response,int statusCode,String code, String msg){
        try {
            response.setStatus(statusCode);
            response.setContentType(APPLICATION_JSON_VALUE);
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

    public void responseSuccessMsg(HttpServletResponse response, HttpStatus statusCode,String msg, String data){
        try {
            response.setStatus(statusCode.value());
            response.setContentType(APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\n"
                + "    \"status\": \""+statusCode.name()+"\",\n"
                + "    \"message\": \""+msg+"\",\n"
                + "    \"data\": \""+data+"\"\n"
                + "}");
            response.getWriter().flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
