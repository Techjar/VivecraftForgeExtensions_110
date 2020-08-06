package com.techjar.vivecraftforge.network.packet;

import java.util.function.Supplier;

import com.techjar.vivecraftforge.network.IPacket;
import com.techjar.vivecraftforge.util.PlayerTracker;
import com.techjar.vivecraftforge.util.VRPlayerData;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketDraw implements IPacket {
	public float drawDist;

	public PacketDraw() {
	}

	public PacketDraw(float drawDist) {
		this.drawDist = drawDist;
	}

	@Override
	public void encode(final PacketBuffer buffer) {
		buffer.writeFloat(drawDist);
	}

	@Override
	public void decode(final PacketBuffer buffer) {
		drawDist = buffer.readFloat();
	}

	@Override
	public void handleClient(final Supplier<NetworkEvent.Context> context) {
	}

	@Override
	public void handleServer(final Supplier<NetworkEvent.Context> context) {
		ServerPlayerEntity player = context.get().getSender();
		context.get().enqueueWork(() -> {
			if (!PlayerTracker.hasPlayerData(player))
				return;
			VRPlayerData data = PlayerTracker.getPlayerData(player, true);
			data.bowDraw = drawDist;
		});
	}
}
