package com.techjar.vivecraftforge.network;

import com.techjar.vivecraftforge.util.VivecraftForgeLog;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import net.minecraft.entity.player.EntityPlayerMP;

import com.techjar.vivecraftforge.network.packet.*;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.FMLEmbeddedChannel;
import net.minecraftforge.fml.common.network.FMLIndexedMessageToMessageCodec;
import net.minecraftforge.fml.common.network.FMLOutboundHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import java.util.EnumMap;

public class ChannelHandler extends FMLIndexedMessageToMessageCodec<IPacket> {
	private EnumMap<Side, FMLEmbeddedChannel> channels;
	SimpleNetworkWrapper network;

	private ChannelHandler() {
		this.addDiscriminator(0, PacketVersion.class);
		this.addDiscriminator(1, PacketRequestData.class);
		this.addDiscriminator(2, PacketHeadData.class);
		this.addDiscriminator(3, PacketController0Data.class);
		this.addDiscriminator(4, PacketController1Data.class);
		this.addDiscriminator(5, PacketWorldScale.class);
		this.addDiscriminator(6, PacketDraw.class);
		this.addDiscriminator(7, PacketMoveMode.class);
		this.addDiscriminator(8, PacketUberPacket.class);
		this.addDiscriminator(9, PacketTeleport.class);
		this.addDiscriminator(10, PacketClimbing.class);
	}

	public static ChannelHandler init() {
		ChannelHandler channelHandler = new ChannelHandler();
		channelHandler.channels = NetworkRegistry.INSTANCE.newChannel("Vivecraft", channelHandler, new PacketHandlerServer());
		if (FMLCommonHandler.instance().getSide().isClient()) {
			FMLEmbeddedChannel channel = channelHandler.channels.get(Side.CLIENT);
			String codec = channel.findChannelHandlerNameForType(ChannelHandler.class);
			channel.pipeline().addAfter(codec, "ClientHandler", new PacketHandlerClient());
		}
		channelHandler.network = NetworkRegistry.INSTANCE.newSimpleChannel("Vivecraft2");
		channelHandler.network.
		return channelHandler;
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, IPacket msg, ByteBuf target) throws Exception {
		msg.encodePacket(ctx, target);
		VivecraftForgeLog.info("Encoded packet: %s", msg.getClass().getName());
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf source, IPacket msg) {
		msg.decodePacket(ctx, source);
	}

	public void sendToAll(IPacket message) {
		this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALL);
		this.channels.get(Side.SERVER).writeAndFlush(message);
	}

	public void sendTo(IPacket message, EntityPlayerMP player) {
		this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
		this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player);
		this.channels.get(Side.SERVER).writeAndFlush(message);
	}

	public void sendToAllAround(IPacket message, NetworkRegistry.TargetPoint point) {
		this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT);
		this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(point);
		this.channels.get(Side.SERVER).writeAndFlush(message);
	}

	public void sendToDimension(IPacket message, int dimensionId) {
		this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.DIMENSION);
		this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(dimensionId);
		this.channels.get(Side.SERVER).writeAndFlush(message);
	}

	public void sendToServer(IPacket message) {
		this.channels.get(Side.CLIENT).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
		this.channels.get(Side.CLIENT).writeAndFlush(message);
	}
}
