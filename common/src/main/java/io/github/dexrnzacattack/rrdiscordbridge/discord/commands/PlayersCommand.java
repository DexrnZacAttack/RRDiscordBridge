package io.github.dexrnzacattack.rrdiscordbridge.discord.commands;

import io.github.dexrnzacattack.rrdiscordbridge.RRDiscordBridge;
import io.github.dexrnzacattack.rrdiscordbridge.interfaces.IPlayer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Stream;

/**
 * Sends a message showing containing the current player count, their names, and whether they are opped.
 * <p>
 * The player list will not be sent if {@link io.github.dexrnzacattack.rrdiscordbridge.Settings#publicPlayerNames publicPlayerNames} is disabled
 * <p>
 * The OP status of all players will not be publicized if {@link io.github.dexrnzacattack.rrdiscordbridge.Settings#publicOperatorNames publicOperatorNames} is disabled
 */
public class PlayersCommand extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("players")) {
            IPlayer[] players = RRDiscordBridge.instance.getServer().getOnlinePlayers();
            StringBuilder playerList = new StringBuilder();

            EmbedBuilder builder = new EmbedBuilder().setTitle(String.format("%s/%s player(s) online", RRDiscordBridge.instance.getServer().getOnlinePlayers().length, RRDiscordBridge.instance.getServer().getMaxPlayers()));

            if (RRDiscordBridge.instance.getSettings().publicPlayerNames) {
                Stream<IPlayer> p = Arrays.stream(players);

                // sort by ops first (if enabled)
                Comparator<IPlayer> c =
                        RRDiscordBridge.instance.getSettings().publicOperatorNames
                                ? Comparator.comparing(IPlayer::isOperator).reversed()
                                .thenComparing(IPlayer::getName, String.CASE_INSENSITIVE_ORDER)
                                : Comparator.comparing(IPlayer::getName, String.CASE_INSENSITIVE_ORDER);

                p.sorted(c).forEach(player -> {
                    playerList.append(String.format(" - %s%s%s\n", player.isOperator()
                            && RRDiscordBridge.instance.getSettings().publicOperatorNames
                            ? "**[OP] "
                            : "", player.getName(), player.isOperator()
                            && RRDiscordBridge.instance.getSettings().publicOperatorNames
                            ? "**"
                            : ""));
                });

                builder.setDescription(playerList);
            }

            MessageEmbed embed = builder.build();
            event.replyEmbeds(embed).queue();
        }
    }
}
