package me.dexrn.rrdiscordbridge.fabric.mixins;

import me.dexrn.rrdiscordbridge.fabric.events.PlayerChatEvent;

import net.minecraft.network.protocol.game.ServerboundChatPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public class OnChatMessageMixin {
    @Shadow public ServerPlayer player;

    @Inject(
            method = "handleChat(Lnet/minecraft/network/protocol/game/ServerboundChatPacket;)V",
            at =
                    @At(
                            value = "INVOKE",
                            target =
                                    "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;filterTextPacket(Ljava/lang/String;Ljava/util/function/Consumer;)V"),
            cancellable = true)
    public void onChatMessage(ServerboundChatPacket packet, CallbackInfo ci) {
        if (packet.getMessage().startsWith("/")) return;

        PlayerChatEvent.EVENT.invoker().onPlayerChat(this.player, packet.getMessage(), ci);
    }
}
