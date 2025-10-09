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
    public void info(String message) {
        logger.info(message);
    }

    @Override
    public void warn(String message) {
        logger.warn(message);
    }

    @Override
    public void warn(String message, Throwable ex) {
        logger.warn(message, ex);
    }

    @Override
    public void error(String message) {
        logger.error(message);
    }

    @Override
    public void error(String message, Throwable ex) {
        logger.error(message, ex);
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
