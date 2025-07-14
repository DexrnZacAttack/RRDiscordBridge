package io.github.dexrnzacattack.rrdiscordbridge.chat.extension.extensions;

import club.minnced.discord.webhook.send.*;
import io.github.dexrnzacattack.rrdiscordbridge.RRDiscordBridge;
import io.github.dexrnzacattack.rrdiscordbridge.chat.extension.IChatExtension;
import io.github.dexrnzacattack.rrdiscordbridge.chat.extension.extensions.waypoints.JVMapWaypoints;
import io.github.dexrnzacattack.rrdiscordbridge.chat.extension.extensions.waypoints.Waypoint;
import io.github.dexrnzacattack.rrdiscordbridge.chat.extension.extensions.waypoints.XaerosWaypoints;
import io.github.dexrnzacattack.rrdiscordbridge.chat.extension.result.ChatExtensionResult;
import io.github.dexrnzacattack.rrdiscordbridge.chat.extension.result.DiscordChatExtensionResult;
import net.dv8tion.jda.api.entities.Message;

import static io.github.dexrnzacattack.rrdiscordbridge.discord.DiscordBot.webhookClient;

public class WaypointChatExtension implements IChatExtension {
    public String name;

    public WaypointChatExtension(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return "§bSends a human readable webhook message to Discord instead of the normal waypoint text that most mods send.";
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }

    @Override
    public ChatExtensionResult onMCMessage(String message, String player) {
        try {
            Waypoint waypoint;
            String type = "a ";

            AllowedMentions allowedMentions = new AllowedMentions()
                    .withParseUsers(true)
                    .withParseEveryone(false);

            WebhookEmbedBuilder embedBuilder = new WebhookEmbedBuilder();

            if (XaerosWaypoints.isWaypoint(message)) {
                XaerosWaypoints xWaypoint = XaerosWaypoints.fromString(message);
                waypoint = new Waypoint(xWaypoint.name, xWaypoint.x, xWaypoint.y, xWaypoint.z, xWaypoint.color, xWaypoint.group);
                // ehhhhhhhh weird way of doing this but it works
                type = "an Xaero's Minimap ";
            } else if ((message.charAt(0) == '[' && message.charAt(message.length() - 1) == ']')) {
                JVMapWaypoints jWaypoint = JVMapWaypoints.fromString(message);
                waypoint = new Waypoint(jWaypoint.name, Integer.toString(jWaypoint.x), Integer.toString(jWaypoint.y), Integer.toString(jWaypoint.z), null, jWaypoint.dim);
                type = "a JourneyMap/VoxelMap ";
            } else {
                throw new IllegalArgumentException("Not a waypoint!");
            }

            embedBuilder.setAuthor(new WebhookEmbed.EmbedAuthor(String.format("%s shared %swaypoint", player, type), null, null));
            if (waypoint.color != null)
                embedBuilder.setColor(waypoint.color.getRGB());
            embedBuilder.setTitle(new WebhookEmbed.EmbedTitle(waypoint.name, null));
            embedBuilder.addField(new WebhookEmbed.EmbedField(true, "X", waypoint.x));
            embedBuilder.addField(new WebhookEmbed.EmbedField(true, "Y", waypoint.y));
            embedBuilder.addField(new WebhookEmbed.EmbedField(true, "Z", waypoint.z));
            embedBuilder.setFooter(new WebhookEmbed.EmbedFooter(waypoint.dimension, null));

            WebhookEmbed embed = embedBuilder.build();

            WebhookMessage wMessage = new WebhookMessageBuilder()
                    .setUsername(player)
                    .setAvatarUrl(String.format(RRDiscordBridge.instance.getSettings().skinProvider, player))
                    .addEmbeds(embed)
                    .setAllowedMentions(allowedMentions)
                    .build();

            webhookClient.send(wMessage);
            return new ChatExtensionResult(message, true, false);
        } catch (Exception ignored) {
            // failed so we just send like nothing happened.
            return new ChatExtensionResult(message, true, true);
        }
    }

    @Override
    public DiscordChatExtensionResult onDCMessage(Message message) {
        return new DiscordChatExtensionResult(message, true);
    }
}
