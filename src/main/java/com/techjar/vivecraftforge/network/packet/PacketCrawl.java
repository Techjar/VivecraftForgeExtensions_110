package com.techjar.vivecraftforge.network.packet;

import java.util.function.Supplier;

import com.techjar.vivecraftforge.Config;
import com.techjar.vivecraftforge.network.IPacket;
import com.techjar.vivecraftforge.util.PlayerTracker;
import com.techjar.vivecraftforge.util.VRPlayerData;
import net.minecraft.world.entity.Pose;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class PacketCrawl implements IPacket {
	public boolean crawling;

	public PacketCrawl() {
	}

	public PacketCrawl(boolean crawling) {
		this.crawling = crawling;
	}

	@Override
	public void encode(FriendlyByteBuf buffer) {
	}

	@Override
	public void decode(FriendlyByteBuf buffer) {
		crawling = buffer.readBoolean();
	}

	@Override
	public void handleClient(Supplier<NetworkEvent.Context> context) {
	}

	@Override
	public void handleServer(Supplier<NetworkEvent.Context> context) {
		if (Config.crawlingEnabled.get()) {
			ServerPlayer player = context.get().getSender();
			context.get().enqueueWork(() -> {
				if (!PlayerTracker.hasPlayerData(player))
					return;
				VRPlayerData data = PlayerTracker.getPlayerData(player, true);
				data.crawling = crawling;
				if (data.crawling)
					player.setPose(Pose.SWIMMING);
			});
		}
	}
}
