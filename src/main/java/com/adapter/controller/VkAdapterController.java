package com.adapter.controller;


import com.adapter.model.DatingProfile;
import com.adapter.model.Reply;
import com.adapter.service.VkAdapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/dating_profiles")
public class VkAdapterController {

    @Autowired
    private VkAdapterService vkAdapterService;

    @PostMapping("/get_profile")
    public DatingProfile getDatingProfile(
            @RequestParam("boundary") String boundary,
            @RequestParam("_token") String token,
            @RequestParam("_agent") String agent,
            @RequestParam("_session") String session,
            @RequestParam("_v") String version) {
        return vkAdapterService.getProfile(boundary, token, agent, session, version);
    }

    @PostMapping("/like_profile")
    public String likeProfile(@RequestParam("boundary") String boundary,
                                            @RequestParam("user_id") String user_id,
                                            @RequestParam("meta") String meta,
                                            @RequestParam("_token") String token,
                                            @RequestParam("_agent") String agent,
                                            @RequestParam("_session") String session,
                                            @RequestParam("_v") String version) {
        return vkAdapterService.replyProfile(boundary, user_id, meta, token, agent, session, version, Reply.LIKE);
    }

    @PostMapping("/dislike_profile")
    public String dislikeProfile(@RequestParam("boundary") String boundary,
                                               @RequestParam("user_id") String user_id,
                                               @RequestParam("meta") String meta,
                                               @RequestParam("_token") String token,
                                               @RequestParam("_agent") String agent,
                                               @RequestParam("_session") String session,
                                               @RequestParam("_v") String version) {
        return vkAdapterService.replyProfile(boundary, user_id, meta, token, agent, session, version, Reply.DISLIKE);
    }
}
