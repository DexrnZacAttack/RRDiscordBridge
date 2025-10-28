package me.dexrn.rrdiscordbridge.impls.logging;

import me.dexrn.rrdiscordbridge.interfaces.ILogger;

import org.apache.logging.log4j.Logger;

import java.util.logging.Handler;

/** Log4J logger */
public class Log4JLogger implements ILogger {
    private final Logger logger;

    public Log4JLogger(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void info(String message, Object... fmt) {
        logger.info(message, fmt);
    }

    @Override
    public void warn(String message, Object... fmt) {
        logger.warn(message, fmt);
    }

    @Override
    public void error(String message, Object... fmt) {
        logger.error(message, fmt);
    }

    @Override
    public void addHandler(Handler handler) {}

    @Override
    public Handler[] getHandlers() {
        return new Handler[0];
    }

    @Override
    public void removeHandler(Handler handler) {}
}
