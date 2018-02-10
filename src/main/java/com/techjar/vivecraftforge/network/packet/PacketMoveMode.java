package com.techjar.vivecraftforge.network.packet;

import com.techjar.vivecraftforge.network.IPacket;
import com.techjar.vivecraftforge.util.PlayerTracker;
import com.techjar.vivecraftforge.util.VRPlayerData;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayerMP;

public class PacketMoveMode implements IPacket {
	public boolean freeMove;

	public PacketMoveMode() {
	}

	public PacketMoveMode(boolean freeMove) {
		this.freeMove = freeMove;
	}

	@Override
	public void encodePacket(ChannelHandlerContext context, ByteBuf buffer) {
		buffer.writeBoolean(freeMove);
	}

	@Override
	public void decodePacket(ChannelHandlerContext context, ByteBuf buffer) {
		freeMove = buffer.readBoolean();
	}

	@Override
	public void handleClient(EntityPlayerSP player) {
	}

	@Override
	public void handleServer(EntityPlayerMP player) {
		VRPlayerData data = PlayerTracker.getPlayerData(player, true);
		data.freeMove = freeMove;
	}
}
