package me.dexrn.rrdiscordbridge.forge;

import me.dexrn.rrdiscordbridge.Events;
import me.dexrn.rrdiscordbridge.forge.impls.ForgeCancellable;
import me.dexrn.rrdiscordbridge.forge.impls.ForgePawsPlayer;
import me.dexrn.rrdiscordbridge.forge.impls.ForgePawsServer;
import me.dexrn.rrdiscordbridge.impls.AbstractEventHandler;
import me.dexrn.rrdiscordbridge.impls.vanilla.advancement.AdvancementType;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.function.Function;

// kind of cursed since I had to copy the entire event handler due to the change to mojmap at
// runtime
public class ForgePawsEventHandler<S extends ForgePawsServer, P extends ForgePawsPlayer>
        extends AbstractEventHandler<S, P, MinecraftServer, ServerPlayer> {
    public ForgePawsEventHandler(
            Function<MinecraftServer, S> server, Function<ServerPlayer, P> player) {
        super(server, player);
    }

    @SubscribeEvent
    public void onPlayerAdvancement(AdvancementEvent.AdvancementEarnEvent event) {
        Advancement adv = event.getAdvancement().value();
        DisplayInfo info = adv.display().orElse(null);

        if (info != null && info.shouldAnnounceChat()) {
            Events.onPlayerAchievement(
                    AdvancementType.getType(info.getType()),
                    createPlayer((ServerPlayer) event.getEntity()),
                    adv.name().orElse(Component.literal("")).getString(),
                    info.getDescription().getString());
        }
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
