package com.phudnguyen.bot.skypebot.feign;

import com.google.gson.Gson;
import com.phudnguyen.bot.skypebot.service.AuthService;
import feign.Logger;
import feign.RequestInterceptor;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.io.InputStream;
import java.nio.charset.Charset;

@Configuration
public class FeignClientConfiguration {

    @Autowired
    private Gson gson;

    @Autowired
    private AuthService authService;

    @Bean
    public RequestInterceptor addMapApiToken() {
        return template -> {
            Class<?> type = template.feignTarget().type();
            if (type == SkypeMediaClient.class) {
                template.header("Authorization", "skype_token " + authService.getSkypeToken());
                // TODO: move to config
                template.header("X-Client-Version", "1418/8.62.0.83//");
                template.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.89 Safari/537.36");
                template.header("Referer", "https://web.skype.com/");
                template.header("Origin", "https://web.skype.com");
            } else if (type == SkypeDataClient.class) {
                template.header("Authentication", "skypetoken=" + authService.getSkypeToken());
            }
        };
    }

    @Bean
    public Decoder defaultDecoder() {
        return new GsonDecoder(gson);
    }

    @Bean
    public Encoder defaultEncoder() {
        return new GsonEncoder(gson);
    }

    @Bean
    @Primary
    public Decoder feignDecoder() {
        return new ResponseEntityDecoder((response, type) ->
        {
            if (type == InputStream.class) {
                return response.body().asInputStream();
            } else if (type == String.class) {
                return IOUtils.toString(response.body().asReader(Charset.forName("UTF-8")));
            } else {
                return defaultDecoder().decode(response, type);
            }
        });
    }

    @Bean
    @Primary
    public Encoder feignEncoder() {
        return (object, bodyType, template) -> {
            if (bodyType == byte[].class) {
                template.body((byte[])object, Charset.forName("UTF-8"));
            } else {
                defaultEncoder().encode(object, bodyType, template);
            }
        };
    }

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
