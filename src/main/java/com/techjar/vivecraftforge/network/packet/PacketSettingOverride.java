package com.techjar.vivecraftforge.network.packet;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.techjar.vivecraftforge.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketSettingOverride implements IPacket {
	public Map<String, Object> settings = new HashMap<>();

	public PacketSettingOverride() {
	}

	public PacketSettingOverride(Map<String, Object> settings) {
		this.settings = settings;
	}

	@Override
	public void encode(final PacketBuffer buffer) {
		for (Map.Entry<String, Object> entry : settings.entrySet()) {
			buffer.writeString(entry.getKey());
			buffer.writeString(entry.getValue().toString());
		}
	}

	@Override
	public void decode(final PacketBuffer buffer) {
		while (buffer.readableBytes() > 0) {
			settings.put(buffer.readString(), buffer.readString());
		}
	}

	@Override
	public void handleClient(final Supplier<NetworkEvent.Context> context) {
	}

	@Override
	public void handleServer(final Supplier<NetworkEvent.Context> context) {
	}
}
