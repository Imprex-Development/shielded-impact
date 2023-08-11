package dev.imprex.shieldedimpact.command.argument;

import java.util.concurrent.CompletableFuture;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import dev.imprex.shieldedimpact.api.ShieldedPlugin;
import dev.imprex.shieldedimpact.api.ShieldedReference;
import dev.imprex.shieldedimpact.api.ShieldedRegistry;
import dev.imprex.shieldedimpact.command.SuggestionUtil;

class ShieldedReferenceArgumentType implements ArgumentType<ShieldedReference> {

	private static ShieldedPlugin plugin;

	static {
		RegisteredServiceProvider<ShieldedPlugin> provider = Bukkit.getServicesManager().getRegistration(ShieldedPlugin.class);
		if (provider.getPlugin() != null) {
			plugin = (ShieldedPlugin) provider.getPlugin();
		}
	}

	public static ShieldedReferenceArgumentType shieldReferenceArg() {
		return new ShieldedReferenceArgumentType();
	}

	public static ShieldedReference getShieldReference(CommandContext<?> context, String name) {
		return context.getArgument(name, ShieldedReference.class);
	}

	private final ShieldedRegistry registry;

	public ShieldedReferenceArgumentType() {
		this.registry = plugin.getShieldRegistry();
	}

	@Override
	public ShieldedReference parse(StringReader reader) throws CommandSyntaxException {
		int start = reader.getCursor();
		String input = reader.readString();
		ShieldedReference reference = this.registry.getReference(input);
		if (reference != null) {
			return reference;
		}

		reader.setCursor(start);
		throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownArgument().createWithContext(reader);
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
		return SuggestionUtil.suggest(this.registry.getReferences().stream().map(shield -> shield.getShieldedInfo().value()), builder);
	}
}