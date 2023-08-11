package dev.imprex.shieldedimpact.plugin.shield;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.util.NumberConversions;

import dev.imprex.shieldedimpact.api.ShieldedInfo;
import dev.imprex.shieldedimpact.api.ShieldedPlayer;
import dev.imprex.shieldedimpact.api.ShieldedSetting;
import dev.imprex.shieldedimpact.common.ShieldedBlockUtil;
import dev.imprex.shieldedimpact.plugin.Shield;
import dev.imprex.shieldedimpact.plugin.ShieldedConstructor;
import dev.imprex.shieldedimpact.plugin.ShieldedImpactPlugin;

@ShieldedInfo("twopoint")
public class TwoPointShield extends Shield {

	@ShieldedSetting("particleOne")
	private Particle particleOne = Particle.FIREWORKS_SPARK;

	@ShieldedSetting("particleTwo")
	private Particle particleTwo = Particle.FIREWORKS_SPARK;

	@ShieldedSetting("particleWalk")
	private Particle particleWalk = Particle.FIREWORKS_SPARK;

	@ShieldedSetting("particleHit")
	private Particle particleHit = Particle.CLOUD;

	@ShieldedSetting("blockOffsetY")
	private int blockOffsetY = 6;

	private double currentRadius = 0;
	private double angle = 0;

	public TwoPointShield(ShieldedImpactPlugin plugin, ShieldedConstructor constructor, ShieldedPlayer player) {
		super(plugin, constructor, player);
	}

	@Override
	public void onHit(Location center, Entity target, Location targetLocation) {
		World world = target.getWorld();
		world.spawnParticle(this.particleHit, targetLocation.clone().add(0, 1, 0), 10, 0, 1, 0, 0);

		target.setVelocity(targetLocation.toVector().subtract(center.toVector()).multiply(0.15D).setY(0.2));
	}

	@Override
	public void onTick(Location center) {
		World world = center.getWorld();

		if (this.isMoving()) {
			this.currentRadius = 0;
			world.spawnParticle(this.particleWalk, center.add(0, 0.25, 0), 1, 0.25, 0, 0.25, 0);
			return;
		}

		if (this.currentRadius < this.visualRadius) {
			this.currentRadius += 0.05;
		} else if (this.currentRadius != this.visualRadius) {
			this.currentRadius = visualRadius;
		}

		double increment = 0.25 / this.visualRadius;
		if (this.angle >= Math.PI * 2) {
			this.angle = 0;
		}

		this.angle += increment;

		this.displayParticle(world, center.getX(), center.getY(), center.getZ(), this.angle);
		this.displayParticle(world, center.getX(), center.getY(), center.getZ(), this.angle - Math.PI);
	}

	public void displayParticle(World world, double x, double y, double z, double angle) {
		double positionX = x + this.currentRadius * Math.sin(angle);
		double positionZ = z + this.currentRadius * Math.cos(angle);

		Block block;
		if (this.player.isFlying()) {
			int floorX = NumberConversions.floor(positionX);
			int floorY = NumberConversions.floor(y + 1);
			int floorZ = NumberConversions.floor(positionZ);
			block = ShieldedBlockUtil.getNonPassableBlockY(world, floorX, floorY, floorZ, true, this.blockOffsetY, 0);
		} else {
			block = ShieldedBlockUtil.getNonPassableBlockY(world, positionX, y, positionZ, this.blockOffsetY);
		}

		double positionY = block == null ? y : block.getY() - 1;
		positionY = ShieldedBlockUtil.getBlockOffsetY(world, positionX, positionY, positionZ);

		world.spawnParticle(this.particleTwo, positionX, positionY + 0.25, positionZ, 1, 0, 0, 0, 0);
	}

	@Override
	public void onDestroy(Location center) {
	}
}