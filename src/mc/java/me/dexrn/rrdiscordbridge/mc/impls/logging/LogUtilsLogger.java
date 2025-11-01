package me.dexrn.rrdiscordbridge.mc.impls.logging;

import com.mojang.logging.LogUtils;

import me.dexrn.rrdiscordbridge.interfaces.ILogger;

import org.slf4j.Logger;

import java.util.logging.Handler;

public class LogUtilsLogger implements ILogger {
    private final Logger logger;

    LogUtilsLogger() {
        logger = LogUtils.getLogger();
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
