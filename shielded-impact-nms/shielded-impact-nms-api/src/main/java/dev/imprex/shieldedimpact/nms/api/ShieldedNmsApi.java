package dev.imprex.shieldedimpact.nms.api;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.mojang.brigadier.CommandDispatcher;

public interface ShieldedNmsApi {

	ShieldedCommandPacketDecoder getCommandPacketDecoder(CommandDispatcher<CommandSender> dispatcher, Player player);

	void sendFailedCommand(CommandSender sender, String label, Exception exception);
}