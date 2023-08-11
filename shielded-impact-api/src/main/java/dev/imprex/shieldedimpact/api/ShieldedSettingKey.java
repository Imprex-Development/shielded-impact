package dev.imprex.shieldedimpact.api;

import java.util.Objects;

import org.bukkit.plugin.Plugin;

public class ShieldedSettingKey<Value> {

	public static <Value> ShieldedSettingKey<Value> key(String namespace, String shielded, String key) {
		return new ShieldedSettingKey<>(namespace, shielded, key);
	}

	public static <Value> ShieldedSettingKey<Value> key(String namespace, ShieldedInfo shielded, ShieldedSetting key) {
		return new ShieldedSettingKey<>(namespace, shielded.value(), key.value());
	}

	public static <Value> ShieldedSettingKey<Value> key(Plugin plugin, ShieldedInfo shielded, ShieldedSetting key) {
		return new ShieldedSettingKey<>(plugin.getName(), shielded.value(), key.value());
	}

	private final String namespace;

	private final String shielded;

	private final String key;

	private ShieldedSettingKey(String namespace, String shielded, String key) {
		this.namespace = namespace;
		this.shielded = shielded;
		this.key = key;
	}

	public String getNamespace() {
		return this.namespace;
	}

	public String getShielded() {
		return this.shielded;
	}

	public String getKey() {
		return this.key;
	}

	@Override
	public int hashCode() {
		return Objects.hash(key, namespace, shielded);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ShieldedSettingKey<?> other = (ShieldedSettingKey<?>) obj;
		return Objects.equals(key, other.key) && Objects.equals(namespace, other.namespace)
				&& Objects.equals(shielded, other.shielded);
	}

	@Override
	public String toString() {
		return this.namespace + ":" + this.shielded + ":" + this.key;
	}
}