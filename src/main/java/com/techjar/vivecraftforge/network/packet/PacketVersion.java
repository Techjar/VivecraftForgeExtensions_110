package com.techjar.vivecraftforge.network.packet;

import com.google.common.base.Charsets;
import com.techjar.vivecraftforge.VivecraftForge;
import com.techjar.vivecraftforge.network.IPacket;
import com.techjar.vivecraftforge.util.VivecraftForgeLog;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayerMP;

public class PacketVersion implements IPacket {
	public String message;

	public PacketVersion() {
	}

	public PacketVersion(String message) {
		this.message = message;
	}

	@Override
	public void encodePacket(ChannelHandlerContext context, ByteBuf buffer) {
		buffer.writeBytes(message.getBytes(Charsets.UTF_8));
	}

	@Override
	public void decodePacket(ChannelHandlerContext context, ByteBuf buffer) {
		byte[] bytes = new byte[buffer.readableBytes()];
		buffer.readBytes(bytes);
		message = new String(bytes, Charsets.UTF_8);
	}

	@Override
	public void handleClient(EntityPlayerSP player) {
	}

	@Override
	public void handleServer(EntityPlayerMP player) {
		VivecraftForgeLog.info("Player joined: %s", message);
		VivecraftForge.packetPipeline.sendTo(new PacketVersion(VivecraftForge.MOD_NAME + " " + VivecraftForge.MOD_VERSION), player);
		VivecraftForge.packetPipeline.sendTo(new PacketRequestData(), player);
		VivecraftForge.packetPipeline.sendTo(new PacketClimbing(), player);

		// TODO: parse version string
	}
}
