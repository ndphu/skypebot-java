package com.phudnguyen.bot.skypebot.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class UploadLocalImageRequest {
    private String path;
    private String groupId;
    private String groupName;
}
