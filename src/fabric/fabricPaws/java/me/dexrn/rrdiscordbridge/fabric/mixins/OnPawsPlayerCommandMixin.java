package me.dexrn.rrdiscordbridge.fabric.mixins;

import com.mojang.brigadier.ParseResults;

import me.dexrn.rrdiscordbridge.fabric.events.PlayerCommandEvent;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Commands.class)
public class OnPawsPlayerCommandMixin {
    // tbh we should just be hooking into this but in reality this whole system is fucked and we
    // should just be mixing into the actual command instead or maybe not even doing such thing at
    // all
    @Inject(
            method = "performCommand",
            at =
                    @At(
                            value = "INVOKE",
                            target =
                                    "Lnet/minecraft/commands/Commands;executeCommandInContext(Lnet/minecraft/commands/CommandSourceStack;Ljava/util/function/Consumer;)V",
                            shift = At.Shift.AFTER))
    public void onPlayerCommand(
            ParseResults<CommandSourceStack> parseResults, String command, CallbackInfo ci) {
        PlayerCommandEvent.EVENT
                .invoker()
                .onPlayerCommand(parseResults.getContext().getSource().getPlayer(), command, ci);
    }
}
