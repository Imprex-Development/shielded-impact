package dev.imprex.shieldedimpact.api;

import java.util.concurrent.CompletableFuture;

import org.bukkit.entity.Player;

public interface ShieldedPlayerProvider {

	CompletableFuture<ShieldedPlayer> getPlayer(Player player);
}