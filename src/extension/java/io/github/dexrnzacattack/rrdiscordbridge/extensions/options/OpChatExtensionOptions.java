package io.github.dexrnzacattack.rrdiscordbridge.extensions.options;

import com.google.gson.annotations.Expose;

import io.github.dexrnzacattack.rrdiscordbridge.extension.config.options.AbstractExtensionOptions;

public class OpChatExtensionOptions extends AbstractExtensionOptions {
    @Expose() public boolean nonOpsCanSendMessages = true;
}
