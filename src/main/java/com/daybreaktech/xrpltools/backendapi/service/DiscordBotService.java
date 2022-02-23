package com.daybreaktech.xrpltools.backendapi.service;

import com.daybreaktech.xrpltools.backendapi.event.SampleEvent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.security.auth.login.LoginException;

@Service
public class DiscordBotService {

    @Value("${discord.bot.apiKey}")
    private String apiKey;

    @Value("${discord.guildId}")
    private Long guildId;

    @Value("${discord.channels.test}")
    private Long testChannel;

    private JDA jda;

    @PostConstruct
    private void postConstruct() throws LoginException, InterruptedException {
        jda = JDABuilder.create(apiKey,
                GatewayIntent.GUILD_MESSAGES)
                .addEventListeners(new SampleEvent())
                .build();
        jda.awaitReady();
    }

    public void sendMessageToChannel(String message) {
        TextChannel channel = jda.getGuildById(guildId)
                .getTextChannelById(testChannel);
        channel.sendMessage(message).queue();
    }

}
