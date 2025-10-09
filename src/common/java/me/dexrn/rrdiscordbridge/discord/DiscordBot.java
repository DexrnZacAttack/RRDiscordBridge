package me.dexrn.rrdiscordbridge.discord;

import static me.dexrn.rrdiscordbridge.RRDiscordBridge.instance;
import static me.dexrn.rrdiscordbridge.RRDiscordBridge.logger;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.AllowedMentions;
import club.minnced.discord.webhook.send.WebhookMessage;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;

import me.dexrn.rrdiscordbridge.config.Settings;
import me.dexrn.rrdiscordbridge.discord.commands.AboutCommand;
import me.dexrn.rrdiscordbridge.discord.commands.PlayersCommand;
import me.dexrn.rrdiscordbridge.extension.event.events.ExtensionEvents;
import me.dexrn.rrdiscordbridge.extension.event.events.chat.MinecraftChatEvent;
import me.dexrn.rrdiscordbridge.extension.event.registry.ExtensionEventRegistry;
import me.dexrn.rrdiscordbridge.extension.types.ModifiableMessage;
import me.dexrn.rrdiscordbridge.game.FormattingCodes;
import me.dexrn.rrdiscordbridge.interfaces.ICancellable;
import me.dexrn.rrdiscordbridge.interfaces.IPlayer;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;
import net.dv8tion.jda.internal.utils.JDALogger;

import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;

/** The Discord bot */
public class DiscordBot {
    /** The webhook client */
    public WebhookClient webhookClient;

    /** The webhook */
    public Webhook webhook;

    /** The bot's user */
    public User self;

    /** The JDA instance */
    public JDA jda;

    /** The bot's relay channel */
    TextChannel channel;

    /** Updates the player count */
    public final Runnable updatePlayerCountRunnable =
            () -> {
                if (instance != null && instance.getServer() != null)
                    this.setPlayerCount(instance.getServer().getOnlinePlayers().length);
            };

    /** Creates the bot */
    public DiscordBot() {
        JDALogger.setFallbackLoggerEnabled(false);
    }

    /**
     * Starts the bot
     *
     * @throws InterruptedException If the bot is interrupted while starting
     */
    public void start() throws InterruptedException {
        if (instance.getSettings().botToken.isEmpty()) {
            instance.getServer()
                    .broadcastMessage(
                            "Failed to load RRDiscordBridge, please check the console logs.");
            throw new RuntimeException(
                    String.format(
                            "Please set the bot token in %s", instance.getSettings().configPath));
        }

        // afaik we can make our own exception for this that will automatically yell in chat on
        // throw
        if (instance.getSettings().relayChannelId.isEmpty()) {
            instance.getServer()
                    .broadcastMessage(
                            "Failed to load RRDiscordBridge, please check the console logs.");
            throw new RuntimeException(
                    String.format(
                            "Please set the channel id (of the relay channel) in %s",
                            instance.getSettings().configPath));
        }

        jda =
                JDABuilder.createDefault(instance.getSettings().botToken)
                        .addEventListeners(
                                new DiscordEventHandler(), new PlayersCommand(), new AboutCommand())
                        .enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT)
                        .build()
                        .awaitReady();

        self = jda.getSelfUser();

        List<Webhook> webhooks = channel.retrieveWebhooks().complete();

        webhook =
                webhooks.stream()
                        .filter(
                                hook -> {
                                    User owner = hook.getOwnerAsUser();
                                    if (owner == null) return false;

                                    return owner.getId().equals(self.getId());
                                })
                        .findFirst()
                        .orElseGet(() -> channel.createWebhook("RRMCBridge").complete());

        WebhookClientBuilder builder = new WebhookClientBuilder(webhook.getUrl());
        builder.setThreadFactory(
                (job) -> {
                    Thread thread = new Thread(job);
                    thread.setName("RRDiscordBridgeBot");
                    thread.setDaemon(true);
                    return thread;
                });
        builder.setWait(true);
        webhookClient = builder.build();

        setPlayerCount();

        channel.getGuild()
                .updateCommands()
                .addCommands(
                        Commands.slash("players", "List of online players"),
                        Commands.slash("about", "Server info"))
                .queue();
    }

    /** Stops the bot */
    public void stop() {
        if (channel != null) {
            channel.getJDA().shutdown();
        }

        if (webhookClient != null) {
            webhookClient.close();
            webhookClient = null;
        }
    }

    /** Sets the RPC status */
    public void setPlayerCount() {
        Activity activity =
                Activity.playing(
                        String.format(
                                "with %s %s",
                                instance.getServer().getOnlinePlayers().length,
                                instance.getServer().getOnlinePlayers().length != 1
                                        ? "players"
                                        : "player"));
        jda.getPresence().setActivity(activity);
    }

    /**
     * Sets the RPC status
     *
     * @param i The new player count
     */
    public void setPlayerCount(int i) {
        Activity activity =
                Activity.playing(String.format("with %s %s", i, i != 1 ? "players" : "player"));
        jda.getPresence().setActivity(activity);
    }

    public static String getName(Member member) {
        String nickname = member.getNickname();

        if (member.getUser().isBot()) {
            return instance.getSettings().useNicknames && nickname != null && !nickname.isEmpty()
                    ? nickname
                    : member.getUser().getName();
        }

        String displayName = member.getUser().getGlobalName();

        return instance.getSettings().useNicknames && nickname != null && !nickname.isEmpty()
                ? nickname
                : instance.getSettings().useDisplayNames
                                && displayName != null
                                && !displayName.isEmpty()
                        ? displayName
                        : member.getUser().getName();
    }

    /**
     * Gets the name of a user based on the config settings
     *
     * @param user The user
     * @return The name of the user
     */
    public static String getName(User user) {
        if (user.isBot()) {
            return user.getName();
        }

        String displayName = user.getGlobalName();

        // explanation:
        // if useDisplayNames is enabled, use their displayname, otherwise use their username.
        return instance.getSettings().useDisplayNames
                        && displayName != null
                        && !displayName.isEmpty()
                ? user.getGlobalName()
                : user.getName();
    }

    /**
     * Sends a message using a webhook that uses the player's name and skin.
     *
     * @param player The player to use
     * @param message The message content
     * @param event The cancellable event
     */
    public void sendPlayerMessage(IPlayer player, String message, ICancellable event) {
        if (!instance.getSettings().enabledEvents.contains(Settings.Events.PLAYER_CHAT)) return;

        String msg = message;

        if (instance.getBridgeExtensions() != null) {
            MinecraftChatEvent ev = new MinecraftChatEvent(player, message);
            ExtensionEventRegistry.getInstance().invoke(ExtensionEvents.MINECRAFT_CHAT, ev);

            ModifiableMessage<String> res = ev.getResult();
            msg = res.message;

            if (!res.getShouldSendToMinecraft()) event.cancel();

            if (!res.getShouldSendToDiscord()) return;
        }

        // disallows @everyone lol
        AllowedMentions allowedMentions =
                new AllowedMentions().withParseUsers(true).withParseEveryone(false);

        WebhookMessage messageSend =
                new WebhookMessageBuilder()
                        .setUsername(player.getName())
                        .setAvatarUrl(
                                String.format(
                                        instance.getSettings().skinProvider, player.getName()))
                        .setContent(FormattingCodes.removeDcFormatting(msg))
                        .setAllowedMentions(allowedMentions)
                        .build();
        webhookClient.send(messageSend);
    }

    /**
     * Sends a message using a webhook that uses the player's name and skin.
     *
     * @param playerName The player name
     * @param message The message
     */
    public void sendPlayerMessage(String playerName, String message) {
        if (!instance.getSettings().enabledEvents.contains(Settings.Events.PLAYER_CHAT)) return;

        AllowedMentions allowedMentions =
                new AllowedMentions().withParseUsers(true).withParseEveryone(false);

        WebhookMessage wMessage =
                new WebhookMessageBuilder()
                        .setUsername(playerName)
                        .setAvatarUrl(
                                String.format(instance.getSettings().skinProvider, playerName))
                        .setContent(FormattingCodes.removeDcFormatting(message))
                        .setAllowedMentions(allowedMentions)
                        .build();
        webhookClient.send(wMessage);
    }

    /**
     * Sends a message using a provided webhook that uses the player's name and skin.
     *
     * @param playerName The player name
     * @param message The message
     * @param wc The webhook
     */
    public static void sendPlayerMessage(String playerName, String message, WebhookClient wc) {
        if (!instance.getSettings().enabledEvents.contains(Settings.Events.PLAYER_CHAT)) return;

        AllowedMentions allowedMentions =
                new AllowedMentions().withParseUsers(true).withParseEveryone(false);

        WebhookMessage wMessage =
                new WebhookMessageBuilder()
                        .setUsername(playerName)
                        .setAvatarUrl(
                                String.format(instance.getSettings().skinProvider, playerName))
                        .setContent(FormattingCodes.removeDcFormatting(message))
                        .setAllowedMentions(allowedMentions)
                        .build();
        wc.send(wMessage);
    }

    /**
     * Sends a message using a webhook that uses the player's name and skin.
     *
     * @param eventType The event type (used for Settings.enabledEvents).
     * @param player The player
     * @param message The message
     */
    public void sendPlayerMessage(Settings.Events eventType, IPlayer player, String message) {
        if (!instance.getSettings().enabledEvents.contains(eventType)) return;

        String msg = message;

        if (instance.getBridgeExtensions() != null) {
            MinecraftChatEvent ev = new MinecraftChatEvent(player, message);
            ExtensionEventRegistry.getInstance().invoke(ExtensionEvents.MINECRAFT_CHAT, ev);

            ModifiableMessage<String> res = ev.getResult();
            msg = res.message;

            if (!(boolean) res.getShouldSendToDiscord()) return;
        }

        AllowedMentions allowedMentions =
                new AllowedMentions().withParseUsers(true).withParseEveryone(false);

        WebhookMessage wMessage =
                new WebhookMessageBuilder()
                        .setUsername(player.getName())
                        .setAvatarUrl(
                                String.format(
                                        instance.getSettings().skinProvider, player.getName()))
                        .setContent(FormattingCodes.removeDcFormatting(msg))
                        .setAllowedMentions(allowedMentions)
                        .build();
        webhookClient.send(wMessage);
    }

    /**
     * Sends a message using a webhook that uses the player's name and skin.
     *
     * @param playerName The player name
     * @param playerSkinName The name of the player that you want to use as the skin
     * @param message The message
     */
    public void sendPlayerMessage(String playerName, String playerSkinName, String message) {
        if (!instance.getSettings().enabledEvents.contains(Settings.Events.PLAYER_CHAT)) return;

        AllowedMentions allowedMentions =
                new AllowedMentions().withParseUsers(true).withParseEveryone(false);

        WebhookMessage wMessage =
                new WebhookMessageBuilder()
                        .setUsername(playerName)
                        .setAvatarUrl(
                                String.format(instance.getSettings().skinProvider, playerSkinName))
                        .setContent(FormattingCodes.removeDcFormatting(message))
                        .setAllowedMentions(allowedMentions)
                        .build();
        webhookClient.send(wMessage);
    }

    /**
     * Sends a message using a webhook that uses the player's name and skin.
     *
     * @param eventType The event type (used for Settings.enabledEvents).
     * @param player The player
     * @param playerSkinName The name of the player that you want to use as the skin
     * @param message The message
     */
    public void sendPlayerMessage(
            Settings.Events eventType, IPlayer player, String playerSkinName, String message) {
        if (!instance.getSettings().enabledEvents.contains(eventType)) return;

        String msg = message;

        if (instance.getBridgeExtensions() != null) {
            MinecraftChatEvent ev = new MinecraftChatEvent(player, message);
            ExtensionEventRegistry.getInstance().invoke(ExtensionEvents.MINECRAFT_CHAT, ev);

            ModifiableMessage<String> res = ev.getResult();
            msg = res.message;

            if (!(boolean) res.getShouldSendToDiscord()) return;
        }

        AllowedMentions allowedMentions =
                new AllowedMentions().withParseUsers(true).withParseEveryone(false);

        WebhookMessage wMessage =
                new WebhookMessageBuilder()
                        .setUsername(player.getName())
                        .setAvatarUrl(
                                String.format(instance.getSettings().skinProvider, playerSkinName))
                        .setContent(FormattingCodes.removeDcFormatting(msg))
                        .setAllowedMentions(allowedMentions)
                        .build();
        webhookClient.send(wMessage);
    }

    /**
     * Sends an embed that uses the player's info as the author info to the Discord channel
     *
     * @param eventType The event type (used for Settings.enabledEvents).
     * @param playerName The name of the player that you want to be used for the author info.
     * @param description Message in the description part of the embed.
     * @param color Color of the side of the embed.
     * @param title Message in the title part of the embed.
     */
    public void sendPlayerEvent(
            Settings.Events eventType,
            String playerName,
            String description,
            Color color,
            String title) {
        if (!instance.getSettings().enabledEvents.contains(eventType)) return;

        EmbedBuilder embed =
                new EmbedBuilder()
                        .setColor(color)
                        .setDescription(FormattingCodes.removeDcFormatting(description))
                        .setTitle(FormattingCodes.removeDcFormatting(title))
                        .setTimestamp(java.time.Instant.now())
                        .setAuthor(
                                playerName,
                                null,
                                String.format(instance.getSettings().skinProvider, playerName));

        channel.sendMessageEmbeds(embed.build()).queue();
    }

    /**
     * Sends an embed that uses the player's skin as the author picture to the Discord channel
     *
     * @param eventType The event type (used for Settings.enabledEvents).
     * @param playerName The name of the player that you want to be used for the author picture.
     * @param authorName The name of the author that you want to be used for the author name.
     * @param description Message in the description part of the embed.
     * @param color Color of the side of the embed.
     * @param title Message in the title part of the embed.
     * @param footer Message in the footer part of the embed
     */
    public void sendPlayerEvent(
            Settings.Events eventType,
            String playerName,
            String authorName,
            String description,
            Color color,
            String title,
            String footer) {
        if (!instance.getSettings().enabledEvents.contains(eventType)) return;

        EmbedBuilder embed =
                new EmbedBuilder()
                        .setColor(color)
                        .setDescription(FormattingCodes.removeDcFormatting(description))
                        .setTitle(FormattingCodes.removeDcFormatting(title))
                        .setTimestamp(java.time.Instant.now())
                        .setFooter(FormattingCodes.removeDcFormatting(footer))
                        .setAuthor(
                                authorName,
                                null,
                                String.format(instance.getSettings().skinProvider, playerName));

        channel.sendMessageEmbeds(embed.build()).queue();
    }

    /**
     * Sends an embed for an event
     *
     * @param eventType The event type (used for Settings.enabledEvents).
     * @param author The author info.
     * @param description Message in the description part of the embed.
     * @param color Color of the side of the embed.
     * @param title Message in the title part of the embed.
     */
    public void sendEvent(
            Settings.Events eventType,
            MessageEmbed.AuthorInfo author,
            String description,
            Color color,
            String title) {
        if (!instance.getSettings().enabledEvents.contains(eventType)) return;

        EmbedBuilder embed =
                new EmbedBuilder()
                        .setColor(color)
                        .setDescription(FormattingCodes.removeDcFormatting(description))
                        .setTitle(FormattingCodes.removeDcFormatting(title))
                        .setTimestamp(java.time.Instant.now())
                        .setAuthor(author.getName(), null, author.getIconUrl());

        channel.sendMessageEmbeds(embed.build()).queue();
    }

    /**
     * Sends a message to the channel
     *
     * @param message The message
     */
    public void sendMessage(String message) {
        if (channel != null) {
            channel.sendMessage(message).queue();
        } else {
            logger.warn("bot isn't ready!");
        }
    }

    /**
     * Sends a message to a channel
     *
     * @param message The message
     * @param channel The channel to send the message in
     * @return The sent message
     */
    public static Message sendMessage(String message, TextChannel channel) {
        if (channel != null) {
            MessageCreateAction action = channel.sendMessage(message);
            return action.complete();
        } else {
            logger.warn("Cannot send because provided channel is null.");
        }
        return null;
    }

    /**
     * Sends a message to a channel
     *
     * @param message The message
     * @param channel The channel to send the message in
     */
    public Message sendMessage(String message, String channel) {
        TextChannel txtChannel = jda.getTextChannelById(channel);
        if (txtChannel != null) {
            MessageCreateAction action = txtChannel.sendMessage(message);
            return action.complete();
        } else {
            logger.warn("Could not find channel " + channel);
        }
        return null;
    }

    /**
     * Edits an existing message
     *
     * @param newMessage The new message content
     * @param msg The message you want to edit
     */
    public static void editMessage(String newMessage, Message msg) {
        if (msg != null) {
            msg.editMessage(newMessage).queue();
        } else {
            logger.warn("Cannot edit because provided message is null.");
        }
    }

    static @NotNull String trimDiscordMessage(Message message) {
        String messageTrimmed = "§7Empty message";

        if (!message.getAttachments().isEmpty()) messageTrimmed = "";

        if (!message.getContentDisplay().isEmpty()) {
            messageTrimmed =
                    message.getContentDisplay()
                            .substring(
                                    0,
                                    Math.min(
                                            instance.getSettings().maxMessageSize,
                                            message.getContentDisplay().length()));
        }

        return messageTrimmed;
    }
}
