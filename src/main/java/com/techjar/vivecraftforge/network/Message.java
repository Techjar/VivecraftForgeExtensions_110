package com.techjar.vivecraftforge.network;

import java.util.function.Supplier;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

public class Message<T extends IPacket> {
	private final Class<T> tClass;

	public Message(Class<T> tClass) {
		this.tClass = tClass;
	}

	public Class<T> getPacketClass() {
		return tClass;
	}

	public final void encode(T packet, PacketBuffer buffer) {
		packet.encode(buffer);
	}

	public final T decode(PacketBuffer buffer) {
		T packet;
		try {
			packet = tClass.newInstance();
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException("instantiating packet", e);
		}

		packet.decode(buffer);
		return packet;
	}

	public final void handle(T packet, Supplier<NetworkEvent.Context> context) {
		if (context.get().getDirection() == NetworkDirection.PLAY_TO_SERVER)
			packet.handleServer(context);
		else
			packet.handleClient(context);
		context.get().setPacketHandled(true);
	}
}
