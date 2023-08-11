package dev.imprex.shieldedimpact.plugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import dev.imprex.shieldedimpact.api.Shielded;
import dev.imprex.shieldedimpact.api.ShieldedInfo;
import dev.imprex.shieldedimpact.api.ShieldedPlayer;
import dev.imprex.shieldedimpact.api.ShieldedReference;
import dev.imprex.shieldedimpact.api.ShieldedSetting;
import dev.imprex.shieldedimpact.api.ShieldedSettingKey;

public class ShieldedConstructor implements ShieldedReference {

	private final Class<? extends Shielded> shieldedClass;
	private final ShieldedInfo shieldedInfo;

	private final Constructor<? extends Shielded> constructor;

	private final Map<ShieldedSettingKey<?>, SettingField<?>> settingField = new HashMap<>();
	private final List<ShieldedSettingKey<?>> settingKeyList = new ArrayList<>();

	public ShieldedConstructor(Class<? extends Shielded> shieldClass) {
		Objects.requireNonNull(shieldClass, "Shield class can't be null!");
		this.shieldedClass = shieldClass;

		this.shieldedInfo = this.shieldedClass.getAnnotation(ShieldedInfo.class);
		Objects.requireNonNull(this.shieldedInfo, "Shield class '" + shieldClass.getSimpleName() + "' has no shield info annotation!");

		try {
			this.constructor = this.shieldedClass.getConstructor(ShieldedImpactPlugin.class, ShieldedConstructor.class, ShieldedPlayer.class);
		} catch (NoSuchMethodException | SecurityException e) {
			throw new NullPointerException("Shield class '" + shieldClass.getSimpleName() + "' has no valid constructor!");
		}

		for (Field field : this.shieldedClass.getDeclaredFields()) {
			ShieldedSetting setting = field.getAnnotation(ShieldedSetting.class);
			if (setting == null) {
				continue;
			}

			ShieldedSettingKey<?> settingKey = ShieldedSettingKey.key(ShieldedImpactPlugin.NAMESPACE_KEY, shieldedInfo, setting);
			this.settingField.put(settingKey, new SettingField<>(setting, field));
			this.settingKeyList.add(settingKey);
		}
	}

	@SuppressWarnings("unchecked")
	Shield newInstance(ShieldedImpactPlugin plugin, ShieldedPlayer player) {
		try {
			Shield shield = (Shield) this.constructor.newInstance(plugin, this, player);
			this.settingField.values().forEach(field -> field.setAccessible(shield));

			for (Entry<ShieldedSettingKey<?>, SettingField<?>> entry : this.settingField.entrySet()) {
				ShieldedSettingKey<?> key = entry.getKey();
				SettingField<Object> field = (SettingField<Object>) entry.getValue(); // TODO rework

				Object value = player.getSetting(key);
				if (value != null) {
					field.setField(shield, value);
				}
			}

			return shield;
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public <Value> void setSetting(Shielded shield, ShieldedSettingKey<Value> key, Value value) {
		Objects.requireNonNull(shield, "Shield can't be null!");
		Objects.requireNonNull(key, "ShieldedSetting can't be null!");
		Objects.requireNonNull(value, "T can't be null!");

		SettingField<?> field = this.settingField.get(key);
		if (field == null) {
			throw new IllegalArgumentException(key.getKey() + " is not available in " + shield.getClass().getSimpleName());
		}

		((SettingField<Value>) field).setField(shield, value);
	}

	@SuppressWarnings("unchecked")
	public <Value> Value getSetting(Shielded shield, ShieldedSettingKey<Value> key) {
		Objects.requireNonNull(shield, "Shield can't be null!");
		Objects.requireNonNull(key, "ShieldedSetting can't be null!");

		SettingField<?> field = this.settingField.get(key);
		if (field == null) {
			throw new IllegalArgumentException(key.getKey() + " is not available in " + shield.getClass().getSimpleName());
		}

		return (Value) field.getField(shield);
	}

	@Override
	public List<ShieldedSettingKey<?>> getSettingKeys() {
		return Collections.unmodifiableList(this.settingKeyList);
	}

	@Override
	public boolean hasSetting(ShieldedSettingKey<?> key) {
		return this.settingField.containsKey(key);
	}

	@Override
	public Class<? extends Shielded> getShieldedClass() {
		return this.shieldedClass;
	}

	@Override
	public ShieldedInfo getShieldedInfo() {
		return this.shieldedInfo;
	}

	private class SettingField<T> {

		private final ShieldedSetting setting;

		private final Field field;
		private final Class<?> genericClass;

		private SettingField(ShieldedSetting setting, Field field) {
			Objects.requireNonNull(setting, "ShieldedSetting can't be null!");
			Objects.requireNonNull(field, "Field can't be null!");

			this.setting = setting;
			this.field = field;

			ParameterizedType parameterizedType = (ParameterizedType) field.getType().getGenericSuperclass();
			this.genericClass = (Class<?>) parameterizedType.getActualTypeArguments()[0];
		}

		public void setAccessible(Shielded instance) {
			if (!field.canAccess(instance)) {
				field.setAccessible(true);
			}
		}

		public void validateValue(Object value) {
			Class<?> valueClass = value.getClass();
			if (!valueClass.isAssignableFrom(this.genericClass)) {
				throw new IllegalArgumentException(String.format("%s is not type of %s at %s",
						valueClass.getSimpleName(),
						this.genericClass.getSimpleName(),
						setting.value()
						));
			}
		}

		@SuppressWarnings("unchecked")
		public T getField(Shielded instance) {
			try {
				return (T) field.get(instance);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
				return null;
			}
		}

		public void setField(Shielded instance, T value) {
			this.validateValue(value);

			try {
				field.set(instance, value);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
}