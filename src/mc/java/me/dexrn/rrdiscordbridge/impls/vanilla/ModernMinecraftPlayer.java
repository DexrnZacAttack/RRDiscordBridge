package me.dexrn.rrdiscordbridge.impls.vanilla;

import me.dexrn.rrdiscordbridge.interfaces.IPlayer;
import me.dexrn.rrdiscordbridge.interfaces.IServer;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.Objects;

public class ModernMinecraftPlayer implements IPlayer {
    protected final ServerPlayer player;

    public ModernMinecraftPlayer(ServerPlayer player) {
        this.player = player;
    }

    // TODO: we need to implement permission shit
    @Override
    public boolean isOperator() {
        return Objects.requireNonNull(player.getServer())
                        .getProfilePermissions(player.getGameProfile())
                > 1;
    }

    @Override
    public String getName() {
        return player.getName().getString();
    }

    @Override
    public void sendMessage(String message) {
        player.displayClientMessage(Component.literal(message), false);
    }

    @Override
    public boolean hasPlayedBefore() {
        return true;
    }

    @Override
    public IServer getServer() {
        return new ModernMinecraftServer(player.getServer());
    }
}
