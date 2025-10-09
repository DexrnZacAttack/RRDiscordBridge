package me.dexrn.rrdiscordbridge.extensions.options;

import com.google.gson.annotations.Expose;

import me.dexrn.rrdiscordbridge.extension.config.options.AbstractExtensionOptions;

public class OpChatExtensionOptions extends AbstractExtensionOptions {
    @Expose() public boolean nonOpsCanSendMessages = true;
}
