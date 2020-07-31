package com.phudnguyen.bot.skypebot.dto;

import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class CreateObjectRequest {
    @Builder.Default
    private String type = "pish/image";
    private String filename;
    @Builder.Default
    private Map<String, List<String>> permissions = new HashMap<>();
}
