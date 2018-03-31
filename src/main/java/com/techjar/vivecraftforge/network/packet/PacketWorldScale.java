package com.techjar.vivecraftforge.network.packet;

import com.techjar.vivecraftforge.network.IPacket;
import com.techjar.vivecraftforge.util.PlayerTracker;
import com.techjar.vivecraftforge.util.VRPlayerData;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayerMP;

public class PacketWorldScale implements IPacket {
	public float worldScale;

	public PacketWorldScale() {
	}

	@Override
	public void encodePacket(ChannelHandlerContext context, ByteBuf buffer) {
		buffer.writeFloat(worldScale);
	}

	@Override
	public void decodePacket(ChannelHandlerContext context, ByteBuf buffer) {
		worldScale = buffer.readFloat();
	}

	@Override
	public void handleClient(final EntityPlayerSP player) {
	}

	@Override
	public void handleServer(final EntityPlayerMP player) {
		player.getServerWorld().addScheduledTask(new Runnable() {
			@Override
			public void run() {
				VRPlayerData data = PlayerTracker.getPlayerData(player, true);
				data.worldScale = worldScale;
			}
		});
	}
}
