package com.phudnguyen.bot.skypebot.service.impl;

import com.phudnguyen.bot.skypebot.dto.Conversation;
import com.phudnguyen.bot.skypebot.dto.ConversationsResponse;
import com.phudnguyen.bot.skypebot.dto.CreateObjectRequest;
import com.phudnguyen.bot.skypebot.dto.PostMessageRequest;
import com.phudnguyen.bot.skypebot.dto.UploadObjectResponse;
import com.phudnguyen.bot.skypebot.feign.SkypeDataClient;
import com.phudnguyen.bot.skypebot.feign.SkypeMediaClient;
import com.phudnguyen.bot.skypebot.service.SkypeService;
import com.phudnguyen.bot.skypebot.service.XmlHelper;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class SkypeServiceImpl implements SkypeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SkypeServiceImpl.class);

    private final SkypeMediaClient skypeMediaClient;
    private final SkypeDataClient skypeDataClient;
    private final XmlHelper xmlHelper;

    @Autowired
    public SkypeServiceImpl(SkypeMediaClient skypeMediaClient, SkypeDataClient skypeDataClient, XmlHelper xmlHelper) {
        this.skypeMediaClient = skypeMediaClient;
        this.skypeDataClient = skypeDataClient;
        this.xmlHelper = xmlHelper;
    }

    @Override
    public void uploadLocalImage(String target, String path) throws IOException {
        LOGGER.info("Upload local image {} to thread {}", path, target);
        String name = FilenameUtils.getName(path);
        File f = new File(path);
        String objectId = uploadFile(target, name, IOUtils.toByteArray(new FileInputStream(path)));
        postMessage(target, name, f.length(), objectId);
        LOGGER.info("Sent message successfully");
    }

    private void postMessage(String target, String fileName, long fileSize, String objectId) {
        DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .withZone(ZoneId.of("UTC"));
        String formattedDate = formatter.format(new Date().toInstant());
        LOGGER.info("Sending message...");
        skypeDataClient.postMessage(target,
                PostMessageRequest.builder()
                        .clientmessageid(xmlHelper.randomMessageId())
                        .composetime(formattedDate)
                        .content(xmlHelper.getContentMessage(objectId, fileName, fileSize))
                        .amsreferences(Collections.singletonList(objectId))
                        .build());
    }

    private String uploadFile(String target, String fileName, byte[] data) {
        String transactionId = UUID.randomUUID().toString();
        Map<String, List<String>> permissions = new HashMap<>();
        permissions.put(target, Collections.singletonList("read"));
        UploadObjectResponse uploadResponse = skypeMediaClient.createObject(CreateObjectRequest.builder()
                        .filename(fileName)
                        .type("pish/image")
                        .permissions(permissions)
                        .build(),
                transactionId);
        LOGGER.info("Upload id {}", uploadResponse.getId());
        skypeMediaClient.uploadObject(uploadResponse.getId(),
                transactionId,
                data
        );
        LOGGER.info("Uploaded successfully");
        return uploadResponse.getId();
    }

    @Override
    public List<Conversation> getConversations(int limit) {
        ConversationsResponse resp = skypeDataClient.getConversation(limit);
        return resp.getConversations();
    }

    @Override
    public void uploadLocalDir(String target, String path) throws IOException {
        try (Stream<Path> walk = Files.walk(Paths.get(path))) {
            List<String> result = walk.filter(Files::isRegularFile)
                    .map(Path::toString)
                    .filter(s -> StringUtils.endsWith(s, "jpeg")
                            || StringUtils.endsWith(s, "jpg")
                            || StringUtils.endsWith(s, "png"))
                    .collect(Collectors.toList());
            for (String s : result) {
                try {
                    this.uploadLocalImage(target, s);
                } catch (IOException e) {
                    LOGGER.error("Fail to upload {}", s, e);
                }
            }
        }
    }

    @Override
    public void uploadUrlFile(String target) throws IOException {
        IOUtils.readLines(new FileInputStream("urls.txt"), Charset.forName("UTF-8")).forEach(url -> {
            try {
                LOGGER.info("Processing {}", url);
                String filename = FilenameUtils.getName(new URL(url).getFile());
                HttpClient client = HttpClientBuilder.create().build();
                HttpGet request = new HttpGet(url);
                HttpResponse response = client.execute(request);
                byte[] rawData = IOUtils.toByteArray(response.getEntity().getContent());
                if (rawData.length == 0) {
                    LOGGER.error("Fail to get image data with status {}", response.getStatusLine());
                    return;
                }
                if (rawData.length == 503) {
                    LOGGER.error("Ignore missing image {}", response.getStatusLine());
                    return;
                }
                LOGGER.info("Buffer size {}", rawData.length);
                String objectId = uploadFile(target, filename, rawData);
                while (true) {
                    try {
                        postMessage(target, filename, rawData.length, objectId);
                        Thread.sleep(5 * 1000);
                        break;
                    } catch (Exception e) {
                        LOGGER.info("Fail to post message by error {}. Waiting for timeout exceeds.", e.getMessage());
                        Thread.sleep(30 * 1000);
                    }
                }

                LOGGER.info("Submitted successfully {}", url);
            } catch (IOException | InterruptedException e) {
                LOGGER.error("Invalid URL {}", url);
            }
        });
    }
}

