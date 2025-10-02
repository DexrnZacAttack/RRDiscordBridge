package io.github.dexrnzacattack.rrdiscordbridge.fabric.mixins;

import io.github.dexrnzacattack.rrdiscordbridge.fabric.events.PlayerCommandEventWild;

import net.minecraft.network.protocol.game.ServerboundChatCommandPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public class OnWildPlayerCommandMixin {
    @Shadow public ServerPlayer player;

    @Inject(
            method = "handleChatCommand",
            at =
                    @At(
                            value = "INVOKE",
                            target =
                                    "Lnet/minecraft/server/MinecraftServer;getCommands()Lnet/minecraft/commands/Commands;"))
    public void onPlayerCommand(
            ServerboundChatCommandPacket serverboundChatCommandPacket, CallbackInfo ci) {
        PlayerCommandEventWild.EVENT
                .invoker()
                .onPlayerCommand(this.player, serverboundChatCommandPacket.command(), ci);
    }
}
