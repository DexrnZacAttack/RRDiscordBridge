package io.github.dexrnzacattack.rrdiscordbridge.mixins;

import io.github.dexrnzacattack.rrdiscordbridge.events.ConsoleCommandEvent;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.dedicated.DedicatedServer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DedicatedServer.class)
public class OnConsoleCommandMixin {

    @Inject(method = "handleConsoleInput", at = @At("TAIL"), cancellable = true)
    public void onConsoleCommand(
            String string, CommandSourceStack commandSourceStack, CallbackInfo ci) {
        ConsoleCommandEvent.EVENT.invoker().onConsoleCommand(string, ci);
    }
}
