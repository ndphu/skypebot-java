package com.phudnguyen.bot.skypebot.service.impl;

import com.google.gson.Gson;
import com.phudnguyen.bot.skypebot.service.AuthService;
import com.phudnguyen.bot.skypebot.service.DriverService;
import org.openqa.selenium.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Objects;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired private DriverService driverService;

    @Autowired private Gson gson;

    private String skypeToken;

    @PostConstruct
    public void postConstruct() throws FileNotFoundException {
        refreshToken();
    }

    @Override
    public String refreshToken() throws FileNotFoundException {
        Cookie cookie = Arrays.stream(gson.fromJson(new FileReader("cookies.json"), Cookie[].class))
                .filter(c -> "skypeToken".equals(c.getName()))
                .findFirst().orElse(null);
        if (Objects.nonNull(cookie)) {
            this.skypeToken = cookie.getValue();
            return cookie.getValue();
        }

        return null;
    }

    @Override
    public String getSkypeToken() {
        return this.skypeToken;
    }
}
