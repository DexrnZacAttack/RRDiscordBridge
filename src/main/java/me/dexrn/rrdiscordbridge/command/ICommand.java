package me.dexrn.rrdiscordbridge.command;

import me.dexrn.rrdiscordbridge.interfaces.ICommandCaller;

/** In-game command interface */
public interface ICommand {
    /**
     * @return The command enum
     */
    CommandRegistry.CommandName getCommandName();

    /**
     * @return The command description
     */
    String getDescription();

    /**
     * Calls the command
     *
     * @param caller The command caller
     * @param params The command params
     * @return {@code true} if the command ran successfully
     */
    boolean invoke(ICommandCaller caller, String[] params);
}
