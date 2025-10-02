package io.github.dexrnzacattack.rrdiscordbridge.extensions;

import static io.github.dexrnzacattack.rrdiscordbridge.RRDiscordBridge.instance;
import static io.github.dexrnzacattack.rrdiscordbridge.RRDiscordBridge.logger;

import club.minnced.discord.webhook.send.*;

import com.vdurmont.semver4j.Semver;

import io.github.dexrnzacattack.rrdiscordbridge.RRDiscordBridge;
import io.github.dexrnzacattack.rrdiscordbridge.extension.IBridgeExtension;
import io.github.dexrnzacattack.rrdiscordbridge.extension.config.ExtensionConfig;
import io.github.dexrnzacattack.rrdiscordbridge.extension.result.ModifiableExtensionChatResult;
import io.github.dexrnzacattack.rrdiscordbridge.extensions.options.WaypointExtensionOptions;
import io.github.dexrnzacattack.rrdiscordbridge.extensions.waypoints.Waypoint;
import io.github.dexrnzacattack.rrdiscordbridge.extensions.waypoints.WaypointType;
import io.github.dexrnzacattack.rrdiscordbridge.helpers.ColorHelper;
import io.github.dexrnzacattack.rrdiscordbridge.interfaces.IPlayer;

import net.dv8tion.jda.api.entities.Message;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Arrays;

public class WaypointExtension implements IBridgeExtension {
    private final Semver version = new Semver("1.1.0", Semver.SemverType.LOOSE);
    public ExtensionConfig config;

    @Override
    public String getName() {
        return "Waypoints";
    }

    @Override
    public String getAuthor() {
        return "§3Dexrn ZacAttack";
    }

    @Override
    public String getDescription() {
        return "§bSends a human readable webhook message to Discord instead of the normal waypoint text that most mods send.";
    }

    @Override
    public void onRegister(RRDiscordBridge instance) {}

    @Override
    public void onEnable() {
        try {
            this.config =
                    new ExtensionConfig(new WaypointExtensionOptions(), version, this.getName())
                            .load();
        } catch (IOException ignored) {
        }
    }

    @Override
    public void onDisable() {}

    @Override
    public void onMinecraftChat(
            IPlayer player, String message, ModifiableExtensionChatResult<String> event) {
        try {
            Waypoint waypoint =
                    Arrays.stream(WaypointType.values())
                            .filter(w -> w.getValidator().test(message))
                            .findFirst()
                            .map(w -> w.getFromString().apply(message))
                            .orElseThrow(
                                    () ->
                                            new IllegalArgumentException(
                                                    "Invalid waypoint: " + message));

            AllowedMentions allowedMentions =
                    new AllowedMentions().withParseUsers(true).withParseEveryone(false);

            // TODO: translations will let us just grab a string based on the waypoint type
            WebhookEmbedBuilder embedBuilder = new WebhookEmbedBuilder();
            embedBuilder
                    .setAuthor(
                            new WebhookEmbed.EmbedAuthor(
                                    String.format(
                                            "%s shared %swaypoint",
                                            player.getName(),
                                            waypoint.getType() == WaypointType.XAEROS
                                                    ? "an Xaero's Minimap "
                                                    : "a JourneyMap/VoxelMap "),
                                    null,
                                    null))
                    .setTitle(new WebhookEmbed.EmbedTitle(waypoint.getName(), null))
                    .addField(new WebhookEmbed.EmbedField(true, "X", waypoint.getX()))
                    .addField(new WebhookEmbed.EmbedField(true, "Y", waypoint.getY()))
                    .addField(new WebhookEmbed.EmbedField(true, "Z", waypoint.getZ()));

            WaypointExtensionOptions options = (WaypointExtensionOptions) this.config.options;

            String badge = URLEncoder.encode(waypoint.getBadge(), "UTF-8");
            if (options.waypointBadge.useBadgeImage
                    && waypoint.getColor() != null
                    && !badge.isEmpty())
                embedBuilder.setThumbnailUrl(
                        String.format(
                                options.waypointBadge.url,
                                options.waypointBadge.width,
                                options.waypointBadge.height,
                                ColorHelper.getRgbHex(waypoint.getColor().getRGB()),
                                ColorHelper.getRgbHex(options.waypointBadge.textColor),
                                badge));

            if (!waypoint.getDimension().isEmpty())
                embedBuilder.setFooter(new WebhookEmbed.EmbedFooter(waypoint.getDimension(), null));

            if (waypoint.getColor() != null) embedBuilder.setColor(waypoint.getColor().getRGB());

            WebhookEmbed embed = embedBuilder.build();

            WebhookMessage wMessage =
                    new WebhookMessageBuilder()
                            .setUsername(player.getName())
                            .setAvatarUrl(
                                    String.format(
                                            instance.getSettings().skinProvider, player.getName()))
                            .addEmbeds(embed)
                            .setAllowedMentions(allowedMentions)
                            .build();

            instance.getBot().webhookClient.send(wMessage);
            event.cancelSendToDiscord();
        } catch (Exception ex) {
            logger.error("shit: ", ex);
            // failed so we just send like nothing happened.
        }
    }

    @Override
    public void onDiscordChat(Message message, ModifiableExtensionChatResult<Message> event) {}

    @Override
    public @Nullable ExtensionConfig getConfig() {
        return this.config;
    }

    @Override
    public Semver getVersion() {
        return version;
    }
}
