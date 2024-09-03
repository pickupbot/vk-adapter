package com.adapter.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DatingProfileDeserializer extends JsonDeserializer<DatingProfile> {

    @Override
    public DatingProfile deserialize(JsonParser p, DeserializationContext ctxt) throws IOException{
        JsonNode jsonProfile = p.getCodec().readTree(p);

        List<String> photos = jsonProfile.get("stories").findValues("url")
                .stream()
                .map(JsonNode::asText)
                .toList();

        List<String> tags = new ArrayList<>();

        jsonProfile.get("form").findValues("interests").get(0).forEach(tag -> tags.add(tag.asText()));


        return DatingProfile.builder()
                .profileId(jsonProfile.get("id").asLong())
                .meta(jsonProfile.get("extra").get("meta").asText())
                .name(jsonProfile.get("name").asText())
                .age(jsonProfile.get("age").asInt())
                .description(jsonProfile.get("form").get("about").asText())
                .photos(photos.toArray(String[]::new))
                .tags(tags.toArray(String[]::new))
                .purpose(purposeDeserializer(jsonProfile.get("form").get("target")))
                .isVerified(jsonProfile.get("is_verify").asBoolean())
                .platform("vk")
                .build();
    }

    private String purposeDeserializer(JsonNode purpose){
        try {
            return switch (purpose.asText()) {
                case "free_date" -> "openRelationship";
                case "new_experience" -> "newExperience";
                case "serious_date" -> "love";
                case "conversation" -> "friendship";
                default -> throw new IllegalStateException();
            };
        } catch (NullPointerException | IllegalStateException exception) {
            return "notSpecified";
        }
    }
}
