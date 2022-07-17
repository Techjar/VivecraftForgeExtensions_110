package com.techjar.vivecraftforge.network.packet;

import java.util.function.Supplier;

import com.techjar.vivecraftforge.network.IPacket;
import com.techjar.vivecraftforge.util.PlayerTracker;
import com.techjar.vivecraftforge.util.VRPlayerData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class PacketActiveHand implements IPacket {
	public int activeHand;

	public PacketActiveHand() {
	}

	public PacketActiveHand(int activeHand) {
		this.activeHand = activeHand;
	}

	@Override
	public void encode(final FriendlyByteBuf buffer) {
		buffer.writeByte(activeHand);
	}

	@Override
	public void decode(final FriendlyByteBuf buffer) {
		activeHand = buffer.readByte();
	}

	@Override
	public void handleClient(final Supplier<NetworkEvent.Context> context) {
	}

	@Override
	public void handleServer(final Supplier<NetworkEvent.Context> context) {
		ServerPlayer player = context.get().getSender();
		context.get().enqueueWork(() -> {
			if (!PlayerTracker.hasPlayerData(player))
				return;
			VRPlayerData data = PlayerTracker.getPlayerData(player, true);
			data.activeHand = activeHand;
		});
	}
}
