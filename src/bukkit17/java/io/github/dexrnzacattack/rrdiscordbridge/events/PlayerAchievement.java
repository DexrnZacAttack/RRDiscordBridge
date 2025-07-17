package io.github.dexrnzacattack.rrdiscordbridge.events;

import io.github.dexrnzacattack.rrdiscordbridge.Events;
import io.github.dexrnzacattack.rrdiscordbridge.game.Advancement;
import io.github.dexrnzacattack.rrdiscordbridge.impls.BukkitPlayer;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAchievementAwardedEvent;

public class PlayerAchievement implements Listener {
    // TODO: advancements
    @EventHandler
    public void onPlayerAchievement(PlayerAchievementAwardedEvent event) {
        Events.onPlayerAchievement(
                Advancement.Type.ACHIEVEMENT,
                new BukkitPlayer(event.getPlayer()),
                event.getAchievement().name());
    }
}
