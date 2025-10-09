package io.github.dexrnzacattack.rrdiscordbridge.impls.logging;

import io.github.dexrnzacattack.rrdiscordbridge.interfaces.ILogger;

import java.util.logging.Handler;
import java.util.logging.Level;

/** The standard Java Logger */
public class JavaLogger implements ILogger {
    private final java.util.logging.Logger logger;

    public JavaLogger(java.util.logging.Logger logger) {
        this.logger = logger;
    }

    @Override
    public void info(String message) {
        this.logger.info(message);
    }

    @Override
    public void warn(String message) {
        this.logger.warning(message);
    }

    @Override
    public void warn(String message, Throwable ex) {
        this.logger.log(Level.WARNING, message, ex);
    }

    @Override
    public void error(String message) {
        this.logger.severe(message);
    }

    @Override
    public void error(String message, Throwable ex) {
        this.logger.log(Level.SEVERE, message, ex);
    }

    @Override
    public void addHandler(Handler handler) {
        this.logger.addHandler(handler);
    }

    @Override
    public Handler[] getHandlers() {
        return this.logger.getHandlers();
    }

    @Override
    public void removeHandler(Handler handler) {
        this.logger.removeHandler(handler);
    }
}
