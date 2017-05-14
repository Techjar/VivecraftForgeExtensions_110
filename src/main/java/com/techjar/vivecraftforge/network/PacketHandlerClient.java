package com.techjar.vivecraftforge.network;

import com.techjar.vivecraftforge.util.VivecraftForgeLog;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

@Sharable
public class PacketHandlerClient extends SimpleChannelInboundHandler<IPacket> {
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, IPacket msg) throws Exception {
		if (ctx.channel().attr(NetworkRegistry.CHANNEL_SOURCE).get() != Side.CLIENT) return;
		VivecraftForgeLog.warning("Should never receive a client packet!");
		//msg.handleClient(FMLClientHandler.instance().getClientPlayerEntity());
	}
}
