package com.techjar.vivecraftforge.network.packet;

import java.util.function.Supplier;

import com.techjar.vivecraftforge.Config;
import com.techjar.vivecraftforge.network.IPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class PacketTeleport implements IPacket {
	public float posX;
	public float posY;
	public float posZ;

	public PacketTeleport() {
	}

	@Override
	public void encode(final FriendlyByteBuf buffer) {
	}

	@Override
	public void decode(final FriendlyByteBuf buffer) {
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
			ServerPlayer player = context.get().getSender();
			context.get().enqueueWork(() -> player.moveTo(posX, posY, posZ, player.getYRot(), player.getXRot()));
		}
	}
}
