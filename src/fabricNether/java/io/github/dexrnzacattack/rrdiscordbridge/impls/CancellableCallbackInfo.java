package io.github.dexrnzacattack.rrdiscordbridge.impls;

import io.github.dexrnzacattack.rrdiscordbridge.interfaces.ICancellable;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class CancellableCallbackInfo implements ICancellable {
    private final CallbackInfo ci;

    public CancellableCallbackInfo(CallbackInfo ci) {
        this.ci = ci;
    }

    @Override
    public void cancel() {
        ci.cancel();
    }

    @Override
    public void uncancel() {}
}
