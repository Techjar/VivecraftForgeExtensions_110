package com.techjar.vivecraftforge.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public interface IPacket {
	void encodePacket(ChannelHandlerContext context, ByteBuf buffer);

	void decodePacket(ChannelHandlerContext context, ByteBuf buffer);

	void handleClient(EntityPlayerSP player);

	void handleServer(EntityPlayerMP player);
}
