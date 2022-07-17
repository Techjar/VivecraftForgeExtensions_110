package com.techjar.vivecraftforge.util;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.Connection;
import net.minecraft.server.RunningOnDifferentThreadException;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemPacket;
import net.minecraft.world.phys.Vec3;

public class AimFixHandler extends ChannelInboundHandlerAdapter {
	private final Connection netManager;

	public AimFixHandler(Connection netManager) {
		this.netManager = netManager;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ServerPlayer player = ((ServerGamePacketListenerImpl)netManager.getPacketListener()).player;
		boolean isCapturedPacket = msg instanceof ServerboundUseItemPacket || msg instanceof ServerboundUseItemOnPacket || msg instanceof ServerboundPlayerActionPacket;

		if (!PlayerTracker.hasPlayerData(player) || !isCapturedPacket || player.getServer() == null) {
			// we don't need to handle this packet, just defer to the next handler in the pipeline
			ctx.fireChannelRead(msg);
			return;
		}

		LogHelper.debug("Captured message {}", msg.getClass().getSimpleName());
		player.getServer().submit(() -> {
			// Save all the current orientation data
			Vec3 oldPos = player.position();
			Vec3 oldPrevPos = new Vec3(player.xo, player.yo, player.zo);
			float oldPitch = player.getXRot();
			float oldYaw = player.getYRot();
			float oldYawHead = player.yHeadRot;
			float oldPrevPitch = player.xRotO;
			float oldPrevYaw = player.yRotO;
			float oldPrevYawHead = player.yHeadRotO;
			float oldEyeHeight = player.eyeHeight;

			VRPlayerData data = null;
			if (PlayerTracker.hasPlayerData(player)) { // Check again in case of race condition
				data = PlayerTracker.getPlayerDataAbsolute(player);
				Vec3 pos = data.getController(0).getPos();
				Vec3 aim = data.getController(0).getRot().multiply(new Vec3(0, 0, -1));

				// Inject our custom orientation data
				player.setPosRaw(pos.x, pos.y, pos.z);
				player.xo = pos.x;
				player.yo = pos.y;
				player.zo = pos.z;
				player.setXRot((float)Math.toDegrees(Math.asin(-aim.y)));
				player.setYRot((float)Math.toDegrees(Math.atan2(-aim.x, aim.z)));
				player.xRotO = player.getXRot();
				player.yRotO = player.yHeadRotO = player.yHeadRot = player.getYRot();
				player.eyeHeight = 0;

				// Set up offset to fix relative positions
				data = PlayerTracker.getPlayerData(player);
				data.offset = oldPos.subtract(pos);
			}

			// Call the packet handler directly
			// This is several implementation details that we have to replicate
			try {
				if (netManager.isConnected()) {
					try {
						((Packet<PacketListener>)msg).handle(netManager.getPacketListener());
					} catch (RunningOnDifferentThreadException e) { // Apparently might get thrown and can be ignored
					}
				}
			} finally {
				// Vanilla uses SimpleInboundChannelHandler, which automatically releases
				// by default, so we're expected to release the packet once we're done.
				ReferenceCountUtil.release(msg);
			}

			// Restore the original orientation data
			player.setPosRaw(oldPos.x, oldPos.y, oldPos.z);
			player.xo = oldPrevPos.x;
			player.yo = oldPrevPos.y;
			player.zo = oldPrevPos.z;
			player.setXRot(oldPitch);
			player.setYRot(oldYaw);
			player.yHeadRot = oldYawHead;
			player.xRotO = oldPrevPitch;
			player.yRotO = oldPrevYaw;
			player.yHeadRotO = oldPrevYawHead;
			player.eyeHeight = oldEyeHeight;

			// Reset offset
			if (data != null)
				data.offset = new Vec3(0, 0, 0);
		});
	}
}
