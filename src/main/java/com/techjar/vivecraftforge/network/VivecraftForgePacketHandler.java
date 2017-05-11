package com.techjar.vivecraftforge.network;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;

import com.techjar.vivecraftforge.VivecraftForge;
import com.techjar.vivecraftforge.util.VivecraftForgeLog;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;

@Sharable
public class VivecraftForgePacketHandler extends SimpleChannelInboundHandler<IPacket> {
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, IPacket msg) throws Exception {
		INetHandler netHandler = ctx.channel().attr(NetworkRegistry.NET_HANDLER).get();
		EntityPlayer player = VivecraftForge.proxy.getPlayerFromNetHandler(netHandler);

		switch (FMLCommonHandler.instance().getEffectiveSide()) {
			case CLIENT:
				VivecraftForgeLog.severe("Should never receive packet on client!");
				break;
			case SERVER:
				msg.handleServer((EntityPlayerMP)player);
				break;
			default:
				VivecraftForgeLog.severe("Impossible scenario encountered! Effective side is neither server nor client!");
				break;
		}
	}
}
