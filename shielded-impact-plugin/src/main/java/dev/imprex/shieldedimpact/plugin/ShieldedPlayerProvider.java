package dev.imprex.shieldedimpact.plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import dev.imprex.shieldedimpact.api.ShieldedPlayer;

public class ShieldedPlayerProvider {

	private final ShieldedImpactPlugin plugin;

	private final Map<Player, ShieldedPlayer> loadedPlayer = new HashMap<>();
	private final Map<Player, CompletableFuture<ShieldedPlayer>> loadingPlayer = new HashMap<>();

	private final ReadWriteLock lock = new ReentrantReadWriteLock();

	public ShieldedPlayerProvider(ShieldedImpactPlugin plugin) {
		this.plugin = plugin;
	}

	private void supplyPlayer(ShieldedPlayer player) {
		this.lock.writeLock().lock();
		try {
			this.loadedPlayer.put(player.getPlayer(), player);
			CompletableFuture<ShieldedPlayer> completableFuture = this.loadingPlayer.remove(player.getPlayer());
			completableFuture.complete(player);
		} finally {
			this.lock.writeLock().unlock();
		}
	}

	public CompletableFuture<ShieldedPlayer> getPlayer(Player player) {
		this.lock.readLock().lock();
		try {
			ShieldedPlayer loadedPlayer = this.loadedPlayer.get(player);
			if (loadedPlayer != null) {
				return CompletableFuture.supplyAsync(() -> loadedPlayer);
			}

			CompletableFuture<ShieldedPlayer> completableFuture = this.loadingPlayer.get(player);
			if (completableFuture != null) {
				return completableFuture;
			}
		} finally {
			this.lock.readLock().unlock();
		}

		this.lock.writeLock().lock();
		try {
			CompletableFuture<ShieldedPlayer> completableFuture = new CompletableFuture<>();
			this.loadingPlayer.put(player, completableFuture);

			Bukkit.getScheduler().runTaskLater(this.plugin, () -> this.supplyPlayer(new ShieldedPlayerInstance(this.plugin, player)), 20 * 5);

			return completableFuture;
		} finally {
			this.lock.writeLock().unlock();
		}
	}

	public ShieldedPlayer getPlayerIfLoaded(Player player) {
		this.lock.readLock().lock();
		try {
			return this.loadedPlayer.get(player);
		} finally {
			this.lock.readLock().unlock();
		}
	}
}