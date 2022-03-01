package com.daybreaktech.xrpltools.backendapi.controller;


import com.daybreaktech.xrpltools.backendapi.service.DiscordBotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${v1API}/bot")
@CrossOrigin("*")
public class DiscordBotController {

    @Autowired
    private DiscordBotService discordBotService;

}
