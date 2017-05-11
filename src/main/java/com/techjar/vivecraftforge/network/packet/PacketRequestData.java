package com.techjar.vivecraftforge.network.packet;

import com.techjar.vivecraftforge.network.IPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayerMP;

public class PacketRequestData implements IPacket {
	public PacketRequestData() {
	}

	@Override
	public void encodePacket(ChannelHandlerContext context, ByteBuf buffer) {
	}

	@Override
	public void decodePacket(ChannelHandlerContext context, ByteBuf buffer) {
	}

	@Override
	public void handleClient(EntityPlayerSP player) {
	}

	@Override
	public void handleServer(EntityPlayerMP player) {
	}
}
