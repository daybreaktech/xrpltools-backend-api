package com.daybreaktech.xrpltools.backendapi.event;

import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class SampleEvent extends ListenerAdapter {

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        super.onReady(event);
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (!event.getAuthor().getId().equals("946020363052130344")) {
            String message = event.getMessage().getContentDisplay();
            if (message.contains("pass butter")) {
                event.getMessage().addReaction("U+1F614").queue();
            }
        }

        super.onMessageReceived(event);
    }
}
