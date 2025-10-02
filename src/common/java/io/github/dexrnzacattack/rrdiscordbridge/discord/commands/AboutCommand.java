package io.github.dexrnzacattack.rrdiscordbridge.discord.commands;

import io.github.dexrnzacattack.rrdiscordbridge.RRDiscordBridge;
import io.github.dexrnzacattack.rrdiscordbridge.game.FormattingCodes;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import net.dv8tion.jda.api.utils.FileUpload;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Sends an embed containing the following server information:
 *
 * <ul>
 *   <li>The server's icon (if enabled and {@code server-icon.png} exists)
 *   <li>The server's name (if available from {@code server.properties})
 *   <li>The server's MOTD (if available from {@code server.properties})
 *   <li>The server software's version
 *   <li>All players that are opped on the server (if enabled)
 *   <li>The server's uptime
 *   <li>The version of RRDiscordBridge that the server is running
 * </ul>
 */
public class AboutCommand extends ListenerAdapter {

    public String getUptime() {
        long elapsedMillis = System.currentTimeMillis() - RRDiscordBridge.getServerStartTime();

        long seconds = (elapsedMillis / 1000) % 60;
        long minutes = (elapsedMillis / (1000 * 60)) % 60;
        long hours = (elapsedMillis / (1000 * 60 * 60));

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("about")) return;

        EmbedBuilder builder = new EmbedBuilder().setTitle("About");

        File icon = new File("server-icon.png");

        if (RRDiscordBridge.instance.getSettings().showServerIcon && icon.exists()) {
            builder.setThumbnail("attachment://server-icon.png");
        }

        if (RRDiscordBridge.instance.getSupportedFeatures().canGetServerName())
            builder.addField(
                    "Name",
                    FormattingCodes.removeDcFormatting(
                            RRDiscordBridge.instance.getServer().getName()),
                    true);

        // doesn't work in 1.1-
        if (RRDiscordBridge.instance.getSupportedFeatures().canGetServerMotd())
            builder.addField(
                    "MOTD",
                    FormattingCodes.removeDcFormatting(
                            RRDiscordBridge.instance.getServer().getMotd()),
                    true);

        builder.addField("Version", RRDiscordBridge.instance.getServer().getVersion(), true);

        if (RRDiscordBridge.instance.getSettings().publicOperatorNames) {
            if (RRDiscordBridge.instance.getSupportedFeatures().canQueryServerOperators()) {
                String[] ops = RRDiscordBridge.instance.getServer().getOperators();
                builder.addField(
                        "Operators",
                        (ops.length > 0 ? " - " + String.join("\n - ", ops) : "No operators"),
                        false);
            } else {
                // note to self: can't seem to easily refactor since readAllLines demands a Path
                // instead...
                String dir = new File(".").getAbsolutePath();
                Path opsTxt = Paths.get(dir, "ops.txt");
                List<String> ops = null;
                try {
                    if (opsTxt.toFile().exists()) {
                        ops = Files.readAllLines(opsTxt.toAbsolutePath());
                        builder.addField(
                                "Operators",
                                (!ops.isEmpty()
                                        ? " - " + String.join("\n - ", ops)
                                        : "No operators"),
                                false);
                    }

                } catch (IOException e) {
                    RRDiscordBridge.logger.warn(
                            String.format(
                                    "Couldn't get the OPs list at %s", opsTxt.toAbsolutePath()),
                            e);
                }
            }
        }

        String uptime =
                RRDiscordBridge.instance.getSettings().useDiscordRelativeTimestamp
                        ? String.format("<t:%d:R>", RRDiscordBridge.getServerStartTime() / 1000)
                        : getUptime();

        builder.addField("Uptime", uptime, false);

        builder.setFooter(
                String.format(
                        "RRDiscordBridge v%s running on %s",
                        RRDiscordBridge.getVersion(),
                        RRDiscordBridge.instance.getServer().getSoftwareName()));

        MessageEmbed embed = builder.build();
        ReplyCallbackAction callback = event.replyEmbeds(embed);

        if (icon.exists()) callback.addFiles(FileUpload.fromData(icon, "server-icon.png"));

        callback.queue();
    }
}
