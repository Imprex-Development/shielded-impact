package dev.imprex.shieldedimpact.plugin.shield;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import de.ngloader.youtubershield.ShieldConstructor;
import de.ngloader.youtubershield.ShieldInfo;
import de.ngloader.youtubershield.YoutuberShieldPlugin;
import dev.imprex.shieldedimpact.api.ShieldedInfo;
import dev.imprex.shieldedimpact.api.ShieldedPlayer;
import dev.imprex.shieldedimpact.api.ShieldedSetting;
import dev.imprex.shieldedimpact.plugin.Shield;
import dev.imprex.shieldedimpact.plugin.ShieldedConstructor;
import dev.imprex.shieldedimpact.plugin.ShieldedImpactPlugin;

@ShieldedInfo("sphere")
public class SphereShield extends Shield {

	@ShieldedSetting("hitParticle")
	private Particle particleSphere = Particle.FIREWORKS_SPARK;

	@ShieldedSetting("hitParticle")
	private Particle particleWalk = Particle.FIREWORKS_SPARK;

	@ShieldedSetting("hitParticle")
	private Particle hitParticle = Particle.CLOUD;

	private double currentRadius = 0;

	private double angleIncrement = 0.2;

	private double phi = 0;
	private boolean phiUp = true;

	public SphereShield(ShieldedImpactPlugin plugin, ShieldedConstructor constructor, ShieldedPlayer player) {
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

		this.phi = this.phi + (this.phiUp ? this.angleIncrement : -this.angleIncrement);
		if (this.phi >= Math.PI * 2) {
			this.phi = 0;
		}

		for (double angle = 0; angle <= Math.PI; angle += this.angleIncrement) {
			double x = center.getX() + this.currentRadius * Math.sin(angle) * Math.cos(this.phi);
			double y = center.getY() + this.currentRadius * Math.cos(angle);
			double z = center.getZ() + this.currentRadius * Math.sin(angle) * Math.sin(this.phi);

			world.spawnParticle(this.particleSphere, x, y, z, 1, 0, 0, 0, 0);
		}
	}

	@Override
	public void onDestroy(Location center) {
	}
}