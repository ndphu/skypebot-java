package com.phudnguyen.bot.skypebot.controller;

import com.phudnguyen.bot.skypebot.dto.Conversation;
import com.phudnguyen.bot.skypebot.dto.UploadLocalDirRequest;
import com.phudnguyen.bot.skypebot.dto.UploadLocalImageRequest;
import com.phudnguyen.bot.skypebot.service.AuthService;
import com.phudnguyen.bot.skypebot.service.DriverService;
import com.phudnguyen.bot.skypebot.service.SkypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/skype")
public class SkypeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SkypeController.class);

    private final DriverService driverService;

    private final AuthService authService;

    private final SkypeService skypeService;

    @Autowired
    public SkypeController(DriverService driverService, AuthService authService, SkypeService skypeService) {
        this.driverService = driverService;
        this.authService = authService;
        this.skypeService = skypeService;
    }

    @PostMapping("/uploadLocalImage")
    public ResponseEntity uploadImage(@RequestBody UploadLocalImageRequest request) throws IOException {
        LOGGER.info("Upload local image {}", request);
        skypeService.uploadLocalImage(request.getGroupId(), request.getPath());
        return ResponseEntity.ok("");
    }

    @PostMapping("/uploadLocalDirectory")
    public ResponseEntity uploadLocalDir(@RequestBody UploadLocalDirRequest request) throws IOException {
        LOGGER.info("Upload local directory {}", request);
        skypeService.uploadLocalDir(request.getGroupId(), request.getPath());
        return ResponseEntity.ok("");
    }



    @PostMapping("/uploadUrlFile")
    public ResponseEntity uploadUrlFile(@RequestBody UploadLocalDirRequest request) throws IOException {
        skypeService.uploadUrlFile(request.getGroupId());
        return ResponseEntity.ok("");
    }

    @GetMapping("/conversations")
    public ResponseEntity getConversations() {
        List<Conversation> conversations = skypeService.getConversations(50);
        LOGGER.info("Found {} conversations", conversations.size());
        return ResponseEntity.ok(conversations);
    }



    @GetMapping("/login")
    public ResponseEntity login() throws IOException {
        driverService.login();
        authService.refreshToken();
        return ResponseEntity.ok(authService.getSkypeToken());
    }
}
