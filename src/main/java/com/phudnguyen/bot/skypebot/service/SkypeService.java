package com.phudnguyen.bot.skypebot.service;

import com.phudnguyen.bot.skypebot.dto.Conversation;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface SkypeService {
    void uploadLocalImage(String target, String path) throws IOException;

    List<Conversation> getConversations(int limit);

    void uploadLocalDir(String target, String path) throws IOException;

    void uploadUrlFile(String groupId) throws IOException;
}
