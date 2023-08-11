package dev.imprex.shieldedimpact.command;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import dev.imprex.shieldedimpact.api.ShieldedPlugin;
import dev.imprex.shieldedimpact.api.l10n.ShieldedLanguage;
import dev.imprex.shieldedimpact.api.l10n.ShieldedLanguageArgument;

public class CommandInfo {

	private final ShieldedPlugin plugin;
	private final ShieldedLanguage language;

	private final CommandSender sender;

	public CommandInfo(ShieldedPlugin plugin, CommandSender sender) {
		this.plugin = plugin;
		this.sender = sender;

		this.language = this.plugin.getLanguage();
	}

	public boolean hasPermission(String... permissions) {
		for (String permission : permissions) {
			if (this.sender.hasPermission(permission)) {
				return false;
			}
		}
		return true;
	}

	public boolean hasAnyPermission(String... permissions) {
		for (String permission : permissions) {
			if (this.sender.hasPermission(permission)) {
				return true;
			}
		}
		return false;
	}

	public void response(String message, ShieldedLanguageArgument<?>... args) {
		this.sender.sendMessage(this.getMessage(message, args));
	}

	public String getMessage(String message, ShieldedLanguageArgument<?>... args) {
		return this.language.format(message, args);
	}

	public boolean isPlayer() {
		return this.sender instanceof Player;
	}

	public boolean isConsole() {
		return this.sender instanceof ConsoleCommandSender;
	}

	public CommandSender getSender() {
		return this.sender;
	}

	public ShieldedPlugin getPlugin() {
		return this.plugin;
	}
}