package me.dexrn.rrdiscordbridge.forge.impls;

import me.dexrn.rrdiscordbridge.interfaces.IPlayer;
import me.dexrn.rrdiscordbridge.interfaces.IServer;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TextComponentString;

import java.util.Objects;

public class ForgeAquaticPlayer implements IPlayer {
    EntityPlayerMP player; // oh no

    public ForgeAquaticPlayer(EntityPlayerMP player) {
        this.player = player;
    }

    /**
     * @return {@code true} if the player is an operator
     */
    @Override
    public boolean isOperator() {
        return Objects.requireNonNull(player.getServer())
                .getPermissionLevel(player.getGameProfile())
                > 1;
    }

    /**
     * @return The player's username
     */
    @Override
    public String getName() {
        return player.getName().getString();
    }

    @Override
    public void sendMessage(String message) {
        player.sendMessage(new TextComponentString(message), ChatType.SYSTEM);
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
        return new ForgeAquaticServer(player.server);
    }
}
