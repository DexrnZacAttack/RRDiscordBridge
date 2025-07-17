package io.github.dexrnzacattack.rrdiscordbridge.mixins;

import io.github.dexrnzacattack.rrdiscordbridge.Events;
import io.github.dexrnzacattack.rrdiscordbridge.impls.FabricPlayer;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public class OnDeathMixin {
    @Inject(method = "die", at = @At("TAIL"))
    private void onDeath(DamageSource cause, CallbackInfo ci) {
        Events.onPlayerDeath(
                new FabricPlayer((ServerPlayer) (Object) this),
                cause.getLocalizedDeathMessage((ServerPlayer) (Object) this).getString());
    }
}
