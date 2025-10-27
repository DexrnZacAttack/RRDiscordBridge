package me.dexrn.rrdiscordbridge.extensions;

import static me.dexrn.rrdiscordbridge.RRDiscordBridge.instance;

import club.minnced.discord.webhook.send.*;

import com.vdurmont.semver4j.Semver;

import me.dexrn.rrdiscordbridge.RRDiscordBridge;
import me.dexrn.rrdiscordbridge.extension.AbstractBridgeExtension;
import me.dexrn.rrdiscordbridge.extension.config.ExtensionConfig;
import me.dexrn.rrdiscordbridge.extension.event.events.ExtensionEvents;
import me.dexrn.rrdiscordbridge.extension.event.events.chat.MinecraftChatEvent;
import me.dexrn.rrdiscordbridge.extension.event.registry.ExtensionEventRegistry;
import me.dexrn.rrdiscordbridge.extension.types.ModifiableMessage;
import me.dexrn.rrdiscordbridge.extensions.options.WaypointExtensionOptions;
import me.dexrn.rrdiscordbridge.extensions.waypoints.Waypoint;
import me.dexrn.rrdiscordbridge.extensions.waypoints.WaypointType;
import me.dexrn.rrdiscordbridge.helpers.ColorHelper;
import me.dexrn.rrdiscordbridge.interfaces.IPlayer;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Arrays;

public class WaypointExtension extends AbstractBridgeExtension {
    private final Semver version = new Semver("1.2.1", Semver.SemverType.LOOSE);
    public WaypointExtensionOptions options;
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
            this.options = (WaypointExtensionOptions) this.config.options;
        } catch (IOException ignored) {
        }

        ExtensionEventRegistry.getInstance()
                .register(this, ExtensionEvents.MINECRAFT_CHAT, this::onMinecraftChat);
    }

    @Override
    public void onDisable() {
        ExtensionEventRegistry.getInstance().unregisterAll(this);
    }

    public void onMinecraftChat(MinecraftChatEvent ev) {
        final String message = ev.getMessage();
        final IPlayer player = ev.getPlayer();
        final ModifiableMessage<String> res = ev.getResult();

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
            ev.getResult().cancelSendToDiscord();
        } catch (Exception ignored) {
            // failed so we just send like nothing happened.
        }
    }

    @Override
    public @Nullable ExtensionConfig getConfig() {
        return this.config;
    }

    @Override
    public Semver getVersion() {
        return version;
    }
}
