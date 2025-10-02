package io.github.dexrnzacattack.rrdiscordbridge.extensions.options;

import com.google.gson.annotations.Expose;

import io.github.dexrnzacattack.rrdiscordbridge.extension.config.options.BaseExtensionOptions;

public class OpChatExtensionOptions extends BaseExtensionOptions {
    @Expose() public boolean nonOpsCanSendMessages = true;
}
