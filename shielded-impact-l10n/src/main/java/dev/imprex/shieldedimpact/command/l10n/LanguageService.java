package dev.imprex.shieldedimpact.command.l10n;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.ResourceBundle;

import dev.imprex.shieldedimpact.api.l10n.PercentFormat;

public class LanguageService {

	private static final String BUNDLE_PATH = "lang.MessageBundle";

	public void copyLanguageFiles(String from, Path to) throws IOException, URISyntaxException {
		Path languageDirectory = Path.of(BUNDLE_PATH);
		languageDirectory = Paths.get(LanguageService.class.getResource("/lang/").toURI());
		
		Files.walk(languageDirectory, 0, FileVisitOption.FOLLOW_LINKS).forEach(System.out::println);
	}

	public Language createLanguage(Locale locale) {
		ResourceBundle resource = ResourceBundle.getBundle(BUNDLE_PATH, locale);
		NumberFormat numberFormat = NumberFormat.getNumberInstance(locale);
		PercentFormat percentFormat = PercentFormat.getPercentInstance(locale);
		DateTimeFormatter dateFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).localizedBy(locale);

		Language language = new Language(locale, resource, dateFormat, numberFormat, percentFormat);
		return language;
	}
}