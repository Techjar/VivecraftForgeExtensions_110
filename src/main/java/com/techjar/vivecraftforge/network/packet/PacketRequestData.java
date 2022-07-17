package com.techjar.vivecraftforge.network.packet;

import java.util.function.Supplier;

import com.techjar.vivecraftforge.network.IPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class PacketRequestData implements IPacket {
	public PacketRequestData() {
	}

	@Override
	public void encode(final FriendlyByteBuf buffer) {
	}

	@Override
	public void decode(final FriendlyByteBuf buffer) {
	}

	@Override
	public void handleClient(final Supplier<NetworkEvent.Context> context) {
	}

	@Override
	public void handleServer(final Supplier<NetworkEvent.Context> context) {
	}
}
