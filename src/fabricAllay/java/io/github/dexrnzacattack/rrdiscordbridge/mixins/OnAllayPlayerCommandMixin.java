package io.github.dexrnzacattack.rrdiscordbridge.mixins;

import io.github.dexrnzacattack.rrdiscordbridge.events.PlayerCommandEventAllay;

import net.minecraft.network.protocol.game.ServerboundChatCommandPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public class OnAllayPlayerCommandMixin {
    @Shadow public ServerPlayer player;

    @Inject(
            method = "lambda$handleChatCommand$11",
            at =
                    @At(
                            value = "INVOKE",
                            target =
                                    "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;performChatCommand(Lnet/minecraft/network/protocol/game/ServerboundChatCommandPacket;)V"))
    public void onPlayerCommand(
            ServerboundChatCommandPacket serverboundChatCommandPacket, CallbackInfo ci) {
        PlayerCommandEventAllay.EVENT
                .invoker()
                .onPlayerCommand(this.player, serverboundChatCommandPacket.command(), ci);
    }
}
