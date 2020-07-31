package com.phudnguyen.bot.skypebot.dto;

import lombok.Data;

import java.util.List;

@Data
public class ConversationsResponse {
    private List<Conversation> conversations;
}
