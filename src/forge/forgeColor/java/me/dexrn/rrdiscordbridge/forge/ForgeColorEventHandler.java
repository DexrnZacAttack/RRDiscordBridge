package me.dexrn.rrdiscordbridge.forge;

import me.dexrn.rrdiscordbridge.Events;
import me.dexrn.rrdiscordbridge.forge.impls.ForgeColorCancellable;
import me.dexrn.rrdiscordbridge.interfaces.IPlayer;
import me.dexrn.rrdiscordbridge.interfaces.IServer;
import me.dexrn.rrdiscordbridge.mc.impls.AbstractEventHandler;
import me.dexrn.rrdiscordbridge.mc.impls.vanilla.advancement.AdvancementType;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.util.function.Function;

public class ForgeColorEventHandler<S extends IServer, P extends IPlayer>
        extends AbstractEventHandler<S, P, MinecraftServer, EntityPlayerMP> {
    public ForgeColorEventHandler(
            Function<MinecraftServer, S> server, Function<EntityPlayerMP, P> player) {
        super(server, player);
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        Events.onPlayerJoin(createPlayer((EntityPlayerMP) event.player));
    }

    @SubscribeEvent
    public void onPlayerChat(ServerChatEvent event) {
        Events.onChatMessage(
                createPlayer(event.getPlayer()),
                event.getMessage(),
                new ForgeColorCancellable(event));
    }

    @SubscribeEvent
    public void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event) {
        Events.onPlayerLeave(createPlayer((EntityPlayerMP) event.player));
    }

    @SubscribeEvent
    public void onPlayerDeath(LivingDeathEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof EntityPlayerMP) {
            Events.onPlayerDeath(
                    createPlayer((EntityPlayerMP) entity),
                    ((EntityPlayerMP) entity)
                            .getCombatTracker()
                            .getDeathMessage()
                            .getUnformattedText());
        }
    }

    @SubscribeEvent
    public void onCommand(CommandEvent event) {
        net.minecraft.command.ICommand command = event.getCommand();
        String cmd = command.getName();

        Entity entity = event.getSender().getCommandSenderEntity();
        if (entity == null) {
            Events.onServerCommand(cmd);
        } else if (entity instanceof EntityPlayerMP) {
            Events.onPlayerCommand(createPlayer((EntityPlayerMP) entity), "/" + cmd);
        }
    }

    @SubscribeEvent
    public void onPlayerAdvancement(AdvancementEvent event) {
        Advancement adv = event.getAdvancement();
        DisplayInfo info = adv.getDisplay();

        if (info != null && info.shouldAnnounceToChat()) {
            Events.onPlayerAchievement(
                    AdvancementType.getTypeFromName(info.getFrame().getName()),
                    createPlayer((EntityPlayerMP) event.getEntityPlayer()),
                    info.getTitle().getUnformattedText(),
                    info.getDescription().getUnformattedText());
        }
    }
}
