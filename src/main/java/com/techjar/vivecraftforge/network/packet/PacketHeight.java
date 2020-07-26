package com.techjar.vivecraftforge.network.packet;

import java.util.function.Supplier;

import com.techjar.vivecraftforge.network.IPacket;
import com.techjar.vivecraftforge.util.PlayerTracker;
import com.techjar.vivecraftforge.util.VRPlayerData;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketHeight implements IPacket {
	public float height;

	public PacketHeight() {
	}

	public PacketHeight(float height) {
		this.height = height;
	}

	@Override
	public void encode(final PacketBuffer buffer) {
		buffer.writeFloat(height);
	}

	@Override
	public void decode(final PacketBuffer buffer) {
		height = buffer.readFloat();
	}

	@Override
	public void handleClient(final Supplier<NetworkEvent.Context> context) {
	}

	@Override
	public void handleServer(final Supplier<NetworkEvent.Context> context) {
		ServerPlayerEntity player = context.get().getSender();
		context.get().enqueueWork(() -> {
			VRPlayerData data = PlayerTracker.getPlayerData(player, true);
			data.height = height;
		});
	}
}
