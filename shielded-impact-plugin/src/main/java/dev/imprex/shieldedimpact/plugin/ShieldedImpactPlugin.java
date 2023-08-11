package dev.imprex.shieldedimpact.plugin;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import dev.imprex.shieldedimpact.api.ShieldedPlayer;
import dev.imprex.shieldedimpact.api.ShieldedPlugin;
import dev.imprex.shieldedimpact.api.ShieldedRegistry;
import dev.imprex.shieldedimpact.api.ShieldedSettingKey;
import dev.imprex.shieldedimpact.api.l10n.ShieldedLanguage;
import dev.imprex.shieldedimpact.nms.api.ShieldedNmsApi;
import dev.imprex.shieldedimpact.plugin.shield.TestShield;

public class ShieldedImpactPlugin extends JavaPlugin implements ShieldedPlugin {

	public static final String NAMESPACE_KEY = "shieldedimpact";

	private ShieldedController controller;
	private ShieldedPlayerProvider playerProvider;

	private ShieldedListener listener;

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

		getCommand("shield").setExecutor(new CommandExecutor() {
			
			@Override
			public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
				if (!(sender instanceof Player player)) {
					return false;
				}

				if (args.length > 0) {
					ShieldedPlayer shieldedPlayer = playerProvider.getPlayerIfLoaded(player);
					ShieldedSettingKey<Particle> key = ShieldedSettingKey.key(ShieldedImpactPlugin.NAMESPACE_KEY, "test", "particle");
					shieldedPlayer.setSetting(key, Particle.CLOUD);
					return true;
				}

				playerProvider.getPlayer(player).whenComplete((shieldedPlayer, throwable) -> {
					if (throwable != null) {
						throwable.printStackTrace();
						return;
					}

					shieldedPlayer.enableShield(TestShield.class);
				});
				return true;
			}
		});
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
		return null;
	}

	@Override
	public ShieldedRegistry getShieldRegistry() {
		return null;
	}

	@Override
	public ShieldedPlayerProvider getPlayerProvider() {
		return this.playerProvider;
	}
}