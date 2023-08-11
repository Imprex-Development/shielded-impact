package dev.imprex.shieldedimpact.plugin;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

import dev.imprex.shieldedimpact.api.Shielded;
import dev.imprex.shieldedimpact.api.ShieldedPlayer;
import dev.imprex.shieldedimpact.api.ShieldedSettingKey;

public class ShieldedPlayerInstance implements ShieldedPlayer {

	private final ShieldedController controller;
	private final Player player;

	private final Map<ShieldedSettingKey<?>, Object> settingStorage = new HashMap<>();

	public ShieldedPlayerInstance(ShieldedImpactPlugin plugin, Player player) {
		this.controller = plugin.getController();
		this.player = player;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <Value> Value getSetting(ShieldedSettingKey<Value> key) {
		return (Value) this.settingStorage.get(key);
	}

	@Override
	public <Value> void setSetting(ShieldedSettingKey<Value> key, Value value) {
		this.settingStorage.put(key, value);

		Shielded shielded = this.getCurrentShield();
		if (shielded != null && shielded.hasSetting(key)) {
			shielded.setSetting(key, value);
		}
	}

	@Override
	public void enableShield(Class<? extends Shielded> shieldClass) {
		this.controller.enableShield(this, shieldClass);
	}

	@Override
	public void disableShield() {
		this.controller.disableShield(this);
	}

	@Override
	public Shielded getCurrentShield() {
		return this.controller.getShield(this.player);
	}

	@Override
	public Player getPlayer() {
		return this.player;
	}
}