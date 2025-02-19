package com.sparta.levelup_backend.config;


import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;


import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class HttpMessageConverterConfigurer implements WebMvcConfigurer {

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FormToJsonHttpMessageConverter<?> converter = new FormToJsonHttpMessageConverter<>();
        MediaType media = new MediaType(APPLICATION_FORM_URLENCODED, UTF_8);
        converter.setSupportedMediaTypes(List.of(media));
        converters.add(converter);
    }

}
