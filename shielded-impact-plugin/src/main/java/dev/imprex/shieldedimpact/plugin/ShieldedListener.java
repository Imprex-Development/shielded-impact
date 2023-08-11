package dev.imprex.shieldedimpact.plugin;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import dev.imprex.shieldedimpact.api.ShieldedPlayer;

public class ShieldedListener implements Listener {

	private final ShieldedController controller;
	private final ShieldedPlayerProvider playerProvider;

	public ShieldedListener(ShieldedImpactPlugin plugin) {
		this.controller = plugin.getController();
		this.playerProvider = plugin.getPlayerProvider();
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Shield shield = this.controller.getShield(event.getPlayer());
		if (shield != null) {
			shield.onMove();
		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		ShieldedPlayer player = this.playerProvider.getPlayerIfLoaded(event.getPlayer());
		if (player != null) {
			player.disableShield();
		}
	}
}