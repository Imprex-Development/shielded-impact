package dev.imprex.shieldedimpact.plugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import dev.imprex.shieldedimpact.api.Shielded;
import dev.imprex.shieldedimpact.api.ShieldedInfo;

public class ShieldedRegistry implements Shield {

	private static final Map<Class<? extends Shielded>, ShieldedConstructor> SHIELD_LIST = new HashMap<>();

	public static void register(Class<? extends Shielded> shieldClass) {
		if (SHIELD_LIST.containsKey(shieldClass)) {
			throw new IllegalStateException(shieldClass.getSimpleName() + " is already registered!");
		}

		ShieldedConstructor constructor = new ShieldedConstructor(shieldClass);
		SHIELD_LIST.put(shieldClass, constructor);
	}

	public static boolean isRegistered(Class<? extends Shielded> shieldClass) {
		return SHIELD_LIST.containsKey(shieldClass);
	}

	public static boolean isRegistered(ShieldedConstructor constructor) {
		return SHIELD_LIST.containsValue(constructor);
	}

	public static ShieldedConstructor getConstructor(Class<? extends Shielded> shieldClass) {
		return SHIELD_LIST.get(shieldClass);
	}

	public static Class<? extends Shielded> getClassByName(String name) {
		for (ShieldedConstructor shield : SHIELD_LIST.values()) {
			if (shield.getShieldedInfo().value().equalsIgnoreCase(name)) {
				return shield.getShieldedClass();
			}
		}
		return null;
	}

	public static ShieldedConstructor getConstructorByName(String name) {
		for (ShieldedConstructor constructor : SHIELD_LIST.values()) {
			if (constructor.getShieldedInfo().value().equalsIgnoreCase(name)) {
				return constructor;
			}
		}
		return null;
	}

	public static List<ShieldedInfo> getAllRegistered() {
		return SHIELD_LIST.values().stream().map(ShieldedConstructor::getShieldedInfo).collect(Collectors.toUnmodifiableList());
	}
}
