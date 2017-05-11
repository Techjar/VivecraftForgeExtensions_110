package com.techjar.vivecraftforge.network.packet;

import com.google.common.base.Throwables;
import com.techjar.vivecraftforge.network.IPacket;
import com.techjar.vivecraftforge.util.BlockListMode;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayerMP;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/*
 * For whatever reason this uses a serializer instead of just
 * packing the data into the buffer.
 */
public class PacketClimbing implements IPacket {
	public BlockListMode blockListMode;
	public ArrayList<String> blockList;

	public PacketClimbing() {
	}

	public PacketClimbing(BlockListMode blockListMode, ArrayList<String> blockList) {
		this.blockListMode = blockListMode;
		this.blockList = blockList;
	}

	@Override
	public void encodePacket(ChannelHandlerContext context, ByteBuf buffer) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream stream = new ObjectOutputStream(baos);
			stream.writeByte((byte)blockListMode.ordinal());
			stream.writeObject(blockList);
			stream.flush();
			buffer.writeBytes(baos.toByteArray());
			stream.close();
		} catch (IOException ex) {
			Throwables.propagate(ex);
		}
	}

	@Override
	public void decodePacket(ChannelHandlerContext context, ByteBuf buffer) {
	}

	@Override
	public void handleClient(EntityPlayerSP player) {
	}

	@Override
	public void handleServer(EntityPlayerMP player) {
		player.fallDistance = 0;
	}
}
