package me.dexrn.rrdiscordbridge.interfaces;

import java.util.logging.Handler;

/** Simple Logger wrapper */
public interface ILogger {
    void info(String message);

    void warn(String message);

    void warn(String message, Throwable ex);

    void error(String message);

    void error(String message, Throwable ex);

    void addHandler(Handler handler);

    Handler[] getHandlers();

    void removeHandler(Handler handler);
}
