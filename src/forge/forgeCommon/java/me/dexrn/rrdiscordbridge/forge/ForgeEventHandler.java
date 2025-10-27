package me.dexrn.rrdiscordbridge.forge;

import me.dexrn.rrdiscordbridge.Events;
import me.dexrn.rrdiscordbridge.forge.impls.ForgeCancellable;
import me.dexrn.rrdiscordbridge.forge.impls.ForgePlayer;
import me.dexrn.rrdiscordbridge.forge.impls.ForgeServer;
import me.dexrn.rrdiscordbridge.impls.AbstractEventHandler;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.function.Function;

public class ForgeEventHandler<S extends ForgeServer, P extends ForgePlayer>
        extends AbstractEventHandler<S, P, MinecraftServer, ServerPlayer> {
    public ForgeEventHandler(
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
                new ForgeCancellable(event));
    }

    @SubscribeEvent
    public void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event) {
        Events.onPlayerLeave(createPlayer((ServerPlayer) event.getEntity()));
    }

    @SubscribeEvent
    public void onPlayerDeath(LivingDeathEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player player) {
            Events.onPlayerDeath(
                    createPlayer((ServerPlayer) player),
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
            Events.onPlayerCommand(createPlayer(player), "/" + cmd);
        }
    }
}
