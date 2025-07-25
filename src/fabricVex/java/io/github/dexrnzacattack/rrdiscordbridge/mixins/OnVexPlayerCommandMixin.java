package io.github.dexrnzacattack.rrdiscordbridge.mixins;

import io.github.dexrnzacattack.rrdiscordbridge.events.PlayerCommandEventVex;

import net.minecraft.network.chat.LastSeenMessages;
import net.minecraft.network.protocol.game.ServerboundChatCommandPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(ServerGamePacketListenerImpl.class)
public class OnVexPlayerCommandMixin {
    @Shadow public ServerPlayer player;

    @Inject(
            method = "lambda$handleChatCommand$11",
            at =
                    @At(
                            value = "INVOKE",
                            target =
                                    "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;performChatCommand(Lnet/minecraft/network/protocol/game/ServerboundChatCommandPacket;Lnet/minecraft/network/chat/LastSeenMessages;)V"))
    public void onPlayerCommand(
            ServerboundChatCommandPacket serverboundChatCommandPacket,
            Optional<LastSeenMessages> lastSeenMessages,
            CallbackInfo ci) {
        PlayerCommandEventVex.EVENT
                .invoker()
                .onPlayerCommand(this.player, serverboundChatCommandPacket.command(), ci);
    }
}
