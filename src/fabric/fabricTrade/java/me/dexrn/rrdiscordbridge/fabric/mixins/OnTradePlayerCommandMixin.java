package me.dexrn.rrdiscordbridge.fabric.mixins;

import me.dexrn.rrdiscordbridge.fabric.events.PlayerCommandEvent;

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
            method = "performUnsignedChatCommand",
            at =
                    @At(
                            value = "INVOKE",
                            target =
                                    "Lnet/minecraft/server/MinecraftServer;getCommands()Lnet/minecraft/commands/Commands;"))
    public void onPlayerCommand(String command, CallbackInfo ci) {
        PlayerCommandEvent.EVENT.invoker().onPlayerCommand(this.player, command, ci);
    }
}
