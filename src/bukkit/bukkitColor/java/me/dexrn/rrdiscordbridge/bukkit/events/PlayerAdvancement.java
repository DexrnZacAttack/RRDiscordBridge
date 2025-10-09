package me.dexrn.rrdiscordbridge.bukkit.events;

import me.dexrn.rrdiscordbridge.Events;
import me.dexrn.rrdiscordbridge.bukkit.impls.BukkitPlayer;
import me.dexrn.rrdiscordbridge.game.Advancement;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PlayerAdvancement implements Listener {
    @EventHandler
    public void onPlayerAdvancement(PlayerAdvancementDoneEvent event) {
        // Thanks to the folks over on the Minecraft Wiki discord who made it so that a
        // ResourceLocation in the URL will take you to the correct entry in the table
        String loc = event.getAdvancement().getKey().getKey();

        String v = loc.split("/")[1];

        if (loc.startsWith("recipes/") || v.equals("root")) return;

        String resource = v.replaceAll("_", " ");

        // https://gist.github.com/Hylke1982/166a792313c5e2df9d31
        String name =
                Stream.of(resource.trim().split("\\s"))
                        .filter(word -> !word.isEmpty())
                        .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1))
                        .collect(Collectors.joining(" "));

        Events.onPlayerAchievement(
                Advancement.Type.ADVANCEMENT, new BukkitPlayer(event.getPlayer()), name, null, loc);
    }
}
