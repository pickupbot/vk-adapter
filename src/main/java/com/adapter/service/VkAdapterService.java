package com.adapter.service;


import com.adapter.model.DatingProfile;
import com.adapter.model.Reply;
import com.adapter.repository.VkAdapterRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



@Service
@RequiredArgsConstructor
public class VkAdapterService {

    private final VkAdapterRepository repository;
    public List<DatingProfile> getProfile(String boundary,
                                          String token,
                                          String agent,
                                          String session,
                                          String version) {

        Map<String, String> data = new HashMap<>();

        data.put("count", "1");
        data.put("_token", token);
        data.put("_agent", agent);
        data.put("_session", session);
        data.put("_v", version);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://dating.vk.com/api/dating.getRecommendedUsersSimple"))
                .header("Content-Type",
                        "multipart/form-data; boundary=" + boundary)
                .POST(oMultipartData(data, boundary))
                .build();

        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.body());

            List<JsonNode> jsonProfiles = new ArrayList<>();
            root.get("users").forEach(jsonProfiles::add);

            List<DatingProfile> profileEntities = new ArrayList<>();

            try {
                DatingProfile profile = new ObjectMapper().readValue(jsonProfiles.get(0).toString(), DatingProfile.class);
                profileEntities.add(profile);

                if (!repository.isDatingProfileExist(profile.getProfileId())) {

                    repository.save(profile);
                }
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            return profileEntities;

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    public String replyProfile(String boundary,
                               String user_id,
                               String meta,
                               String token,
                               String agent,
                               String session,
                               String version,
                               Reply reply) {
        Map<String, String> data = new HashMap<>();

        data.put("user_id", user_id);
        data.put("meta", meta);
        data.put("count", "1");
        data.put("_token", token);
        data.put("_agent", agent);
        data.put("_session", session);
        data.put("_v", version);

        String uri = reply == Reply.LIKE ? "https://dating.vk.com/api/dating.like" : "https://dating.vk.com/api/dating.dislike";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .header("Content-Type",
                        "multipart/form-data; boundary=" + boundary)
                .POST(oMultipartData(data, boundary))
                .build();

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static HttpRequest.BodyPublisher oMultipartData(Map<String, String> data,
                                                           String boundary)  {
        var byteArrays = new ArrayList<byte[]>();
        byte[] separator = ("--" + boundary
                + "\r\nContent-Disposition: form-data; name=")
                .getBytes(StandardCharsets.UTF_8);
        for (Map.Entry<String, String> entry : data.entrySet()) {
            byteArrays.add(separator);
            byteArrays.add(
                    ("\"" + entry.getKey() + "\"\r\n\r\n" + entry.getValue()
                            + "\r\n").getBytes(StandardCharsets.UTF_8));
        }
        byteArrays
                .add(("--" + boundary + "--").getBytes(StandardCharsets.UTF_8));
        return BodyPublishers.ofByteArrays(byteArrays);
    }
}
