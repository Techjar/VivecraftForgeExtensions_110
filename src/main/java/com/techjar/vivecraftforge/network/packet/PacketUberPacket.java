package com.techjar.vivecraftforge.network.packet;

import com.techjar.vivecraftforge.network.IPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.UUID;

public class PacketUberPacket implements IPacket {
	public UUID uuid;
	public PacketHeadData headData;
	public PacketController0Data controller0Data;
	public PacketController1Data controller1Data;

	public PacketUberPacket() {
	}

	public PacketUberPacket(UUID uuid, PacketHeadData headData, PacketController0Data controller0Data, PacketController1Data controller1Data) {
		this.uuid = uuid;
		this.headData = headData;
		this.controller0Data = controller0Data;
		this.controller1Data = controller1Data;
	}

	@Override
	public void encodePacket(ChannelHandlerContext context, ByteBuf buffer) {
		buffer.writeLong(uuid.getMostSignificantBits());
		buffer.writeLong(uuid.getLeastSignificantBits());
		headData.encodePacket(context, buffer);
		controller0Data.encodePacket(context, buffer);
		controller1Data.encodePacket(context, buffer);
	}

	@Override
	public void decodePacket(ChannelHandlerContext context, ByteBuf buffer) {
		uuid = new UUID(buffer.readLong(), buffer.readLong());
		headData = new PacketHeadData();
		headData.decodePacket(context, buffer);
		controller0Data = new PacketController0Data();
		controller0Data.decodePacket(context, buffer);
		controller1Data = new PacketController1Data();
		controller1Data.decodePacket(context, buffer);
	}

	@Override
	public void handleClient(EntityPlayerSP player) {
	}

	@Override
	public void handleServer(EntityPlayerMP player) {
	}
}
