package com.phudnguyen.bot.skypebot.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PostMessageRequest {
    String clientmessageid;
    String composetime;
    String content;
    @Builder.Default
    String messagetype = "RichText/UriObject";
    @Builder.Default
    String contenttype = "text";
    @Builder.Default
    String imdisplayname = "/dev/null";
    List<String> amsreferences;
}
