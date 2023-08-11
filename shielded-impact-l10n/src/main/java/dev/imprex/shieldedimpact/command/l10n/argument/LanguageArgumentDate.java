package dev.imprex.shieldedimpact.command.l10n.argument;

import java.time.temporal.TemporalAccessor;

import dev.imprex.shieldedimpact.api.l10n.ShieldedLanguage;

public final class LanguageArgumentDate extends LanguageArgument<TemporalAccessor> {

	LanguageArgumentDate(TemporalAccessor value) {
		super(value);
	}

	@Override
	public String format(ShieldedLanguage language) {
		return language.dateFormat().format(this.value);
	}
}