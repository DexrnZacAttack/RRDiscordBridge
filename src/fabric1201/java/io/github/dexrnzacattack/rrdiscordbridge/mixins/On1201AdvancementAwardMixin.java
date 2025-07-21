package io.github.dexrnzacattack.rrdiscordbridge.mixins;

import com.moulberry.mixinconstraints.annotations.IfMinecraftVersion;

import io.github.dexrnzacattack.rrdiscordbridge.events.AdvancementAwardEvent1201;

import net.minecraft.advancements.Advancement;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerAdvancements.class)
public class On1201AdvancementAwardMixin {
    @Shadow private ServerPlayer player;

    @IfMinecraftVersion(maxVersion = "1.20.1")
    @Inject(
            method = "award",
            at =
                    @At(
                            value = "INVOKE",
                            target =
                                    "Lnet/minecraft/server/players/PlayerList;broadcastMessage(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/ChatType;Ljava/util/UUID;)V"))
    private void onAdvancementAward(
            Advancement advancement, String criterionKey, CallbackInfoReturnable<Boolean> cir) {
        AdvancementAwardEvent1201.EVENT.invoker().onAdvancementAward(this.player, advancement);
    }
}
