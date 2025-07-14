package io.github.dexrnzacattack.rrdiscordbridge.command;

import io.github.dexrnzacattack.rrdiscordbridge.interfaces.ICommandCaller;

/**
 * In-game command interface
 */
public interface ICommand {
    CommandRegistry.CommandName getCommandName();

    String getDescription();

    boolean invoke(ICommandCaller caller, String[] params);
}
