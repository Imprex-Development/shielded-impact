package dev.imprex.shieldedimpact.command;

import org.bukkit.command.CommandSender;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

import dev.imprex.shieldedimpact.api.ShieldedPlugin;

public interface CommandFailedConsumer {

	public static CommandFailedConsumer create(ShieldedPlugin plugin) {
		if (plugin.isNmsAvailable()) {
			return plugin.getNms()::sendFailedCommand;
		}

		// TODO handle non nms available feedback
		return (sender, label, exception) -> {
			if (!(sender instanceof CommandSyntaxException)) {
				sender.sendMessage(exception.getMessage());
			}
		};
	}

	void handle(CommandSender sender, String label, Exception exception);
}