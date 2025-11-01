package me.dexrn.rrdiscordbridge.forge.impls;

import me.dexrn.rrdiscordbridge.interfaces.IPlayer;
import me.dexrn.rrdiscordbridge.interfaces.IServer;

import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;

import java.util.Objects;

public class ForgeNetherPlayer implements IPlayer {
    ServerPlayer player;

    public ForgeNetherPlayer(ServerPlayer player) {
        this.player = player;
    }

    /**
     * @return {@code true} if the player is an operator
     */
    @Override
    public boolean isOperator() {
        return Objects.requireNonNull(player.getServer())
                        .getProfilePermissions(player.getGameProfile())
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
        player.displayClientMessage(new TextComponent(message), false);
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
        return new ForgeNetherServer(player.getServer());
    }
}
