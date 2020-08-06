package com.techjar.vivecraftforge.network.packet;

import java.util.function.Supplier;

import com.techjar.vivecraftforge.Config;
import com.techjar.vivecraftforge.network.IPacket;
import com.techjar.vivecraftforge.util.PlayerTracker;
import com.techjar.vivecraftforge.util.VRPlayerData;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketCrawl implements IPacket {
	public boolean crawling;

	public PacketCrawl() {
	}

	public PacketCrawl(boolean crawling) {
		this.crawling = crawling;
	}

	@Override
	public void encode(PacketBuffer buffer) {
	}

	@Override
	public void decode(PacketBuffer buffer) {
		crawling = buffer.readBoolean();
	}

	@Override
	public void handleClient(Supplier<NetworkEvent.Context> context) {
	}

	@Override
	public void handleServer(Supplier<NetworkEvent.Context> context) {
		if (Config.crawlingEnabled.get()) {
			ServerPlayerEntity player = context.get().getSender();
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
