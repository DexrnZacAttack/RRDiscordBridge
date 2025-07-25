package io.github.dexrnzacattack.rrdiscordbridge.mixins;

import io.github.dexrnzacattack.rrdiscordbridge.events.AdvancementAwardEventTrails;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerAdvancements.class)
public class OnTrailsAdvancementAwardMixin {
    @Shadow private ServerPlayer player;

    @Inject(
            method =
                    "lambda$award$2(Lnet/minecraft/advancements/AdvancementHolder;Lnet/minecraft/advancements/DisplayInfo;)V",
            at =
                    @At(
                            value = "INVOKE",
                            target =
                                    "Lnet/minecraft/server/players/PlayerList;broadcastSystemMessage(Lnet/minecraft/network/chat/Component;Z)V"))
    private void onAdvancementAward(
            AdvancementHolder advancementHolder, DisplayInfo displayInfo, CallbackInfo ci) {
        AdvancementAwardEventTrails.EVENT
                .invoker()
                .onAdvancementAward(this.player, advancementHolder, displayInfo);
    }
}
