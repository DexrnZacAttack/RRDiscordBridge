package io.github.dexrnzacattack.rrdiscordbridge.mixins;

import com.moulberry.mixinconstraints.annotations.IfMinecraftVersion;

import io.github.dexrnzacattack.rrdiscordbridge.events.AdvancementAwardEvent;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerAdvancements.class)
public class OnAdvancementAwardMixin {
    @Shadow private ServerPlayer player;

    @IfMinecraftVersion(minVersion = "1.20.2")
    @Inject(method = "award", at = @At("HEAD"))
    private void onAdvancementAward(
            AdvancementHolder advancement,
            String criterionKey,
            CallbackInfoReturnable<Boolean> cir) {
        AdvancementAwardEvent.EVENT.invoker().onAdvancementAward(this.player, advancement);
    }
}
