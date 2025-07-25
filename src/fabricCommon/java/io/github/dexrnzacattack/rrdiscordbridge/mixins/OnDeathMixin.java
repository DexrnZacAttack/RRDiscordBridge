package io.github.dexrnzacattack.rrdiscordbridge.mixins;

import io.github.dexrnzacattack.rrdiscordbridge.events.PlayerDeathEvent;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public class OnDeathMixin {
    @Inject(method = "die", at = @At("HEAD"))
    private void onDeath(DamageSource cause, CallbackInfo ci) {
        ServerPlayer player = (ServerPlayer) (Object) this;
        PlayerDeathEvent.EVENT
                .invoker()
                .onPlayerDeath(player, cause.getLocalizedDeathMessage(player));
    }
}
