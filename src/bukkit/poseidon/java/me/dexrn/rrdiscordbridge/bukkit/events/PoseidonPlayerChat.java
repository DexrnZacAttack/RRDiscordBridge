package me.dexrn.rrdiscordbridge.bukkit.events;

import me.dexrn.rrdiscordbridge.Events;
import me.dexrn.rrdiscordbridge.bukkit.impls.BukkitCancellable;
import me.dexrn.rrdiscordbridge.bukkit.impls.PoseidonPlayer;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

public class PoseidonPlayerChat implements Listener {
    @EventHandler
    public void onPlayerChatLegacy(PlayerChatEvent event) {
        Events.onChatMessage(
                new PoseidonPlayer(event.getPlayer()),
                event.getMessage(),
                new BukkitCancellable(event));
    }
}
