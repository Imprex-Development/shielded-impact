package dev.imprex.shieldedimpact.command;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

public class SuggestionUtil {

//	public static final SuggestionProvider<CommandInfo> SUGGEST_COMMAND_ROOT = (context, builder) -> {
//		ShieldCommandDispatcher dispatcher = YoutuberShieldPlugin.getInstance().getCommandDispatcher();
//		Collection<CommandNode<CommandInfo>> children = dispatcher.getDispatcher().getRoot().getChildren();
//
//		return compareSuggest(builder, children.stream().map(child -> child.getName()));
//	};

	public static SuggestionProvider<CommandInfo> suggest(Class<? extends Enum<?>> enumClass) {
		return (context, builder) -> compareSuggest(builder, Stream.of(enumClass.getEnumConstants()).map(value -> value.name()));
	}

	public static SuggestionProvider<CommandInfo> suggest(String... values) {
		return (context, builder) -> compareSuggest(builder, Stream.of(values));
	}

	public static final CompletableFuture<Suggestions> compareSuggest(SuggestionsBuilder builder, Stream<String> values) {
		return compareSuggest(builder, values.toArray(String[]::new));
	}

	public static final CompletableFuture<Suggestions> compareSuggest(SuggestionsBuilder builder, String... values) {
		String input = builder.getRemaining().toLowerCase();
		for (String value : values) {
			String name = value.toLowerCase();
			if (name.startsWith(input) || name.contains(input)) {
				builder.suggest(value.toLowerCase());
			}
		}
		return builder.buildFuture();
	}

	public static final CompletableFuture<Suggestions> compareSuggest(SuggestionsBuilder builder, String input, String prefix, Stream<String> values) {
		return compareSuggest(builder, input, prefix, values.toArray(String[]::new));
	}

	public static final CompletableFuture<Suggestions> compareSuggest(SuggestionsBuilder builder, String input, String prefix, String... values) {
		input = input.toLowerCase();

		for (String value : values) {
			String name = value.toLowerCase();
			if (name.startsWith(input) || name.contains(input)) {
				builder.suggest(prefix + value.toLowerCase());
			}
		}
		return builder.buildFuture();
	}

	public static CompletableFuture<Suggestions> suggest(Stream<String> args, SuggestionsBuilder builder) {
		return suggest(args.toArray(String[]::new), builder);
	}

	/**
	 * @author Mojang
	 */
	public static CompletableFuture<Suggestions> suggest(String[] args, SuggestionsBuilder builder) {
		String remaining = builder.getRemaining().toLowerCase(Locale.ROOT);
		int length = args.length;

		for (int i = 0; i < length; ++i) {
			String var6 = args[i];
			if (matchesSubStr(remaining, var6.toLowerCase(Locale.ROOT))) {
				builder.suggest(var6);
			}
		}

		return builder.buildFuture();
	}

	/**
	 * @author Mojang
	 */
	public static boolean matchesSubStr(String argument, String contains) {
		for (int i = 0; !contains.startsWith(argument, i); ++i) {
			i = contains.indexOf(95, i);
			if (i < 0) {
				return false;
			}
		}

		return true;
	}
}
