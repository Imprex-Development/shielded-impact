package dev.imprex.shieldedimpact.command.l10n.argument;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAccessor;

import dev.imprex.shieldedimpact.api.l10n.ShieldedLanguage;
import dev.imprex.shieldedimpact.api.l10n.ShieldedLanguageArgument;

public abstract sealed class LanguageArgument<T> implements ShieldedLanguageArgument<T>
		permits LanguageArgumentDate, LanguageArgumentLong, LanguageArgumentDouble, LanguageArgumentObject,
		LanguageArgumentPercent, LanguageArgumentPlayer {

	public static LanguageArgument<Long> toLong(long number) {
		return new LanguageArgumentLong(number);
	}

	public static LanguageArgument<Double> toDouble(double number) {
		return new LanguageArgumentDouble(number);
	}

	public static LanguageArgument<Integer> toPercent(int number) {
		return new LanguageArgumentPercent(Math.round((float) 1 / 100 * number));
	}

	public static LanguageArgument<TemporalAccessor> toDateFromEpochMillis(long millis) {
		return new LanguageArgumentDate(ZonedDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.systemDefault()));
	}

	public static LanguageArgument<TemporalAccessor> toDate(TemporalAccessor date) {
		return new LanguageArgumentDate(date);
	}

	public static LanguageArgument<Object> toObject(Object object) {
		return new LanguageArgumentObject(object);
	}

	protected final T value;

	LanguageArgument(T value) {
		this.value = value;
	}

	@Override
	public abstract String format(ShieldedLanguage language);
}