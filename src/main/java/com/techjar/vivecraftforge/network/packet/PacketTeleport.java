package com.techjar.vivecraftforge.network.packet;

import java.util.function.Supplier;

import com.techjar.vivecraftforge.Config;
import com.techjar.vivecraftforge.network.IPacket;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketTeleport implements IPacket {
	public float posX;
	public float posY;
	public float posZ;

	public PacketTeleport() {
	}

	@Override
	public void encode(final PacketBuffer buffer) {
	}

	@Override
	public void decode(final PacketBuffer buffer) {
		posX = buffer.readFloat();
		posY = buffer.readFloat();
		posZ = buffer.readFloat();
	}

	@Override
	public void handleClient(final Supplier<NetworkEvent.Context> context) {
	}

	@Override
	public void handleServer(final Supplier<NetworkEvent.Context> context) {
		if (Config.teleportEnabled.get()) {
			ServerPlayerEntity player = context.get().getSender();
			context.get().enqueueWork(() -> player.setLocationAndAngles(posX, posY, posZ, player.rotationYaw, player.rotationPitch));
		}
	}
}
