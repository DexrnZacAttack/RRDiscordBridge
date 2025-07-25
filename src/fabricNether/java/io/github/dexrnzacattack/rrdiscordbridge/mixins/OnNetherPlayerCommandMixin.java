package io.github.dexrnzacattack.rrdiscordbridge.mixins;

import io.github.dexrnzacattack.rrdiscordbridge.events.PlayerCommandEventNether;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public class OnNetherPlayerCommandMixin {
    @Shadow public ServerPlayer player;

    @Inject(method = "handleCommand", at = @At("TAIL"), cancellable = true)
    public void onPlayerCommand(String command, CallbackInfo ci) {
        PlayerCommandEventNether.EVENT.invoker().onPlayerCommand(this.player, command, ci);
    }
}
