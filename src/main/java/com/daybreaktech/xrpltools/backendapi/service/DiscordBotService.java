package com.daybreaktech.xrpltools.backendapi.service;

import com.daybreaktech.xrpltools.backendapi.event.SampleEvent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.security.auth.login.LoginException;

@Service
public class DiscordBotService {

    @Value("${discord.bot.apiKey}")
    private String apiKey;

    @Value("${discord.guildId}")
    private Long guildId;

    @Value("${discord.stats.xrp-price-voice-channel}")
    private Long xrpPriceVoiceChannel;

    private JDA jda;

    @Autowired
    private CoinGeckoService coinGeckoService;

    @PostConstruct
    private void postConstruct() throws LoginException, InterruptedException {
        jda = JDABuilder.create(apiKey,
                GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_VOICE_STATES)
                .build();
        jda.awaitReady();
    }

    public void sendMessageToChannel(String message, Long channelId) {
        TextChannel channel = jda.getGuildById(guildId)
                .getTextChannelById(channelId);
        channel.sendMessage(message).queue();
    }

    @Scheduled(fixedDelay = 424000)
    public void updateXrpPrice() {
        String price = coinGeckoService.getCurrentXrpPrice();
        VoiceChannel voiceChannel = jda.getGuildById(guildId)
                .getVoiceChannelById(xrpPriceVoiceChannel);

        if (price != null && !price.isEmpty()) {
            voiceChannel.getManager().setName("XRP Price: " + price).queue();
        }
    }

}
