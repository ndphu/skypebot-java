package com.phudnguyen.bot.skypebot.dto.xml;

import lombok.Builder;
import lombok.Data;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.Convert;
import org.simpleframework.xml.convert.Converter;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;

import java.lang.reflect.Field;
import java.util.Objects;

@Root(name = "URIObject")
@Convert(value = URIObject.URIObjectConverter.class)
@Data
@Builder
public class URIObject {
    @Attribute(name = "uri")
    String uri;
    @Attribute(name = "url_thumbnail")
    String urlThumbnail;
    @Attribute(name = "type")
    String type;
    @Attribute(name = "doc_id")
    String docId;
    @Attribute(name = "width", required = false)
    int width;
    @Attribute(name = "height", required = false)
    int height;


    String content;

    @Element(name = "a")
    LoginTag loginTag;

    @Element(name = "OriginalName")
    OriginalName originalName;

    @Element(name = "FileSize")
    FileSizeTag fileSize;

    @Element(name = "meta")
    Meta meta;


    static class URIObjectConverter implements Converter<URIObject> {
        private final Serializer ser = new Persister();

        @Override
        public URIObject read(InputNode node) throws Exception {
            return null;
        }

        @Override
        public void write(OutputNode node, URIObject value) throws Exception {
            node.setValue(value.content + "\n");
            Field[] fields = URIObject.class.getDeclaredFields();
            for (Field field : fields) {
                if (Objects.nonNull(field.getAnnotation(Attribute.class))) {
                    node.setAttribute(field.getAnnotation(Attribute.class).name(), String.valueOf(field.get(value)));
                } else if (Objects.nonNull(field.getAnnotation(Element.class))) {
                    ser.write(field.get(value), node);
                }
            }
        }
    }
}
