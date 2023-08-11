package dev.imprex.shieldedimpact.api;

import org.bukkit.entity.Player;

public interface ShieldedPlayer {

	<Value> Value getSetting(ShieldedSettingKey<Value> key);

	<Value> void setSetting(ShieldedSettingKey<Value> key, Value value);

	void enableShield(Class<? extends Shielded> shieldClass);

	void disableShield();

	Shielded getCurrentShield();

	Player getPlayer();
}