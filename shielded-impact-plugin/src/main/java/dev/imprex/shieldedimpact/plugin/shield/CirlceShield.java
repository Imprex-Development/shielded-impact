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

@ShieldedInfo("circle")
public class CirlceShield extends Shield {

	@ShieldedSetting("particleCircle")
	private Particle particleCircle = Particle.FIREWORKS_SPARK;

	@ShieldedSetting("particleWalk")
	private Particle particleWalk = Particle.FIREWORKS_SPARK;

	@ShieldedSetting("hitParticle")
	private Particle hitParticle = Particle.CLOUD;

	@ShieldedSetting("blockOffsetY")
	private int blockOffsetY = 6;

	private double currentRadius = 0;

	public CirlceShield(ShieldedImpactPlugin plugin, ShieldedConstructor constructor, ShieldedPlayer player) {
		super(plugin, constructor, player);
	}

	@Override
	public void onHit(Location center, Entity target, Location targetLocation) {
		World world = target.getWorld();
		world.spawnParticle(this.hitParticle, targetLocation.clone().add(0, 1, 0), 10, 0, 1, 0, 0);

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

		for (double angle = 0; angle <= Math.PI * 2; angle += 0.75 / this.visualRadius) {
			double x = center.getX() + this.currentRadius * Math.sin(angle);
			double z = center.getZ() + this.currentRadius * Math.cos(angle);

			Block block;
			if (this.player.isFlying()) {
				int floorX = NumberConversions.floor(x);
				int floorY = NumberConversions.floor(center.getY() + 1);
				int floorZ = NumberConversions.floor(z);
				block = ShieldedBlockUtil.getNonPassableBlockY(world, floorX, floorY, floorZ, true, this.blockOffsetY, 0);
			} else {
				block = ShieldedBlockUtil.getNonPassableBlockY(world, x, center.getY(), z, this.blockOffsetY);
			}

			double y = ShieldedBlockUtil.getBlockOffsetY(world, x, block == null ? center.getY() : block.getY() - 1, z);

			world.spawnParticle(this.particleCircle, x, y + 0.25, z, 1, 0, 0, 0, 0);
		}
	}

	@Override
	public void onDestroy(Location center) {
	}
}