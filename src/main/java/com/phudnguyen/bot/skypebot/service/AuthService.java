package com.phudnguyen.bot.skypebot.service;

import java.io.FileNotFoundException;

public interface AuthService {

    String refreshToken() throws FileNotFoundException;

    String getSkypeToken();
}
