package com.sparta.levelup_backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.levelup_backend.config.annotaion.FormToJson;
import java.io.IOException;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.MultiValueMap;

public class FormToJsonHttpMessageConverter<T> extends AbstractHttpMessageConverter<T> {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final FormHttpMessageConverter converter = new FormHttpMessageConverter();

    @Override
    protected boolean supports(Class<?> clazz) {

        return clazz.isAnnotationPresent(FormToJson.class);
    }

    @Override
    protected T readInternal(Class<? extends T> clazz, HttpInputMessage inputMessage)
        throws IOException, HttpMessageNotReadableException {
        MultiValueMap<String, String> read = converter.read(null, inputMessage);

        return objectMapper.convertValue(read.asSingleValueMap(), clazz);
    }

    @Override
    protected void writeInternal(T t, HttpOutputMessage outputMessage)
        throws IOException, HttpMessageNotWritableException {
    }

}
