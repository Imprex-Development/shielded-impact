package dev.imprex.shieldedimpact.command.l10n.argument;

import org.bukkit.entity.Player;

import dev.imprex.shieldedimpact.api.l10n.ShieldedLanguage;

public final class LanguageArgumentPlayer extends LanguageArgument<Player> {

	LanguageArgumentPlayer(Player value) {
		super(value);
	}

	@Override
	public String format(ShieldedLanguage language) {
		return this.value.getDisplayName();
	}
}