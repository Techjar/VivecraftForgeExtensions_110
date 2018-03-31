package com.techjar.vivecraftforge.network.packet;

import com.techjar.vivecraftforge.network.IPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayerMP;

public class PacketTeleport implements IPacket {
	public float posX;
	public float posY;
	public float posZ;

	public PacketTeleport() {
	}

	@Override
	public void encodePacket(ChannelHandlerContext context, ByteBuf buffer) {
	}

	@Override
	public void decodePacket(ChannelHandlerContext context, ByteBuf buffer) {
		posX = buffer.readFloat();
		posY = buffer.readFloat();
		posZ = buffer.readFloat();
	}

	@Override
	public void handleClient(final EntityPlayerSP player) {
	}

	@Override
	public void handleServer(final EntityPlayerMP player) {
		player.getServerWorld().addScheduledTask(new Runnable() {
			@Override
			public void run() {
				player.setLocationAndAngles(posX, posY, posZ, player.rotationYaw, player.rotationPitch);
			}
		});
	}
}
