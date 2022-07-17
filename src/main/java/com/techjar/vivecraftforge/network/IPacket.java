package com.techjar.vivecraftforge.network;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public interface IPacket {
	void encode(final FriendlyByteBuf buffer);

	void decode(final FriendlyByteBuf buffer);

	void handleClient(final Supplier<NetworkEvent.Context> context);

	void handleServer(final Supplier<NetworkEvent.Context> context);
}
