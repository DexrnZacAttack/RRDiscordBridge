package io.github.dexrnzacattack.rrdiscordbridge;

import static io.github.dexrnzacattack.rrdiscordbridge.RRDiscordBridge.REAL_ORANGE;

import io.github.dexrnzacattack.rrdiscordbridge.config.Settings;
import io.github.dexrnzacattack.rrdiscordbridge.discord.DiscordBot;
import io.github.dexrnzacattack.rrdiscordbridge.helpers.ReflectionHelper;
import io.github.dexrnzacattack.rrdiscordbridge.interfaces.ICancellable;
import io.github.dexrnzacattack.rrdiscordbridge.interfaces.IPlayer;

import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;

/** In-game event handlers */
public class Events {
    public static void onPlayerJoin(IPlayer player) {
        DiscordBot.setPlayerCount();
        if (ReflectionHelper.doesMethodExist("org.bukkit.entity.Player", "hasPlayedBefore")
                && !player.hasPlayedBefore()) {
            DiscordBot.sendPlayerEvent(
                    Settings.Events.PLAYER_JOIN,
                    player.getName(),
                    String.format("%s joined the game for the first time.", player.getName()),
                    null,
                    Color.GREEN,
                    null);
        } else {
            DiscordBot.sendPlayerEvent(
                    Settings.Events.PLAYER_JOIN,
                    player.getName(),
                    String.format("%s joined the game.", player.getName()),
                    null,
                    Color.GREEN,
                    null);
        }
    }

    public static void onPlayerLeave(IPlayer player) {
        DiscordBot.setPlayerCount(
                RRDiscordBridge.instance.getServer().getOnlinePlayers().length - 1);
        DiscordBot.sendPlayerEvent(
                Settings.Events.PLAYER_LEAVE,
                player.getName(),
                String.format("%s left the game.", player.getName()),
                null,
                REAL_ORANGE,
                null);
    }

    public static void onPlayerKick(IPlayer player, String reason) {
        DiscordBot.setPlayerCount(
                RRDiscordBridge.instance.getServer().getOnlinePlayers().length - 1);
        DiscordBot.sendPlayerEvent(
                Settings.Events.PLAYER_KICK,
                player.getName(),
                String.format("%s was kicked.", player.getName()),
                reason,
                REAL_ORANGE,
                null);
    }

    public static void onPlayerCommand(IPlayer player, String message) {
        // /me
        if (message.toLowerCase().startsWith("/me ")
                && message.length() > 4
                && RRDiscordBridge.instance
                        .getSettings()
                        .enabledEvents
                        .contains(Settings.Events.ME_COMMAND))
            DiscordBot.sendPlayerMessage(
                    Settings.Events.ME_COMMAND,
                    player.getName(),
                    String.format("_%s %s_", player.getName(), message.substring(4)));

        // /say
        // FIXME: getop check sort of works sure but this was broken to begin with because if you
        // ran /say without perms it would still broadcast without perms (as it never knows if the
        // command fails or not)
        if (message.toLowerCase().startsWith("/say ")
                && message.length() > 5
                && RRDiscordBridge.instance
                        .getSettings()
                        .enabledEvents
                        .contains(Settings.Events.SAY_BROADCAST)
                && player.isOperator())
            DiscordBot.sendPlayerMessage(
                    Settings.Events.SAY_BROADCAST,
                    "Server (Broadcast)",
                    RRDiscordBridge.instance.getSettings().broadcastSkinName,
                    message.substring(5));
    }

    public static void onServerCommand(String command) {
        if (command.toLowerCase().startsWith("say ")
                && command.length() > 4
                && RRDiscordBridge.instance
                        .getSettings()
                        .enabledEvents
                        .contains(Settings.Events.SAY_BROADCAST))
            DiscordBot.sendPlayerMessage(
                    Settings.Events.SAY_BROADCAST,
                    "Server (Broadcast)",
                    RRDiscordBridge.instance.getSettings().broadcastSkinName,
                    command.substring(4));
    }

    public static void onChatMessage(IPlayer sender, String message, ICancellable event) {
        if (RRDiscordBridge.instance
                .getSettings()
                .enabledEvents
                .contains(Settings.Events.PLAYER_CHAT))
            DiscordBot.sendPlayerMessage(sender.getName(), message, event);
    }

    public static void onPlayerDeath(IPlayer player, String message) {
        if (message != null && !message.trim().isEmpty()) {
            if (player == null) {
                // it seems some versions don't have the entity that died, just the message...
                DiscordBot.sendEvent(
                        Settings.Events.PLAYER_DEATH,
                        new MessageEmbed.AuthorInfo(message, null, null, null),
                        null,
                        Color.RED,
                        null);
            } else {
                DiscordBot.sendPlayerEvent(
                        Settings.Events.PLAYER_DEATH,
                        player.getName(),
                        message,
                        null,
                        Color.RED,
                        null);
            }
        } else {
            if (player != null) {
                DiscordBot.sendPlayerEvent(
                        Settings.Events.PLAYER_DEATH,
                        player.getName(),
                        String.format("%s died", player.getName()),
                        null,
                        Color.RED,
                        null);
            }
        }
    }
}
