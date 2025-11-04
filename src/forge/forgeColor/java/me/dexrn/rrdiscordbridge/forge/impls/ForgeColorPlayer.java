package me.dexrn.rrdiscordbridge.forge.impls;

import me.dexrn.rrdiscordbridge.interfaces.IPlayer;
import me.dexrn.rrdiscordbridge.interfaces.IServer;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;

import java.util.Objects;

public class ForgeColorPlayer implements IPlayer {
    EntityPlayerMP player; // oh no

    public ForgeColorPlayer(EntityPlayerMP player) {
        this.player = player;
    }

    /**
     * @return {@code true} if the player is an operator
     */
    @Override
    public boolean isOperator() {
        return Objects.requireNonNull(player.getServer())
                        .getPlayerList()
                        .getOppedPlayers()
                        .getEntry(player.getGameProfile())
                        .getPermissionLevel()
                > 1;
    }

    /**
     * @return The player's username
     */
    @Override
    public String getName() {
        return player.getName();
    }

    @Override
    public void sendMessage(String message) {
        player.sendMessage(new TextComponentString(message));
    }

    /**
     * @return {@code true} if the player has joined the server before
     */
    @Override
    public boolean hasPlayedBefore() {
        return true;
    }

    @Override
    public IServer getServer() {
        return new ForgeColorServer(player.getServer());
    }
}
