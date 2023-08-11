package dev.imprex.shieldedimpact.api;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public interface Shielded {

	void onHit(Location center, Entity target, Location targetLocation);

	void onTick(Location center);

	void onDestroy(Location center);

	<Value> void setSetting(ShieldedSettingKey<Value> key, Value value);

	<Value> Value getSetting(ShieldedSettingKey<Value> key);

	boolean hasSetting(ShieldedSettingKey<?> key);

	List<ShieldedSettingKey<?>> getSettingKeys();

	boolean addBypass(Entity entity);

	boolean removeBypass(Entity entity);

	boolean canBypass(Entity entity);

	void setBorderRadius(double radius);

	double getBorderRadius();

	void setVisualRadius(double radius);

	double getVisualRadius();

	int getMoveCount();

	boolean isMoving();

	ShieldedPlayer getPlayer();

	Player getBukkitPlayer();

	ShieldedReference getReference();

	boolean isDestroyed();
}