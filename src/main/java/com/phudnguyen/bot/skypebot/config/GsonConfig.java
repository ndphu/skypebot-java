package com.phudnguyen.bot.skypebot.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.phudnguyen.bot.skypebot.dto.CreateObjectRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Type;

@Configuration
public class GsonConfig {
    @Bean
    public Gson gson() {
        return new GsonBuilder().disableHtmlEscaping()
                .registerTypeAdapter(CreateObjectRequest.class, new CreateObjectRequestJsonSerializer())
                .create();
    }

    public static class CreateObjectRequestJsonSerializer implements JsonSerializer<CreateObjectRequest> {

        @Override
        public JsonElement serialize(CreateObjectRequest request, Type type, JsonSerializationContext context) {
            JsonObject object = new JsonObject();
            object.add("permissions", context.serialize(request.getPermissions()));
            object.add("type", context.serialize(request.getType()));
            object.add("filename", context.serialize(request.getFilename()));
            return object;
        }

    }
}
