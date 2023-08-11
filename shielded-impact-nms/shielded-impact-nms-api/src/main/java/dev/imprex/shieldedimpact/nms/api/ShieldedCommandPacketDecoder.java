package dev.imprex.shieldedimpact.nms.api;

import org.bukkit.entity.Player;

public interface ShieldedCommandPacketDecoder {

	void inject();

	void uninject();

	Player getPlayer();
}