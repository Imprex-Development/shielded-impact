package dev.imprex.shieldedimpact.command;

import java.util.concurrent.ExecutionException;

import org.bukkit.command.CommandSender;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import dev.imprex.shieldedimpact.api.ShieldedPlugin;
import dev.imprex.shieldedimpact.command.command.CommandEnable;

public class CommandRegistry {

	private static final Suggestions SUGGESTION_EMPTY = new SuggestionsBuilder("", 0).build();

	private final ShieldedPlugin plugin;

	private final CommandDispatcher<CommandInfo> dispatcher;
	private final CommandFailedConsumer commandFailedConsumer;

	public CommandRegistry(ShieldedPlugin plugin) {
		this(plugin, new CommandDispatcher<>());
	}

	public CommandRegistry(ShieldedPlugin plugin, CommandDispatcher<CommandInfo> dispatcher) {
		this.plugin = plugin;
		this.dispatcher = dispatcher;

		this.commandFailedConsumer = CommandFailedConsumer.create(this.plugin);

		this.register(CommandEnable.ENABLE);
	}

	public void register(LiteralArgumentBuilder<CommandInfo> argumentBuilder) {
		this.dispatcher.register(argumentBuilder);
	}

	public int executeCommand(CommandInfo sender, String command, String label, boolean stripSlash) {
		StringReader stringReader = new StringReader(command);
		if (stripSlash && stringReader.canRead() && stringReader.peek() == '/') {
			stringReader.skip();
		}

		try {
			return this.getDispatcher().execute(stringReader, sender);
		} catch (Exception e) {
			this.commandFailedConsumer.handle(sender.getSender(), label, e);
		}
		return 0;
	}

	public Suggestions onTabComplete(String[] args, CommandInfo commandInfo) {
		StringReader cursor = new StringReader(String.join(" ", args));
		if (cursor.canRead() && cursor.peek() == '/') {
			cursor.skip();
		}

		try {
			ParseResults<CommandInfo> parseResults = this.dispatcher.parse(cursor, commandInfo);
			Suggestions suggestions = this.dispatcher.getCompletionSuggestions(parseResults).get();
			if (suggestions == null || suggestions.isEmpty()) {
				return SUGGESTION_EMPTY;
			}

			return suggestions;
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			return SUGGESTION_EMPTY;
		}

		return SUGGESTION_EMPTY;
	}

	public Suggestions onTabComplete(String[] args, CommandSender sender) {
		return this.onTabComplete(args, new CommandInfo(this.plugin, sender));
	}

	public CommandDispatcher<CommandInfo> getDispatcher() {
		return this.dispatcher;
	}
}
