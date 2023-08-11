package dev.imprex.shieldedimpact.nms.v1_20_R1;

import java.lang.reflect.Field;

import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_20_R1.command.VanillaCommandWrapper;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import dev.imprex.shieldedimpact.nms.api.ShieldedCommandPacketDecoder;
import dev.imprex.shieldedimpact.nms.api.ShieldedNmsApi;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

public class ShieldedApi implements ShieldedNmsApi {

	private static final Field FIELD_CONNECTION = getFieldByType(ServerGamePacketListenerImpl.class, Connection.class);

	private static Field getFieldByType(Class<?> clazz, Class<?> type) {
		return getFieldByType(clazz, type, 0);
	}

	private static Field getFieldByType(Class<?> clazz, Class<?> type, int index) {
		int step = 0;
		for (Field field : clazz.getDeclaredFields()) {
			if (field.getType().equals(type)) {
				if (step == index) {
					field.setAccessible(true);
					return field;
				}
				step++;
			}
		}
		return null;
	}

	@Override
	public ShieldedCommandPacketDecoder getCommandPacketDecoder(CommandDispatcher<CommandSender> dispatcher, Player player) {
		try {
			ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
			Connection connection = (Connection) FIELD_CONNECTION.get(serverPlayer.connection);
			return new CommandPacketDecoder(dispatcher, player, connection);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public void sendFailedCommand(CommandSender sender, String label, Exception exception) {
		CommandSourceStack sourceStack = VanillaCommandWrapper.getListener(sender);
		if (exception instanceof CommandSyntaxException syntaxException) {
			Message message = syntaxException.getRawMessage();
			if (message instanceof Component messageComponent) {
				sourceStack.sendFailure(messageComponent);
			} else {
				sourceStack.sendFailure(Component.literal(message.getString()));
			}

			if (syntaxException.getInput() != null && syntaxException.getCursor() >= 0) {
				int length = Math.min(syntaxException.getInput().length(), syntaxException.getCursor());
				MutableComponent component = Component.literal("")
						.withStyle(ChatFormatting.GRAY)
						.withStyle(
								(modifier) -> modifier.withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, label)));

				if (length > 10) {
					component.append("...");
				}

				component.append(syntaxException.getInput().substring(Math.max(0, length - 10), length));
				if (length < syntaxException.getInput().length()) {
					MutableComponent errorComponent = Component.literal(syntaxException.getInput().substring(length))
							.withStyle(new ChatFormatting[] { ChatFormatting.RED, ChatFormatting.UNDERLINE });
					component.append(errorComponent);
				}

				component.append(Component.translatable("command.context.here")
						.withStyle(new ChatFormatting[] { ChatFormatting.RED, ChatFormatting.ITALIC }));

				sourceStack.sendFailure(component);
			}
			return;
		}

		Component component = Component.literal(exception.getMessage() == null ? exception.getClass().getName() : exception.getMessage());
		sourceStack.sendFailure(Component.translatable("command.failed")
				.withStyle(modifier -> modifier.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, component))));
	}
}