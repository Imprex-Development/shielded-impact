package dev.imprex.shieldedimpact.plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import dev.imprex.shieldedimpact.api.Shielded;
import dev.imprex.shieldedimpact.api.ShieldedPlayer;

public class ShieldedController implements Runnable {

	private final ShieldedImpactPlugin plugin;

	private final List<Shield> shieldList = new ArrayList<>();
	private final Map<Player, Shield> shieldByPlayer = new HashMap<>();

	private BukkitTask task = null;

	public ShieldedController(ShieldedImpactPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public void run() {
		for (Shield shield : this.shieldList) {
			shield.onTick();

			for (Entity target : shield.getBukkitPlayer().getWorld().getEntities()) {
				if (shield.canBypass(target)) {
					continue;
				}

				shield.onTargetMove(target);
			}
		}
	}

	public void start() {
		this.stop();

		this.task = Bukkit.getScheduler().runTaskTimer(this.plugin, this, 0, 1);
	}

	public void stop() {
		if (this.task != null && !this.task.isCancelled()) {
			this.task.cancel();
			this.task = null;
		}
	}

	public void enableShield(ShieldedPlayer player, Class<? extends Shielded> shieldClass) {
		this.enableShield(player, ShieldedRegistry.getConstructor(shieldClass));
	}

	public void enableShield(ShieldedPlayer player, ShieldedConstructor constructor) {
		Objects.requireNonNull(player, "Player can't be null!");
		Objects.requireNonNull(constructor, "ShieldConstructor can't be null!");

		this.disableShield(player.getCurrentShield());

		Shield shield = constructor.newInstance(this.plugin, player);
		if (shield != null) {
			this.shieldByPlayer.put(player.getPlayer(), shield);
			this.shieldList.add(shield);
		}
	}

	public boolean disableShield(ShieldedPlayer player) {
		return this.disableShield(this.getShield(player));
	}

	public boolean disableShield(Shielded shield) {
		if (shield == null) {
			return false;
		}

		this.shieldList.remove(shield);
		this.shieldByPlayer.remove(shield.getBukkitPlayer());

		if (shield instanceof Shield shieldedInstance) {
			shieldedInstance.destroy();
		}
		return true;
	}

	public Shield getShield(ShieldedPlayer player) {
		return this.getShield(player.getPlayer());
	}

	public Shield getShield(Player player) {
		return this.shieldByPlayer.get(player);
	}

	public boolean isRunning() {
		return this.task != null && !this.task.isCancelled();
	}
}