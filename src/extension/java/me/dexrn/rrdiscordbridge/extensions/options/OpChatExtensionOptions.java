package me.dexrn.rrdiscordbridge.extensions.options;

import com.google.gson.annotations.Expose;

import me.dexrn.rrdiscordbridge.extension.config.options.AbstractExtensionOptions;

public class OpChatExtensionOptions extends AbstractExtensionOptions {
    /** The channel ID for the extension to send and receive opchat messages in */
    @Expose() public String channelId = "";

    /** Allows users who are not Operator to send messages in OpChat */
    @Expose() public boolean nonOpsCanSendMessages = true;
}
