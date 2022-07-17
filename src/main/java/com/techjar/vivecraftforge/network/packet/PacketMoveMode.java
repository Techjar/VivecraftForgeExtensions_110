package com.techjar.vivecraftforge.network.packet;

import java.util.function.Supplier;

import com.techjar.vivecraftforge.network.IPacket;
import com.techjar.vivecraftforge.util.PlayerTracker;
import com.techjar.vivecraftforge.util.VRPlayerData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class PacketMoveMode implements IPacket {
	public boolean freeMove;

	public PacketMoveMode() {
	}

	public PacketMoveMode(boolean freeMove) {
		this.freeMove = freeMove;
	}

	@Override
	public void encode(final FriendlyByteBuf buffer) {
		//buffer.writeBoolean(freeMove);
	}

	@Override
	public void decode(final FriendlyByteBuf buffer) {
		//freeMove = buffer.readBoolean();
	}

	@Override
	public void handleClient(final Supplier<NetworkEvent.Context> context) {
	}

	@Override
	public void handleServer(final Supplier<NetworkEvent.Context> context) {
		/*ServerPlayerEntity player = context.get().getSender();
		context.get().enqueueWork(() -> {
			VRPlayerData data = PlayerTracker.getPlayerData(player, true);
			data.freeMove = freeMove;
		});*/
	}
}
