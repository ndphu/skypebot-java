package com.phudnguyen.bot.skypebot.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Conversation {
    private String id;
    private String type;
    private ThreadProperties threadProperties;
}
