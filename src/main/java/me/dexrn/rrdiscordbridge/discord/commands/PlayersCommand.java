package me.dexrn.rrdiscordbridge.discord.commands;

import me.dexrn.rrdiscordbridge.RRDiscordBridge;
import me.dexrn.rrdiscordbridge.config.Settings;
import me.dexrn.rrdiscordbridge.interfaces.IPlayer;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Stream;

/**
 * Sends a message showing containing the current player count, their names, and whether they are
 * opped.
 *
 * <p>The player list will not be sent if {@link Settings#publicPlayerNames publicPlayerNames} is
 * disabled
 *
 * <p>The OP status of all players will not be publicized if {@link Settings#publicOperatorNames
 * publicOperatorNames} is disabled
 */
public class PlayersCommand extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("players")) {
            IPlayer[] players = RRDiscordBridge.instance.getServer().getOnlinePlayers();
            StringBuilder playerList = new StringBuilder();

            int limit = RRDiscordBridge.instance.getServer().getMaxPlayers();

            String t =
                    String.format(
                            "%s%s player(s) online",
                            RRDiscordBridge.instance.getServer().getOnlinePlayers().length,
                            limit != -1 ? "/" + limit : "");

            EmbedBuilder builder = new EmbedBuilder().setTitle(t);

            if (RRDiscordBridge.instance.getSettings().publicPlayerNames) {
                Stream<IPlayer> p = Arrays.stream(players);

                // sort by ops first (if enabled)
                Comparator<IPlayer> c =
                        RRDiscordBridge.instance.getSettings().publicOperatorNames
                                ? Comparator.comparing(IPlayer::isOperator)
                                        .reversed()
                                        .thenComparing(
                                                IPlayer::getName, String.CASE_INSENSITIVE_ORDER)
                                : Comparator.comparing(
                                        IPlayer::getName, String.CASE_INSENSITIVE_ORDER);

                p.sorted(c)
                        .forEach(
                                player -> {
                                    playerList.append(
                                            String.format(
                                                    " - %s%s%s\n",
                                                    player.isOperator()
                                                                    && RRDiscordBridge.instance
                                                                            .getSettings()
                                                                            .publicOperatorNames
                                                            ? "**[OP] "
                                                            : "",
                                                    player.getName(),
                                                    player.isOperator()
                                                                    && RRDiscordBridge.instance
                                                                            .getSettings()
                                                                            .publicOperatorNames
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
