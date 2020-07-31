package com.phudnguyen.bot.skypebot.dto.xml;

import lombok.Builder;
import lombok.Data;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

@Data
@Builder
@Root(name = "a")
public class LoginTag {
    @Attribute(name = "href")
    String href;
    @Text
    String content;
}
