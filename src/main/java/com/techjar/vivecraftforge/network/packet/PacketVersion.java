package com.techjar.vivecraftforge.network.packet;

import com.google.common.base.Charsets;
import com.techjar.vivecraftforge.Config;
import com.techjar.vivecraftforge.VivecraftForge;
import com.techjar.vivecraftforge.network.IPacket;
import com.techjar.vivecraftforge.util.MessageFormatter;
import com.techjar.vivecraftforge.util.PlayerTracker;
import com.techjar.vivecraftforge.util.VRPlayerData;
import com.techjar.vivecraftforge.util.LogHelper;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.ByteBufUtils;

/*
 * Why the fuck does the client want a length-prefixed string, but sends
 * a string that's just char bytes with no length prefix? This whole
 * protocol is an awful mess. I didn't write it, so don't blame me.
 */
public class PacketVersion implements IPacket {
	public String message;

	public PacketVersion() {
	}

	public PacketVersion(String message) {
		this.message = message;
	}

	@Override
	public void encodePacket(ChannelHandlerContext context, ByteBuf buffer) {
		ByteBufUtils.writeUTF8String(buffer, message);
	}

	@Override
	public void decodePacket(ChannelHandlerContext context, ByteBuf buffer) {
		byte[] bytes = new byte[buffer.readableBytes()];
		buffer.readBytes(bytes);
		message = new String(bytes, Charsets.UTF_8);
	}

	@Override
	public void handleClient(final EntityPlayerSP player) {
	}

	@Override
	public void handleServer(final EntityPlayerMP player) {
		VivecraftForge.packetPipeline.sendTo(new PacketVersion(VivecraftForge.MOD_NAME + " " + VivecraftForge.MOD_VERSION), player);
		if (!message.contains("NONVR")) {
			LogHelper.info("VR player joined: %s", message);
			VivecraftForge.packetPipeline.sendTo(new PacketRequestData(), player);
			VivecraftForge.packetPipeline.sendTo(new PacketTeleport(), player);
			if (Config.climbeyEnabled) VivecraftForge.packetPipeline.sendTo(new PacketClimbing(Config.blockListMode, Config.blockList), player);
			player.getServerWorld().addScheduledTask(new Runnable() {
				@Override
				public void run() {
					PlayerTracker.players.put(player.getGameProfile().getId(), new VRPlayerData());
				}
			});
			if (Config.enableJoinMessages && !Config.joinMessageVR.isEmpty())
				String[] formats = new MessageFormatter().player(player).format(Config.joinMessageVR).split("\\n");
				for String line : formats {
					player.getServer().getPlayerList().sendMessage(line);
				}
		} else {
			LogHelper.info("Non-VR player joined: %s", message);
			player.getServerWorld().addScheduledTask(new Runnable() {
				@Override
				public void run() {
					PlayerTracker.companionPlayers.add(player.getGameProfile().getId());
				}
			});
			if (Config.enableJoinMessages && !Config.joinMessageCompanion.isEmpty())
				String[] formats = new MessageFormatter().player(player).format(Config.joinMessageCompanion).split("\\n");
				for String line : formats {
					player.getServer().getPlayerList().sendMessage(line);
				}
		}
	}
}
