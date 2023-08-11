package dev.imprex.shieldedimpact.command;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;

import com.mojang.brigadier.suggestion.Suggestion;

import dev.imprex.shieldedimpact.api.ShieldedPlugin;

public class CommandManager implements CommandExecutor, TabCompleter {

	private static final String COMMAND_PREFIX = "shield";

	private final ShieldedPlugin plugin;
	private final CommandRegistry registry;

	public CommandManager(ShieldedPlugin plugin) {
		this.plugin = plugin;
		this.registry = new CommandRegistry(this.plugin);
	}

	public void initialize() {
		PluginCommand command = Bukkit.getPluginCommand(COMMAND_PREFIX);
		if (command == null) {
			// TODO log command not found
			return;
		}

		command.setExecutor(this);

		if (!this.plugin.isNmsAvailable()) {
			command.setTabCompleter(this);
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		CommandInfo senderInfo = new CommandInfo(this.plugin, sender);
		return this.registry.executeCommand(senderInfo, label, label, false) == 1;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		CommandInfo senderInfo = new CommandInfo(this.plugin, sender);
		return this.registry.onTabComplete(args, senderInfo).getList().stream().map(Suggestion::getText).toList();
	}
}