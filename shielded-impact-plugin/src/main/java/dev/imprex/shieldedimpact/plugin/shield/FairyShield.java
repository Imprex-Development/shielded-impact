package dev.imprex.shieldedimpact.plugin.shield;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import dev.imprex.shieldedimpact.api.ShieldedInfo;
import dev.imprex.shieldedimpact.api.ShieldedPlayer;
import dev.imprex.shieldedimpact.api.ShieldedSetting;
import dev.imprex.shieldedimpact.plugin.Shield;
import dev.imprex.shieldedimpact.plugin.ShieldedConstructor;
import dev.imprex.shieldedimpact.plugin.ShieldedImpactPlugin;

@ShieldedInfo("fairy")
public class FairyShield extends Shield {

	@ShieldedSetting("hitParticle")
	private Particle fairyPaticle = Particle.END_ROD;

	@ShieldedSetting("hitParticle")
	private Particle fairyTrailPaticle = Particle.ASH;

	@ShieldedSetting("hitParticle")
	private Particle hitPaticle = Particle.ELECTRIC_SPARK;

	@ShieldedSetting("hitParticle")
	private Particle destoryPaticle = Particle.EXPLOSION_NORMAL;

	private Random random = new Random();

	private Location currentLocation;
	private Location targetLocation;
	private Vector targetVector = new Vector(1, 0, 0);

	private double noMoveTime = 0;
	private double movementSpeed = 0.1;

	private int hitDelay = 0;

	public FairyShield(ShieldedImpactPlugin plugin, ShieldedConstructor constructor, ShieldedPlayer player) {
		super(plugin, constructor, player);
	}

	@Override
	public void onHit(Location center, Entity target, Location targetLocation) {
		this.noMoveTime = 0;
		this.targetLocation = (target instanceof Player player ? player.getEyeLocation() : target.getLocation().add(0, 0.75, 0)).clone().subtract(0, 0.5, 0);
		this.targetVector = this.targetLocation.toVector().subtract(this.currentLocation.toVector()).normalize();
		this.movementSpeed = this.movementSpeed * 1.2;
		this.hitDelay = 20 * 2;

		World world = target.getWorld();
		world.spawnParticle(this.hitPaticle, targetLocation.clone().add(0, 1, 0), 10, 0, 0.5, 0, 0);

		target.setVelocity(targetLocation.toVector().subtract(center.toVector()).multiply(0.15D).setY(0.2));
	}

	@Override
	public void onTick(Location center) {
		if (this.currentLocation == null
				|| this.currentLocation.getWorld() != center.getWorld()) {
			this.currentLocation = center.clone();
			this.newTarget();
		}

		if (this.hitDelay > 0) {
			this.hitDelay--;
		}

		if (this.noMoveTime > 0) {
			this.noMoveTime--;
		} else if (this.random.nextDouble() > .98) {
			this.noMoveTime = this.random.nextDouble() * (20 * 5);
		}

		double distanceToPlayer = this.currentLocation.distance(this.player.getLocation());
		double distanceToTarget = this.currentLocation.distance(this.targetLocation);

		if (this.hitDelay > 0) {
			this.movementSpeed = 2;
		} else {
			if (distanceToTarget < 1 || distanceToPlayer > this.visualRadius - 1) {
				this.newTarget();
			}

			if (distanceToPlayer < this.visualRadius) {
				this.movementSpeed = this.noMoveTime > 0
						? Math.max(0, this.movementSpeed - .0075)
						: Math.min(.1, this.movementSpeed + .0075);
			} else {
				this.noMoveTime = 0;
				this.movementSpeed = Math.min(.15 + (distanceToPlayer * .02), this.movementSpeed + .01);
			}
		}

		this.targetVector.add(this.targetLocation.toVector()
						.subtract(this.currentLocation.toVector())
						.multiply(.1));

		if (this.targetVector.length() < 1) {
			this.movementSpeed = this.targetVector.length() * this.movementSpeed;
		}

		this.targetVector.normalize();

		if (distanceToTarget > .1) {
			if (this.hitDelay > 0) {
				this.hitDelay = 0;
				this.movementSpeed = this.movementSpeed / 1.2;
			}

			this.currentLocation.add(this.targetVector.clone().multiply(this.movementSpeed));
		}

		World world = center.getWorld();
		world.spawnParticle(this.fairyPaticle, this.currentLocation, 1, 0, 0, 0, 0);
		world.spawnParticle(this.fairyTrailPaticle, this.currentLocation, 1, 0, 0, 0, 0);
	}

	@Override
	public void onDestroy(Location center) {
		this.currentLocation.getWorld().spawnParticle(this.destoryPaticle, center, 1, 0, 0, 0, 0);
	}

	@SuppressWarnings("deprecation")
	private void newTarget() {
		this.targetLocation = (this.player.isOnGround() ? this.player.getEyeLocation() : this.player.getLocation()).add(
				this.newRandomRange(),
				this.random.nextDouble() * 1.5,
				this.newRandomRange());
	}

	private int newRandomRange() {
		return (int) ((this.random.nextDouble() * (this.visualRadius * 2)) - this.visualRadius);
	}
}