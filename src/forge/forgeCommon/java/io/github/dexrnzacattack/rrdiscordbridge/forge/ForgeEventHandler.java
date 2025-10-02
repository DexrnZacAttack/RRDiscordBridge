package io.github.dexrnzacattack.rrdiscordbridge.forge;

import io.github.dexrnzacattack.rrdiscordbridge.Events;
import io.github.dexrnzacattack.rrdiscordbridge.forge.impls.ForgeCancellable;
import io.github.dexrnzacattack.rrdiscordbridge.forge.impls.ForgePlayer;
import io.github.dexrnzacattack.rrdiscordbridge.impls.vanilla.ModernMinecraftCommands;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ForgeEventHandler {
    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        Events.onPlayerJoin(new ForgePlayer((ServerPlayer) event.getEntity()));
    }

    @SubscribeEvent
    public void onPlayerChat(ServerChatEvent event) {
        Events.onChatMessage(
                new ForgePlayer(event.getPlayer()),
                event.getMessage().getString(),
                new ForgeCancellable(event));
    }

    @SubscribeEvent
    public void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event) {
        Events.onPlayerLeave(new ForgePlayer((ServerPlayer) event.getEntity()));
    }

    @SubscribeEvent
    public void onPlayerDeath(LivingDeathEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player player) {
            Events.onPlayerDeath(
                    new ForgePlayer((ServerPlayer) player),
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
            Events.onPlayerCommand(new ForgePlayer(player), "/" + cmd);
        }
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        ModernMinecraftCommands.register(event.getDispatcher());
    }
}
