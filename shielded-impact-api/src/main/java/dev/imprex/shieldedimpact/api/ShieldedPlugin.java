package dev.imprex.shieldedimpact.api;

import dev.imprex.shieldedimpact.api.l10n.ShieldedLanguage;
import dev.imprex.shieldedimpact.nms.api.ShieldedNmsApi;

public interface ShieldedPlugin {

	ShieldedNmsApi getNms();

	boolean isNmsAvailable();

	ShieldedLanguage getLanguage();

	ShieldedRegistry getShieldRegistry();

	ShieldedPlayerProvider getPlayerProvider();
}
