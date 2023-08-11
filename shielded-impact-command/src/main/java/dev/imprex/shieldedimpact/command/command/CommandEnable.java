package dev.imprex.shieldedimpact.command.command;

import static dev.imprex.shieldedimpact.command.argument.ArgumentTypes.argument;
import static dev.imprex.shieldedimpact.command.argument.ArgumentTypes.literal;

import org.bukkit.entity.Player;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import dev.imprex.shieldedimpact.api.ShieldedPlugin;
import dev.imprex.shieldedimpact.api.ShieldedReference;
import dev.imprex.shieldedimpact.command.CommandInfo;
import dev.imprex.shieldedimpact.command.argument.ArgumentTypes;

public class CommandEnable {

	public static final LiteralArgumentBuilder<CommandInfo> ENABLE =
			literal("enable")
			.then(
				argument("reference", ArgumentTypes.shieldedReference())
				.executes(CommandEnable::enableParticle)
				.then(
						argument("target", ArgumentTypes.player())
						.executes(CommandEnable::enableParticlePlayer)));

	private static int enableParticle(CommandContext<CommandInfo> context) throws CommandSyntaxException {
		CommandInfo commandInfo = context.getSource();
		ShieldedPlugin plugin = commandInfo.getPlugin();

		ShieldedReference reference = ArgumentTypes.getShieldedReference(context, "reference");
		return 1;
	}

	private static int enableParticlePlayer(CommandContext<CommandInfo> context) throws CommandSyntaxException {
		CommandInfo commandInfo = context.getSource();
		ShieldedPlugin plugin = commandInfo.getPlugin();

		ShieldedReference reference = ArgumentTypes.getShieldedReference(context, "reference");
		Player target = ArgumentTypes.getPlayer(context, "target");
		return 1;
	}
}