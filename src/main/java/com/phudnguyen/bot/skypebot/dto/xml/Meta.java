package com.phudnguyen.bot.skypebot.dto.xml;

import lombok.Builder;
import lombok.Data;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Text;

@Data
@Builder
public class Meta {
    @Attribute(name = "type")
    String type;
    @Attribute(name = "originalName")
    String originalName;

    @Text
    @Builder.Default
    String content = "";

}
