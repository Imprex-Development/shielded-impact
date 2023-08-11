package dev.imprex.shieldedimpact.command.argument;

import java.util.concurrent.CompletableFuture;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import dev.imprex.shieldedimpact.command.SuggestionUtil;

class PlayerArgumentType implements ArgumentType<Player> {

	public static PlayerArgumentType playerArg() {
		return new PlayerArgumentType();
	}

	public static Player getPlayer(CommandContext<?> context, String name) {
		return context.getArgument(name, Player.class);
	}

	@Override
	public Player parse(StringReader reader) throws CommandSyntaxException {
		int start = reader.getCursor();
		String input = reader.readString();
		Player player = Bukkit.getPlayerExact(input);
		if (player != null) {
			return player;
		}

		reader.setCursor(start);
		throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownArgument().createWithContext(reader);
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
		return SuggestionUtil.suggest(Bukkit.getOnlinePlayers().stream().map(Player::getName), builder);
	}
}