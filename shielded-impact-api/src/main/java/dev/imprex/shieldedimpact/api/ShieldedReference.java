package dev.imprex.shieldedimpact.api;

import java.util.List;

public interface ShieldedReference {

	List<ShieldedSettingKey<?>> getSettingKeys();

	boolean hasSetting(ShieldedSettingKey<?> key);

	Class<? extends Shielded> getShieldedClass();

	ShieldedInfo getShieldedInfo();
}