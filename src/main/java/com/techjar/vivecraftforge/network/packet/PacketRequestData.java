package com.techjar.vivecraftforge.network.packet;

import java.util.function.Supplier;

import com.techjar.vivecraftforge.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketRequestData implements IPacket {
	public PacketRequestData() {
	}

	@Override
	public void encode(final PacketBuffer buffer) {
	}

	@Override
	public void decode(final PacketBuffer buffer) {
	}

	@Override
	public void handleClient(final Supplier<NetworkEvent.Context> context) {
	}

	@Override
	public void handleServer(final Supplier<NetworkEvent.Context> context) {
	}
}
