package io.github.dexrnzacattack.rrdiscordbridge.chat.extension.extensions;

import static io.github.dexrnzacattack.rrdiscordbridge.RRDiscordBridge.instance;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;

import io.github.dexrnzacattack.rrdiscordbridge.RRDiscordBridge;
import io.github.dexrnzacattack.rrdiscordbridge.chat.extension.IChatExtension;
import io.github.dexrnzacattack.rrdiscordbridge.chat.extension.result.ChatExtensionResult;
import io.github.dexrnzacattack.rrdiscordbridge.chat.extension.result.DiscordChatExtensionResult;
import io.github.dexrnzacattack.rrdiscordbridge.interfaces.IPlayer;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.Webhook;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.util.List;

// opchatchatchatchatchatchatextension
public class OpChatChatExtension implements IChatExtension {
    private final String name;
    public WebhookClient opcWebhookClient;
    public Webhook opcWebhook;

    public OpChatChatExtension(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return "§bA MCGalaxy inspired extension that sends messages to all online OPs, and optionally a discord channel. §9Syntax: §a## §2<msg>";
    }

    @Override
    public void onEnable() {
        if (instance.getSettings().opchatChannelId.isEmpty()) {
            RRDiscordBridge.logger.warn(
                    "OPChat channel ID was not specified. Disabling extension.");
            instance.getChatExtensions().enabledExtensions.remove(this);
            onDisable();
            return;
        }

        TextChannel opcChannel =
                instance.getBot().jda.getTextChannelById(instance.getSettings().opchatChannelId);
        if (opcChannel == null) {
            RRDiscordBridge.logger.warn(
                    "Failed to find OPChat channel with ID "
                            + instance.getSettings().opchatChannelId);
            instance.getChatExtensions().enabledExtensions.remove(this);
            onDisable();
            return;
        }

        List<Webhook> webhooks = opcChannel.retrieveWebhooks().complete();

        opcWebhook =
                webhooks.stream()
                        .filter(
                                hook -> {
                                    User owner = hook.getOwnerAsUser();
                                    if (owner == null) return false;

                                    return owner.getId()
                                            .equals(instance.getBot().jda.getSelfUser().getId());
                                })
                        .findFirst()
                        .orElseGet(() -> opcChannel.createWebhook("RRMCBridgeOpChat").complete());

        WebhookClientBuilder opcBuilder = new WebhookClientBuilder(opcWebhook.getUrl());
        opcBuilder.setThreadFactory(
                (job) -> {
                    Thread thread = new Thread(job);
                    thread.setName("RRDiscordBridgeBotOpChat");
                    thread.setDaemon(true);
                    return thread;
                });
        opcBuilder.setWait(true);
        opcWebhookClient = opcBuilder.build();
    }

    @Override
    public void onDisable() {
        opcWebhook = null;

        if (opcWebhookClient != null) {
            opcWebhookClient.close();
            opcWebhookClient = null;
        }
    }

    @Override
    public ChatExtensionResult onMCMessage(String message, String player) {
        if (!message.trim().startsWith("##")) return new ChatExtensionResult(message, true, true);

        message = message.trim().substring(2).trim();

        // in-case the player just sends "##" or "## "
        if (message.isEmpty()) return new ChatExtensionResult(message, true, true);

        String opcMsg = String.format("§b[§r%s §b-> §6OPs§b] §r%s", player, message);

        // so that Console can display the messages
        RRDiscordBridge.logger.info(String.format("[%s -> OPs] %s", player, message));

        instance.getServer().getPlayer(player).sendMessage(opcMsg);
        for (IPlayer p : instance.getServer().getOnlinePlayers()) {
            // send the message to all ops (except for the player that sent the message, only if
            // they're op.)
            // note since we don't have a common .equals method, we check by name instead
            if (p.isOperator()
                    && !p.getName().equals(instance.getServer().getPlayer(player).getName()))
                p.sendMessage(opcMsg);
        }

        if (!instance.getSettings().opchatChannelId.isEmpty()) {
            instance.getBot().sendPlayerMessage(player, message, opcWebhookClient);
        }

        return new ChatExtensionResult(message, false, false);
    }

    @Override
    public DiscordChatExtensionResult onDCMessage(Message message) {
        String author = message.getAuthor().getId();
        if (message.getChannelId().equals(instance.getSettings().opchatChannelId)
                && !author.equals(instance.getBot().self.getId())
                && !author.equals(opcWebhook.getId())
                && !author.equals(instance.getBot().webhook.getId())) {
            String msg =
                    String.format(
                            "§b[§6OPChat§b - §e%s§b]§r %s",
                            instance.getBot().getName(message.getAuthor()),
                            message.getContentRaw());

            RRDiscordBridge.logger.info(msg);

            for (IPlayer p : instance.getServer().getOnlinePlayers()) {
                // send the message to all ops
                if (p.isOperator()) p.sendMessage(msg);
            }
        }

        return new DiscordChatExtensionResult(message, true);
    }
}
