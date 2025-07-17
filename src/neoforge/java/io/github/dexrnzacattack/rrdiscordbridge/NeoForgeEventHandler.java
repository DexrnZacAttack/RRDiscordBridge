package io.github.dexrnzacattack.rrdiscordbridge;

import io.github.dexrnzacattack.rrdiscordbridge.impls.NeoForgeCancellable;
import io.github.dexrnzacattack.rrdiscordbridge.impls.NeoForgePlayer;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.CommandEvent;
import net.neoforged.neoforge.event.ServerChatEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.AdvancementEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public class NeoForgeEventHandler {
    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        Events.onPlayerJoin(new NeoForgePlayer(event.getEntity()));
    }

    @SubscribeEvent
    public void onPlayerChat(ServerChatEvent event) {
        Events.onChatMessage(
                new NeoForgePlayer(event.getPlayer()),
                event.getMessage().getString(),
                new NeoForgeCancellable(event));
    }

    @SubscribeEvent
    public void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event) {
        Events.onPlayerLeave(new NeoForgePlayer(event.getEntity()));
    }

    @SubscribeEvent
    public void onPlayerDeath(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity instanceof Player player) {
            Events.onPlayerDeath(
                    new NeoForgePlayer(player),
                    player.getCombatTracker().getDeathMessage().getString());
        }
    }

    @SubscribeEvent
    public void onCommand(CommandEvent event) {
        CommandSourceStack s = event.getParseResults().getContext().getSource();
        String cmd = event.getParseResults().getReader().getString();

        if (s.getEntity() == null) {
            Events.onServerCommand(cmd);
        } else if (s.getEntity() instanceof ServerPlayer player) {
            Events.onPlayerCommand(new NeoForgePlayer(player), "/" + cmd);
        }
    }

    @SubscribeEvent
    public void onPlayerAdvancement(AdvancementEvent.AdvancementEarnEvent event) {
        Advancement adv = event.getAdvancement().value();
        DisplayInfo info = adv.display().orElse(null);

        if (info == null) return;

        Events.onPlayerAchievement(
                AdvancementType.getType(info.getType()),
                new NeoForgePlayer(event.getEntity()),
                adv.name().orElse(Component.literal("")).getString());
    }
}
