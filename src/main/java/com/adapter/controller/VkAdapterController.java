package com.adapter.controller;


import com.adapter.model.DatingProfile;
import com.adapter.service.VkAdapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dating_profiles")
public class VkAdapterController {

    @Autowired
    private VkAdapterService vkAdapterService;

    @PostMapping("/get_profiles")
    public List<DatingProfile> getDatingProfiles(
            @RequestParam("boundary") String boundary,
            @RequestParam("count") String count,
            @RequestParam("_token") String token,
            @RequestParam("_agent") String agent,
            @RequestParam("_session") String session,
            @RequestParam("_v") String version) {
        return vkAdapterService.getRequest(boundary, count, token, agent, session, version);
    }
}
