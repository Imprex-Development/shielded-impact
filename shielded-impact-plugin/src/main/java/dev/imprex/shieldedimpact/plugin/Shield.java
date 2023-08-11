package dev.imprex.shieldedimpact.plugin;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;

import dev.imprex.shieldedimpact.api.Shielded;
import dev.imprex.shieldedimpact.api.ShieldedPlayer;
import dev.imprex.shieldedimpact.api.ShieldedReference;
import dev.imprex.shieldedimpact.api.ShieldedSettingKey;

public abstract class Shield implements Shielded {

	protected final ShieldedConstructor constructor;
	protected final ShieldedPlayer shieldedPlayer;
	protected final Player player;

	protected List<Entity> bypassShield = new ArrayList<>();

	protected double borderRadius = 5;
	protected double visualRadius = 15;

	private Location cloneLocation = new Location(null, 0, 0, 0);
	private Location playerLocation = new Location(null, 0, 0, 0);
	private Location targetLocation = new Location(null, 0, 0, 0);

	private int moveCount = 0;
	private int minMoveCount = 10;

	private boolean destroyed = false;

	public Shield(ShieldedImpactPlugin plugin, ShieldedConstructor constructor, ShieldedPlayer shieldedPlayer) {
		this.constructor = constructor;
		this.shieldedPlayer = shieldedPlayer;
		this.player = this.shieldedPlayer.getPlayer();

		this.playerLocation = this.player.getLocation();
		this.onMove();
	}

	void onTick() {
		if (this.moveCount > 0) {
			this.moveCount--;
		}

		this.cloneLocation(this.playerLocation, this.cloneLocation);
		this.onTick(this.cloneLocation);
	}

	void onMove() {
		this.cloneLocation(this.playerLocation, this.cloneLocation);
		this.player.getLocation(this.playerLocation);

		if (this.playerLocation.distance(this.cloneLocation) > 0.001 && this.moveCount < 15) {
			this.moveCount += 2;
		}
	}

	void onTargetMove(Entity target) {
		target.getLocation(this.targetLocation);

		if (this.playerLocation.distance(this.targetLocation) < this.borderRadius) {
			this.onHit(this.playerLocation, target, this.targetLocation);
		}
	}

	void destroy() {
		if (this.destroyed) {
			return;
		}
		this.destroyed = true;

		this.cloneLocation(this.playerLocation, this.cloneLocation);
		this.onDestroy(this.cloneLocation);

		this.cloneLocation = null;
		this.playerLocation = null;
		this.targetLocation = null;
	}

	public Location cloneLocation(Location from, Location to) {
		if (from.equals(to)) {
			return to;
		}

		to.setWorld(from.getWorld());
		to.setX(from.getX());
		to.setY(from.getY());
		to.setZ(from.getZ());
		to.setYaw(from.getYaw());
		to.setPitch(from.getPitch());
		return to;
	}

	@Override
	public <Value> void setSetting(ShieldedSettingKey<Value> key, Value value) {
		this.constructor.setSetting(this, key, value);
	}

	@Override
	public <Value> Value getSetting(ShieldedSettingKey<Value> key) {
		return this.constructor.getSetting(this, key);
	}

	@Override
	public boolean hasSetting(ShieldedSettingKey<?> key) {
		return this.constructor.hasSetting(key);
	}

	@Override
	public List<ShieldedSettingKey<?>> getSettingKeys() {
		return this.constructor.getSettingKeys();
	}

	@Override
	public boolean addBypass(Entity entity) {
		if (this.bypassShield.contains(entity)) {
			return false;
		}
		this.bypassShield.add(entity);
		return true;
	}

	@Override
	public boolean removeBypass(Entity entity) {
		return this.removeBypass(entity);
	}

	@Override
	public boolean canBypass(Entity entity) {
		if (this.player.equals(entity) || this.bypassShield.contains(entity)) {
			return true;
		}

		if (entity instanceof Projectile projectile
				&& projectile.getShooter().equals(this.player)) {
			return true;
		}

		return !(entity instanceof Player);
	}

	@Override
	public void setBorderRadius(double radius) {
		this.borderRadius = radius;
	}

	@Override
	public double getBorderRadius() {
		return this.borderRadius;
	}

	@Override
	public void setVisualRadius(double radius) {
		this.visualRadius = radius;
	}

	@Override
	public double getVisualRadius() {
		return this.visualRadius;
	}

	@Override
	public int getMoveCount() {
		return this.moveCount;
	}

	@Override
	public boolean isMoving() {
		return this.moveCount > this.minMoveCount;
	}

	@Override
	public ShieldedPlayer getPlayer() {
		return this.shieldedPlayer;
	}

	@Override
	public Player getBukkitPlayer() {
		return this.player;
	}

	@Override
	public ShieldedReference getReference() {
		return this.constructor;
	}

	@Override
	public boolean isDestroyed() {
		return this.destroyed;
	}
}