package me.dexrn.rrdiscordbridge.impls.logging;

import me.dexrn.rrdiscordbridge.interfaces.ILogger;

import java.util.Date;
import java.util.logging.*;

/** The standard Java Logger */
public class JavaLogger implements ILogger {
    private final java.util.logging.Logger logger;

    public JavaLogger(java.util.logging.Logger logger) {
        this.logger = logger;
        // https://www.logicbig.com/tutorials/core-java-tutorial/logging/customizing-default-format.html

        this.logger.setUseParentHandlers(false);
        ConsoleHandler h = new ConsoleHandler();
        h.setFormatter(
                new SimpleFormatter() {
                    private static final String f = "%1$tF %1$tT [%2$s/%3$s] %4$s %n";

                    @Override
                    public synchronized String format(LogRecord lr) {
                        return String.format(
                                f,
                                new Date(lr.getMillis()),
                                lr.getSourceClassName(),
                                lr.getLevel().getLocalizedName(),
                                lr.getMessage());
                    }
                });
        this.logger.addHandler(h);
    }

    @Override
    public void info(String message, Object... fmt) {
        this.logger.info(String.format(message, fmt));
    }

    @Override
    public void warn(String message, Object... fmt) {
        this.logger.warning(String.format(message, fmt));
    }

    @Override
    public void error(String message, Object... fmt) {
        this.logger.severe(String.format(message, fmt));
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
