package dev.imprex.shieldedimpact.plugin.shield;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;

import dev.imprex.shieldedimpact.api.ShieldedInfo;
import dev.imprex.shieldedimpact.api.ShieldedPlayer;
import dev.imprex.shieldedimpact.api.ShieldedReference;
import dev.imprex.shieldedimpact.api.ShieldedSetting;
import dev.imprex.shieldedimpact.plugin.Shield;
import dev.imprex.shieldedimpact.plugin.ShieldedConstructor;
import dev.imprex.shieldedimpact.plugin.ShieldedImpactPlugin;

@ShieldedInfo("test")
public class TestShield extends Shield {

	@ShieldedSetting("particle")
	public Particle particle = Particle.HEART;

	public TestShield(ShieldedImpactPlugin plugin, ShieldedConstructor constructor, ShieldedPlayer player) {
		super(plugin, constructor, player);
	}

	@Override
	public void onHit(Location center, Entity target, Location targetLocation) {
	}

	@Override
	public void onTick(Location center) {
		center.getWorld().spawnParticle(this.particle, center, 0, 0, 0, 1, 0);
	}

	@Override
	public void onDestroy(Location center) {
	}
}