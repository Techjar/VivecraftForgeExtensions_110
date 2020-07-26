package com.techjar.vivecraftforge.network;

import com.techjar.vivecraftforge.util.LogHelper;

import com.techjar.vivecraftforge.network.packet.*;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class ChannelHandler {
	private static SimpleChannel CHANNEL;

	public static void init() {
		CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation("vivecraft", "data"), () -> "lol", (s) -> true, (s) -> true);

		addDiscriminator(0, new Message<>(PacketVersion.class));
		addDiscriminator(1, new Message<>(PacketRequestData.class));
		addDiscriminator(2, new Message<>(PacketHeadData.class));
		addDiscriminator(3, new Message<>(PacketController0Data.class));
		addDiscriminator(4, new Message<>(PacketController1Data.class));
		addDiscriminator(5, new Message<>(PacketWorldScale.class));
		addDiscriminator(6, new Message<>(PacketDraw.class));
		addDiscriminator(7, new Message<>(PacketMoveMode.class));
		addDiscriminator(8, new Message<>(PacketUberPacket.class));
		addDiscriminator(9, new Message<>(PacketTeleport.class));
		addDiscriminator(10, new Message<>(PacketClimbing.class));
		addDiscriminator(11, new Message<>(PacketSettingOverride.class));
		addDiscriminator(12, new Message<>(PacketHeight.class));
		addDiscriminator(13, new Message<>(PacketActiveHand.class));

		LogHelper.debug("Networking initialized");
	}

	private static <T extends IPacket> void addDiscriminator(int d, Message<T> message) {
		CHANNEL.registerMessage(d, message.getPacketClass(), message::encode, message::decode, message::handle);
	}

	public static void sendToAll(IPacket message) {
		CHANNEL.send(PacketDistributor.ALL.noArg(), message);
	}

	public static void sendTo(IPacket message, ServerPlayerEntity player) {
		CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), message);
	}

	public static void sendToAllAround(IPacket message, PacketDistributor.TargetPoint point) {
		CHANNEL.send(PacketDistributor.NEAR.with(() -> point), message);
	}

	public static void sendToAllTrackingEntity(IPacket message, ServerPlayerEntity player) {
		CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> player), message);
	}

	public static void sendToAllTrackingEntityAndSelf(IPacket message, ServerPlayerEntity player) {
		CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player), message);
	}

	public static void sendToAllTrackingChunk(IPacket message, Chunk chunk) {
		CHANNEL.send(PacketDistributor.TRACKING_CHUNK.with(() -> chunk), message);
	}

	public static void sendToAllInDimension(IPacket message, RegistryKey<World> dimension) {
		CHANNEL.send(PacketDistributor.DIMENSION.with(() -> dimension), message);
	}

	public static void sendToServer(IPacket message) {
		CHANNEL.sendToServer(message);
	}
}
