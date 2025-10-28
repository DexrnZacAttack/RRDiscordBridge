package me.dexrn.rrdiscordbridge.interfaces;

import java.util.logging.Handler;

/** Simple Logger wrapper */
public interface ILogger {
    void info(String message, Object... fmt);

    void warn(String message, Object... fmt);

    void error(String message, Object... fmt);

    void addHandler(Handler handler);

    Handler[] getHandlers();

    void removeHandler(Handler handler);
}
