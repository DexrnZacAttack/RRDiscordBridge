package me.dexrn.rrdiscordbridge.neoforge;

import me.dexrn.rrdiscordbridge.Events;
import me.dexrn.rrdiscordbridge.mc.impls.AbstractEventHandler;
import me.dexrn.rrdiscordbridge.neoforge.impls.NeoForgeCancellable;
import me.dexrn.rrdiscordbridge.neoforge.impls.NeoForgePlayer;
import me.dexrn.rrdiscordbridge.neoforge.impls.NeoForgeServer;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.CommandEvent;
import net.neoforged.neoforge.event.ServerChatEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.util.function.Function;

public class NeoForgeEventHandler<S extends NeoForgeServer, P extends NeoForgePlayer>
        extends AbstractEventHandler<S, P, MinecraftServer, ServerPlayer> {
    public NeoForgeEventHandler(
            Function<MinecraftServer, S> server, Function<ServerPlayer, P> player) {
        super(server, player);
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        Events.onPlayerJoin(createPlayer((ServerPlayer) event.getEntity()));
    }

    @SubscribeEvent
    public void onPlayerChat(ServerChatEvent event) {
        Events.onChatMessage(
                createPlayer(event.getPlayer()),
                event.getMessage().getString(),
                new NeoForgeCancellable(event));
    }

    @SubscribeEvent
    public void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event) {
        Events.onPlayerLeave(createPlayer((ServerPlayer) event.getEntity()));
    }

    @SubscribeEvent
    public void onPlayerDeath(LivingDeathEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof ServerPlayer) {
            Events.onPlayerDeath(
                    createPlayer((ServerPlayer) entity),
                    ((ServerPlayer) entity).getCombatTracker().getDeathMessage().getString());
        }
    }

    @SubscribeEvent
    public void onCommand(CommandEvent event) {
        CommandSourceStack s = event.getParseResults().getContext().getSource();
        String cmd = event.getParseResults().getReader().getString();

        Entity entity = s.getEntity();
        if (entity == null) {
            Events.onServerCommand(cmd);
        } else if (entity instanceof ServerPlayer) {
            Events.onPlayerCommand(createPlayer((ServerPlayer) entity), "/" + cmd);
        }
    }
}
