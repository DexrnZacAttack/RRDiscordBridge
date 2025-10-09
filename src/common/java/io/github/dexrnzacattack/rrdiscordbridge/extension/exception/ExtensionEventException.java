package io.github.dexrnzacattack.rrdiscordbridge.extension.exception;

import io.github.dexrnzacattack.rrdiscordbridge.extension.AbstractBridgeExtension;

/** Event exception which gives more details about where the exception came from */
public class ExtensionEventException extends Exception {
    public ExtensionEventException(AbstractBridgeExtension ext, String msg, Throwable ex) {
        super(String.format(
                "Exception thrown in extension '%s'" +
                "\nFor bug reports, please include the following info:" +
                "\n    - Extension: %s (v%s)" +
                "\n    - Developer: %s" +
                "\n === BEGIN EXCEPTION ===" +
                "\n%s",
                ext.getName(), ext.getVersion(), ext.getAuthor(), ext, msg), ex);
    }
}
