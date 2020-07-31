package com.phudnguyen.bot.skypebot.service.impl;

import com.phudnguyen.bot.skypebot.dto.xml.FileSizeTag;
import com.phudnguyen.bot.skypebot.dto.xml.LoginTag;
import com.phudnguyen.bot.skypebot.dto.xml.Meta;
import com.phudnguyen.bot.skypebot.dto.xml.OriginalName;
import com.phudnguyen.bot.skypebot.dto.xml.URIObject;
import com.phudnguyen.bot.skypebot.service.XmlHelper;
import org.apache.commons.lang.RandomStringUtils;
import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;
import org.springframework.stereotype.Service;

import java.io.StringWriter;

@Service
public class XmlHelperImpl implements XmlHelper {
    @Override
    public String getContentMessage(String objectId, String filename, long length) {
        Persister serializer = new Persister(new AnnotationStrategy());
        URIObject o = URIObject.builder()
                .uri("https://api.asm.skype.com/v1/objects/" + objectId)
                .urlThumbnail("https://api.asm.skype.com/v1/objects/" + objectId + "/views/imgt1_anim")
                .type("Picture.1")
                .docId(objectId)
                .content("To view this shared photo, go to: ")
                .loginTag(LoginTag.builder()
                        .href("https://login.skype.com/login/sso?go=xmmfallback?pic=" + objectId)
                        .content("https://login.skype.com/login/sso?go=xmmfallback?pic=" + objectId)
                        .build())
                .originalName(OriginalName.builder()
                        .name(filename)
                        .build())
                .fileSize(FileSizeTag.builder()
                        .size(length)
                        .build())
                .meta(Meta.builder()
                        .type("photo")
                        .originalName(filename)
                        .build())
                .build();

        StringWriter wr = new StringWriter();

        try {
            serializer.write(o, wr);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        String s = wr.getBuffer().toString();
        s = s.replaceAll("\n", "");
        s = s.replaceAll(" {3}", "");
        return s;
    }

    @Override
    public String randomMessageId() {
        return "1" + RandomStringUtils.randomNumeric(18);
    }
}
