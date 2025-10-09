package io.github.dexrnzacattack.rrdiscordbridge.extensions;

import static io.github.dexrnzacattack.rrdiscordbridge.RRDiscordBridge.instance;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;

import com.vdurmont.semver4j.Semver;

import io.github.dexrnzacattack.rrdiscordbridge.RRDiscordBridge;
import io.github.dexrnzacattack.rrdiscordbridge.discord.DiscordBot;
import io.github.dexrnzacattack.rrdiscordbridge.extension.AbstractBridgeExtension;
import io.github.dexrnzacattack.rrdiscordbridge.extension.config.ExtensionConfig;
import io.github.dexrnzacattack.rrdiscordbridge.extension.event.events.chat.DiscordChatEvent;
import io.github.dexrnzacattack.rrdiscordbridge.extension.event.events.ExtensionEvents;
import io.github.dexrnzacattack.rrdiscordbridge.extension.event.events.chat.MinecraftChatEvent;
import io.github.dexrnzacattack.rrdiscordbridge.extension.event.registry.ExtensionEventRegistry;
import io.github.dexrnzacattack.rrdiscordbridge.extension.types.ModifiableMessage;
import io.github.dexrnzacattack.rrdiscordbridge.extensions.options.OpChatExtensionOptions;
import io.github.dexrnzacattack.rrdiscordbridge.interfaces.IPlayer;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.Webhook;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;

// opchatchatchatchatchatchatextension
public class OpChatExtension extends AbstractBridgeExtension {
    private final Semver version = new Semver("1.2.0", Semver.SemverType.LOOSE);
    public WebhookClient opcWebhookClient;
    public Webhook opcWebhook;
    private ExtensionConfig config;

    @Override
    public String getName() {
        return "OpChat";
    }

    @Override
    public String getAuthor() {
        return "§3Dexrn ZacAttack";
    }

    @Override
    public String getDescription() {
        return "§bA MCGalaxy inspired extension that sends messages to all online OPs, and optionally a discord channel. §9Syntax: §a## §2<msg>";
    }

    @Override
    public void onEnable() {
        try {
            this.config =
                    new ExtensionConfig(new OpChatExtensionOptions(), version, this.getName())
                            .load();
        } catch (IOException ignored) {
        }

        if (instance.getSettings().opchatChannelId.isEmpty()) {
            RRDiscordBridge.logger.warn(
                    "OPChat channel ID was not specified. Disabling extension.");
            disable();
            return;
        }

        TextChannel opcChannel =
                instance.getBot().jda.getTextChannelById(instance.getSettings().opchatChannelId);
        if (opcChannel == null) {
            RRDiscordBridge.logger.warn(
                    "Failed to find OPChat channel with ID "
                            + instance.getSettings().opchatChannelId);
            instance.getBridgeExtensions().enabledExtensions.remove(this);
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

        ExtensionEventRegistry.getInstance().register(this, ExtensionEvents.MINECRAFT_CHAT, this::onMinecraftChat);
        ExtensionEventRegistry.getInstance().register(this, ExtensionEvents.DISCORD_CHAT, this::onDiscordChat);
    }

    public void onMinecraftChat(MinecraftChatEvent ev) {
        final IPlayer player = ev.getPlayer();
        final ModifiableMessage<String> res = ev.getResult();

        if (!res.message.trim().startsWith("##")) return;

        res.message = res.message.trim().substring(2).trim();

        // in-case the player just sends "##" or "## "
        if (res.message.isEmpty()) return;

        res.cancelSendToDiscord();
        res.cancelSendToMinecraft();

        ExtensionConfig config = getConfig();
        if (config == null || !player.isOperator()
                && !((OpChatExtensionOptions) config.options).nonOpsCanSendMessages) {
            player.sendMessage("§cOnly operators are allowed to talk in OpChat.");
            return;
        }

        String opcMsg = String.format("§b[§r%s §b-> §6OPs§b] §r%s", player.getName(), res.message);

        // so that Console can display the messages
        RRDiscordBridge.logger.info(String.format("[%s -> OPs] %s", player.getName(), res.message));

        player.sendMessage(opcMsg);
        for (IPlayer p : player.getServer().getOnlinePlayers()) {
            // send the message to all ops (except for the player that sent the message, only if
            // they're op.)
            // note since we don't have a common .equals method, we check by name instead
            if (p.isOperator() && !p.getName().equals(player.getName())) p.sendMessage(opcMsg);
        }

        if (!instance.getSettings().opchatChannelId.isEmpty()) {
            DiscordBot.sendPlayerMessage(player.getName(), res.message, opcWebhookClient);
        }
    }

    public void onDiscordChat(DiscordChatEvent ev) {
        final Message message = ev.getMessage();
        final ModifiableMessage<Message> res = ev.getResult();

        String author = message.getAuthor().getId();
        if (message.getChannelId().equals(instance.getSettings().opchatChannelId)
                && !author.equals(instance.getBot().self.getId())
                && !author.equals(opcWebhook.getId())
                && !author.equals(instance.getBot().webhook.getId())) {
            ev.getResult().cancelSendToMinecraft();
            String msg =
                    String.format(
                            "§b[§6OPChat§b - §e%s§b]§r %s",
                            DiscordBot.getName(message.getAuthor()), message.getContentRaw());

            RRDiscordBridge.logger.info(msg);

            for (IPlayer p : instance.getServer().getOnlinePlayers()) {
                // send the message to all ops
                if (p.isOperator()) p.sendMessage(msg);
            }
        }
    }

    @Override
    public void onDisable() {
        ExtensionEventRegistry.getInstance().unregisterAll(this);

        this.config = null;
        opcWebhook = null;

        if (opcWebhookClient != null) {
            opcWebhookClient.close();
            opcWebhookClient = null;
        }
    }

    @Override
    public void onRegister(RRDiscordBridge instance) {}

    @Override
    public @Nullable ExtensionConfig getConfig() {
        return config;
    }

    @Override
    public Semver getVersion() {
        return version;
    }
}
