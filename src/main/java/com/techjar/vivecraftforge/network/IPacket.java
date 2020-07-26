package com.techjar.vivecraftforge.network;

import java.util.function.Supplier;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public interface IPacket {
	void encode(final PacketBuffer buffer);

	void decode(final PacketBuffer buffer);

	void handleClient(final Supplier<NetworkEvent.Context> context);

	void handleServer(final Supplier<NetworkEvent.Context> context);
}
