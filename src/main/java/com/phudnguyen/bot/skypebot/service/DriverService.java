package com.phudnguyen.bot.skypebot.service;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface DriverService {
    void login() throws IOException;

    void sendImage(String groupName, String image);

    void openSkypeWeb();

    void selectConversation(String groupName);

    void uploadImage(String path);

    void closeBrowser();
}
