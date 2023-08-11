package dev.imprex.shieldedimpact.plugin;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import dev.imprex.shieldedimpact.api.ShieldedPlugin;
import dev.imprex.shieldedimpact.api.ShieldedRegistry;
import dev.imprex.shieldedimpact.api.l10n.ShieldedLanguage;
import dev.imprex.shieldedimpact.nms.api.ShieldedNmsApi;

public class ShieldedImpactPlugin extends JavaPlugin implements ShieldedPlugin {

	public static final String NAMESPACE_KEY = "shieldedimpact";

	private ShieldedController controller;
	private ShieldedRegistry registry;
	private ShieldedListener listener;

	private ShieldedLanguage language;

	private ShieldedPlayerProvider playerProvider;

	@Override
	public void onLoad() {
		Bukkit.getServicesManager().register(ShieldedPlugin.class, this, this, ServicePriority.Normal);

		this.controller = new ShieldedController(this);
		this.playerProvider = new ShieldedPlayerProvider(this);
		this.listener = new ShieldedListener(this);
	}

	@Override
	public void onEnable() {
		this.controller.start();

		Bukkit.getPluginManager().registerEvents(this.listener, this);
	}

	@Override
	public void onDisable() {
		this.controller.stop();

		HandlerList.unregisterAll(this);
	}

	public ShieldedController getController() {
		return this.controller;
	}

	@Override
	public ShieldedNmsApi getNms() {
		return null;
	}

	@Override
	public boolean isNmsAvailable() {
		return false;
	}

	@Override
	public ShieldedLanguage getLanguage() {
		return this.language;
	}

	@Override
	public ShieldedRegistry getShieldRegistry() {
		return this.registry;
	}

	@Override
	public ShieldedPlayerProvider getPlayerProvider() {
		return this.playerProvider;
	}
}