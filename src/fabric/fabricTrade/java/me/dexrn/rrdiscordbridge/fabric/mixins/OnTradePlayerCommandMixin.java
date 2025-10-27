package me.dexrn.rrdiscordbridge.fabric.mixins;

import me.dexrn.rrdiscordbridge.fabric.events.PlayerCommandEvent;

import net.minecraft.network.chat.LastSeenMessages;
import net.minecraft.network.protocol.game.ServerboundChatCommandPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public class OnTradePlayerCommandMixin {
    @Shadow public ServerPlayer player;

    @Inject(
            method = "performChatCommand",
            at =
                    @At(
                            value = "INVOKE",
                            target =
                                    "Lnet/minecraft/commands/Commands;performCommand(Lcom/mojang/brigadier/ParseResults;Ljava/lang/String;)I"))
    public void onPlayerCommand(
            ServerboundChatCommandPacket packet,
            LastSeenMessages lastSeenMessages,
            CallbackInfo ci) {
        PlayerCommandEvent.EVENT.invoker().onPlayerCommand(this.player, packet.command(), ci);
    }
}
