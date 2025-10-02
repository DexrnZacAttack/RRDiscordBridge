package io.github.dexrnzacattack.rrdiscordbridge.bukkit.events;

import io.github.dexrnzacattack.rrdiscordbridge.Events;
import io.github.dexrnzacattack.rrdiscordbridge.bukkit.BukkitAdvancementType;
import io.github.dexrnzacattack.rrdiscordbridge.bukkit.impls.BukkitPlayer;

import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementDisplay;
import org.bukkit.advancement.AdvancementDisplayType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

public class PlayerAdvancement implements Listener {
    @EventHandler
    public void onPlayerAdvancement(PlayerAdvancementDoneEvent event) {
        Advancement adv = event.getAdvancement();
        AdvancementDisplay display = adv.getDisplay();

        if (display == null || !display.shouldAnnounceChat()) return;

        String title = display.getTitle();
        String description = display.getDescription();
        AdvancementDisplayType type = display.getType();

        Events.onPlayerAchievement(
                BukkitAdvancementType.getType(type),
                new BukkitPlayer(event.getPlayer()),
                title,
                description);
    }
}
