package io.github.dexrnzacattack.rrdiscordbridge.command;

import java.util.EnumMap;

/**
 * Registry of all in-game commands
 */
public class CommandRegistry {
    /**
     * The command registry
     */
    private final EnumMap<CommandName, ICommand> commands = new EnumMap<>(CommandName.class);

    /**
     * Registers a command
     *
     * @param command The command instance to register
     */
    public CommandRegistry register(ICommand command) {
        commands.put(command.getCommandName(), command);
        return this;
    }

    /**
     * Unregisters a command
     *
     * @param name The enum key (name) of the command to unregister
     */
    public CommandRegistry unregister(CommandName name) {
        commands.remove(name);
        return this;
    }

    /**
     * Gets a command by its enum key
     *
     * @return The command (if found)
     */
    public ICommand getCommand(CommandName name) {
        return commands.get(name);
    }

    /**
     * Enum containing the names of all in-game commands
     */
    public enum CommandName {
        CEXT("cext"),
        DCBROADCAST("dcbroadcast"),
        DISCORD("discord"),
        RELOADCONFIG("reloadrdbconfig");

        private final String name;

        CommandName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
