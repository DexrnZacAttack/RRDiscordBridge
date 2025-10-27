package me.dexrn.rrdiscordbridge;

import static me.dexrn.rrdiscordbridge.RRDiscordBridge.instance;

import me.dexrn.rrdiscordbridge.config.Settings;
import me.dexrn.rrdiscordbridge.game.Advancement;
import me.dexrn.rrdiscordbridge.impls.BroadcastPlayer;
import me.dexrn.rrdiscordbridge.interfaces.ICancellable;
import me.dexrn.rrdiscordbridge.interfaces.IPlayer;

import net.dv8tion.jda.api.entities.MessageEmbed;

import javax.annotation.Nullable;

/** In-game event handlers */
public class Events {
    /**
     * Runs when the player gets an achievement/advancement
     *
     * @param type The advancement type
     * @param player The player
     * @param achievement The achievement name
     * @param description The achievement description (if available)
     */
    public static void onPlayerAchievement(
            Advancement.Type type,
            IPlayer player,
            String achievement,
            @Nullable String description) {
        String str =
                achievement.startsWith("[") && achievement.endsWith("]")
                        ? achievement.substring(1, achievement.length() - 1)
                        : achievement;

        onPlayerAchievement(type, player, achievement, description, str);
    }

    /**
     * Runs when the player gets an achievement/advancement
     *
     * @param type The advancement type
     * @param player The player
     * @param achievement The achievement name
     * @param description The achievement description (if available)
     * @param wikiName The wiki achievement name to use in the link, e.g: {@code
     *     https://minecraft.wiki/w/Advancement#{wikiName}}
     */
    public static void onPlayerAchievement(
            Advancement.Type type,
            IPlayer player,
            String achievement,
            @Nullable String description,
            String wikiName) {
        String str =
                achievement.startsWith("[") && achievement.endsWith("]")
                        ? achievement.substring(1, achievement.length() - 1)
                        : achievement;

        String wiki = wikiName.replaceAll(" ", "_");

        switch (type) {
            case ACHIEVEMENT:
                str =
                        String.format(
                                "%s has just earned the achievement [[%s]](<https://minecraft.wiki/w/Achievement/Java_Edition#%s>)",
                                player.getName(), str, wiki);
                break;
            case ADVANCEMENT:
                str =
                        String.format(
                                "%s has made the advancement [[%s]](<https://minecraft.wiki/w/Advancement#%s>)",
                                player.getName(), str, wiki);
                break;
            case GOAL:
                str =
                        String.format(
                                "%s has reached the goal [[%s]](<https://minecraft.wiki/w/Advancement#%s>)",
                                player.getName(), str, wiki);
                break;
            case CHALLENGE:
                str =
                        String.format(
                                "%s has completed the challenge [[%s]](<https://minecraft.wiki/w/Advancement#%s>)",
                                player.getName(), str, wiki);
                break;
        }

        instance.getBot()
                .sendPlayerEvent(
                        Settings.Events.PLAYER_ACHIEVEMENT,
                        player.getName(),
                        type == Advancement.Type.ACHIEVEMENT
                                ? "Achievement Get!"
                                : "Advancement Get!",
                        str,
                        type == Advancement.Type.CHALLENGE
                                ? instance.getSettings().colorPalette.playerChallenge
                                : instance.getSettings().colorPalette.playerAchievement,
                        null,
                        description);
    }

    /**
     * Runs when a player joins the server
     *
     * @param player The player
     */
    public static void onPlayerJoin(IPlayer player) {
        // TODO: we can fall back probably
        if (instance.getSupportedFeatures().canQueryPlayerHasJoinedBefore()
                && !player.hasPlayedBefore()) {
            instance.getBot()
                    .sendPlayerEvent(
                            Settings.Events.PLAYER_JOIN,
                            player.getName(),
                            String.format(
                                    "%s joined the game for the first time.", player.getName()),
                            null,
                            instance.getSettings().colorPalette.playerFirstJoin,
                            null,
                            null);
        } else {
            instance.getBot()
                    .sendPlayerEvent(
                            Settings.Events.PLAYER_JOIN,
                            player.getName(),
                            String.format("%s joined the game.", player.getName()),
                            null,
                            instance.getSettings().colorPalette.playerJoin,
                            null,
                            null);
        }
        instance.getBot().setPlayerCount(instance.getServer().getOnlinePlayers().length);
    }

    /**
     * Runs when a player leaves the server
     *
     * @param player The player
     */
    public static void onPlayerLeave(IPlayer player) {
        if (instance == null) return;

        instance.getBot()
                .sendPlayerEvent(
                        Settings.Events.PLAYER_LEAVE,
                        player.getName(),
                        String.format("%s left the game.", player.getName()),
                        null,
                        instance.getSettings().colorPalette.playerLeave,
                        null,
                        null);
        instance.getBot().setPlayerCount(instance.getServer().getOnlinePlayers().length - 1);
    }

    /**
     * Runs when a player gets kicked from the server
     *
     * @param player The player
     * @param reason The kick reason
     */
    public static void onPlayerKick(IPlayer player, String reason) {
        if (instance == null) return;

        instance.getBot().setPlayerCount(instance.getServer().getOnlinePlayers().length - 1);
        instance.getBot()
                .sendPlayerEvent(
                        Settings.Events.PLAYER_KICK,
                        player.getName(),
                        String.format("%s was kicked.", player.getName()),
                        reason,
                        instance.getSettings().colorPalette.playerKick,
                        null,
                        null);
    }

    /**
     * Runs when a player runs a command
     *
     * @param player The player
     * @param command The command message
     */
    public static void onPlayerCommand(IPlayer player, String command) {
        // /me
        // TODO: we should attempt to mixin to the original command handler method for each command
        // instead of doing this jank!
        if (command.toLowerCase().startsWith("/me ")
                && command.length() > 4
                && instance.getSettings().enabledEvents.contains(Settings.Events.ME_COMMAND))
            instance.getBot()
                    .sendPlayerMessage(
                            Settings.Events.ME_COMMAND,
                            player,
                            String.format("_%s %s_", player.getName(), command.substring(4)));

        // /say
        // FIXME: getop check sort of works sure but this was broken to begin with because if you
        // ran /say without perms it would still broadcast without perms (as it never knows if the
        // command fails or not)
        if (command.toLowerCase().startsWith("/say ")
                && command.length() > 5
                && instance.getSettings().enabledEvents.contains(Settings.Events.SAY_BROADCAST)
                && player.isOperator())
            instance.getBot()
                    .sendPlayerMessage(
                            Settings.Events.SAY_BROADCAST,
                            new BroadcastPlayer(),
                            instance.getSettings().broadcastSkinName,
                            command.substring(5));
    }

    /**
     * Runs when a command is run from the server console
     *
     * @param command The command message
     */
    public static void onServerCommand(String command) {
        if (command.toLowerCase().startsWith("say ")
                && command.length() > 4
                && instance.getSettings().enabledEvents.contains(Settings.Events.SAY_BROADCAST))
            instance.getBot()
                    .sendPlayerMessage(
                            Settings.Events.SAY_BROADCAST,
                            new BroadcastPlayer(),
                            instance.getSettings().broadcastSkinName,
                            command.substring(4));
    }

    /**
     * Runs when a player sends a chat message
     *
     * @param sender The player that sent the message
     * @param message The message
     * @param event The cancellable event
     */
    public static void onChatMessage(IPlayer sender, String message, ICancellable event) {
        if (instance.getSettings().enabledEvents.contains(Settings.Events.PLAYER_CHAT))
            instance.getBot().sendPlayerMessage(sender, message, event);
    }

    /**
     * Runs when a player dies
     *
     * @param player The player
     * @param message The death message
     */
    public static void onPlayerDeath(IPlayer player, String message) {
        if (message != null && !message.trim().isEmpty()) {
            if (player == null) {
                // it seems some versions don't have the entity that died, just the message...
                instance.getBot()
                        .sendEvent(
                                Settings.Events.PLAYER_DEATH,
                                new MessageEmbed.AuthorInfo(message, null, null, null),
                                null,
                                instance.getSettings().colorPalette.playerDeath,
                                null);
            } else {
                instance.getBot()
                        .sendPlayerEvent(
                                Settings.Events.PLAYER_DEATH,
                                player.getName(),
                                message,
                                null,
                                instance.getSettings().colorPalette.playerDeath,
                                null,
                                null);
            }
        } else {
            if (player != null) {
                instance.getBot()
                        .sendPlayerEvent(
                                Settings.Events.PLAYER_DEATH,
                                player.getName(),
                                String.format("%s died", player.getName()),
                                null,
                                instance.getSettings().colorPalette.playerDeath,
                                null,
                                null);
            }
        }
    }
}
