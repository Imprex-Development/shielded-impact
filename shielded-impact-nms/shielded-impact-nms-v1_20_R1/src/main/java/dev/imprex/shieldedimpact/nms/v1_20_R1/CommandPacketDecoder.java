package dev.imprex.shieldedimpact.nms.v1_20_R1;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;

import dev.imprex.shieldedimpact.nms.api.ShieldedCommandPacketDecoder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.MessageToMessageDecoder;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundCommandSuggestionsPacket;
import net.minecraft.network.protocol.game.ServerboundCommandSuggestionPacket;

public class CommandPacketDecoder extends MessageToMessageDecoder<Packet<?>> implements ShieldedCommandPacketDecoder {

	private static final String CHANNEL_DECODER_NAME = "shielded-impact-command-decoder";

	private final CommandDispatcher<CommandSender> dispatcher;
	private final Connection connection;
	private final Player player;

	public CommandPacketDecoder(CommandDispatcher<CommandSender> dispatcher, Player player, Connection connection) {
		this.dispatcher = dispatcher;
		this.connection = connection;
		this.player = player;
	}

	@Override
	public void inject() {
		ChannelPipeline pipeline = this.connection.channel.pipeline();
		pipeline.addAfter("decoder", CHANNEL_DECODER_NAME, this);
	}

	@Override
	public void uninject() {
		ChannelPipeline pipeline = this.connection.channel.pipeline();
		pipeline.remove(CHANNEL_DECODER_NAME);
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, Packet<?> msg, List<Object> out) throws Exception {
		if (msg == null) {
			out.add(msg);
			return;
		}

		if (msg instanceof ServerboundCommandSuggestionPacket packet) {
			StringReader cursor = new StringReader(packet.getCommand());
			if (cursor.canRead() && cursor.peek() == '/') {
				cursor.skip();
			}

			ParseResults<CommandSender> result = this.dispatcher.parse(cursor, this.player);
			this.dispatcher.getCompletionSuggestions(result).whenComplete((suggestions, error) -> {
				if (error != null) {
					error.printStackTrace();
					return;
				}

				if (this.player.isOnline() && !suggestions.isEmpty()) {
					this.connection.send(new ClientboundCommandSuggestionsPacket(packet.getId(), suggestions));
				} else {
					String name = packet.getCommand().split(" ")[0].replace("/", "");
					if (this.dispatcher.getRoot().getChild(name) == null) {
						out.add(msg);
					}
				}
			});
		} else {
			out.add(msg);
		}
	}

	@Override
	public Player getPlayer() {
		return this.player;
	}
}