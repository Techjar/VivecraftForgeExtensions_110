package com.techjar.vivecraftforge.network.packet;

import com.techjar.vivecraftforge.network.IPacket;
import com.techjar.vivecraftforge.util.PlayerTracker;
import com.techjar.vivecraftforge.util.VRPlayerData;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayerMP;

public class PacketController1Data implements IPacket {
	public boolean handsReversed;
	public float posX;
	public float posY;
	public float posZ;
	public float rotW;
	public float rotX;
	public float rotY;
	public float rotZ;

	public PacketController1Data() {
	}

	public PacketController1Data(boolean handsReversed, float posX, float posY, float posZ, float rotW, float rotX, float rotY, float rotZ) {
		this.handsReversed = handsReversed;
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
		this.rotW = rotW;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
	}

	@Override
	public void encodePacket(ChannelHandlerContext context, ByteBuf buffer) {
		buffer.writeBoolean(handsReversed);
		buffer.writeFloat(posX);
		buffer.writeFloat(posY);
		buffer.writeFloat(posZ);
		buffer.writeFloat(rotW);
		buffer.writeFloat(rotX);
		buffer.writeFloat(rotY);
		buffer.writeFloat(rotZ);
	}

	@Override
	public void decodePacket(ChannelHandlerContext context, ByteBuf buffer) {
		handsReversed = buffer.readBoolean();
		posX = buffer.readFloat();
		posY = buffer.readFloat();
		posZ = buffer.readFloat();
		rotW = buffer.readFloat();
		rotX = buffer.readFloat();
		rotY = buffer.readFloat();
		rotZ = buffer.readFloat();
	}

	@Override
	public void handleClient(EntityPlayerSP player) {
	}

	@Override
	public void handleServer(EntityPlayerMP player) {
		VRPlayerData data = PlayerTracker.getPlayerData(player);
		if (data != null) {
			data.handsReversed = handsReversed;
			VRPlayerData.ObjectInfo info = data.controller1;
			info.posX = posX;
			info.posY = posY;
			info.posZ = posZ;
			info.rotW = rotW;
			info.rotX = rotX;
			info.rotY = rotY;
			info.rotZ = rotZ;
		}
	}
}
