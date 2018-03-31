package com.techjar.vivecraftforge.network.packet;

import com.techjar.vivecraftforge.network.IPacket;
import com.techjar.vivecraftforge.util.PlayerTracker;
import com.techjar.vivecraftforge.util.VRPlayerData;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayerMP;

public class PacketDraw implements IPacket {
	public float drawDist;

	public PacketDraw() {
	}

	public PacketDraw(float drawDist) {
		this.drawDist = drawDist;
	}

	@Override
	public void encodePacket(ChannelHandlerContext context, ByteBuf buffer) {
		buffer.writeFloat(drawDist);
	}

	@Override
	public void decodePacket(ChannelHandlerContext context, ByteBuf buffer) {
		drawDist = buffer.readFloat();
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
				data.bowDraw = drawDist;
			}
		});
	}
}
