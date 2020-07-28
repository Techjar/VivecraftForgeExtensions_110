package com.techjar.vivecraftforge.network.packet;

import com.techjar.vivecraftforge.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class PacketUberPacket implements IPacket {
	public UUID uuid;
	public PacketHeadData headData;
	public PacketController0Data controller0Data;
	public PacketController1Data controller1Data;
	public float worldScale;
	public float height;

	public PacketUberPacket() {
	}

	public PacketUberPacket(UUID uuid, PacketHeadData headData, PacketController0Data controller0Data, PacketController1Data controller1Data, float worldScale, float height) {
		this.uuid = uuid;
		this.headData = headData;
		this.controller0Data = controller0Data;
		this.controller1Data = controller1Data;
		this.worldScale = worldScale;
		this.height = height;
	}

	@Override
	public void encode(final PacketBuffer buffer) {
		buffer.writeLong(uuid.getMostSignificantBits());
		buffer.writeLong(uuid.getLeastSignificantBits());
		headData.encode(buffer);
		controller0Data.encode(buffer);
		controller1Data.encode(buffer);
		buffer.writeFloat(worldScale);
		buffer.writeFloat(height);
	}

	@Override
	public void decode(final PacketBuffer buffer) {
		uuid = new UUID(buffer.readLong(), buffer.readLong());
		headData = new PacketHeadData();
		headData.decode(buffer);
		controller0Data = new PacketController0Data();
		controller0Data.decode(buffer);
		controller1Data = new PacketController1Data();
		controller1Data.decode(buffer);
		worldScale = buffer.readFloat();
		height = buffer.readFloat();
	}

	@Override
	public void handleClient(final Supplier<NetworkEvent.Context> context) {
	}

	@Override
	public void handleServer(final Supplier<NetworkEvent.Context> context) {
	}
}
