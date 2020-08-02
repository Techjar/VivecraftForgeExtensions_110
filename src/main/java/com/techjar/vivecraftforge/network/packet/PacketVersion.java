package com.techjar.vivecraftforge.network.packet;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.function.Supplier;

import com.techjar.vivecraftforge.Config;
import com.techjar.vivecraftforge.VivecraftForge;
import com.techjar.vivecraftforge.network.ChannelHandler;
import com.techjar.vivecraftforge.network.IPacket;
import com.techjar.vivecraftforge.util.LogHelper;
import com.techjar.vivecraftforge.util.PlayerTracker;
import com.techjar.vivecraftforge.util.VRPlayerData;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

/*
 * Why the fuck does the client want a length-prefixed string, but sends
 * a string that's just char bytes with no length prefix? This whole
 * protocol is an awful mess. I didn't write it, so don't blame me.
 */
public class PacketVersion implements IPacket {
	public String message;

	public PacketVersion() {
	}

	public PacketVersion(String message) {
		this.message = message;
	}

	@Override
	public void encode(final PacketBuffer buffer) {
		buffer.writeString(message);
	}

	@Override
	public void decode(final PacketBuffer buffer) {
		byte[] bytes = new byte[buffer.readableBytes()];
		buffer.readBytes(bytes);
		message = new String(bytes, StandardCharsets.UTF_8);
	}

	@Override
	public void handleClient(final Supplier<NetworkEvent.Context> context) {
	}

	@Override
	public void handleServer(final Supplier<NetworkEvent.Context> context) {
		ServerPlayerEntity player = context.get().getSender();
		ChannelHandler.sendTo(new PacketVersion(VivecraftForge.MOD_INFO.getDisplayName() + " " + VivecraftForge.MOD_INFO.getVersion()), player);
		if (!message.contains("NONVR")) {
			LogHelper.info("VR player joined: {}", message);
			ChannelHandler.sendTo(new PacketRequestData(), player);

			if (Config.teleportEnabled.get())
				ChannelHandler.sendTo(new PacketTeleport(), player);
			if (Config.climbeyEnabled.get())
				ChannelHandler.sendTo(new PacketClimbing(Config.blockListMode.get(), Config.blockList.get()), player);
			if (Config.crawlingEnabled.get())
				ChannelHandler.sendTo(new PacketCrawl(), player);

			if (Config.teleportLimited.get()) {
				HashMap<String, Object> map = new HashMap<>();
				map.put("limitedTeleport", true);
				map.put("teleportLimitUp", Config.teleportLimitUp.get());
				map.put("teleportLimitDown", Config.teleportLimitDown.get());
				map.put("teleportLimitHoriz", Config.teleportLimitHoriz.get());
				ChannelHandler.sendTo(new PacketSettingOverride(map), player);
			}

			if (Config.worldScaleLimited.get()) {
				HashMap<String, Object> map = new HashMap<>();
				map.put("worldScale.min", Config.worldScaleMin.get());
				map.put("worldScale.max", Config.worldScaleMax.get());
				ChannelHandler.sendTo(new PacketSettingOverride(map), player);
			}

			context.get().enqueueWork(() -> {
				PlayerTracker.players.put(player.getGameProfile().getId(), new VRPlayerData());
				if (Config.enableJoinMessages.get() && !Config.joinMessageVR.get().isEmpty())
					player.getServer().getPlayerList().func_232641_a_(new StringTextComponent(String.format(Config.joinMessageVR.get(), player.getDisplayName())), ChatType.SYSTEM, net.minecraft.util.Util.DUMMY_UUID);
			});
		} else {
			LogHelper.info("Non-VR player joined: {}", message);
			context.get().enqueueWork(() -> {
				PlayerTracker.nonvrPlayers.add(player.getGameProfile().getId());
				if (Config.enableJoinMessages.get() && !Config.joinMessageNonVR.get().isEmpty())
					player.getServer().getPlayerList().func_232641_a_(new StringTextComponent(String.format(Config.joinMessageNonVR.get(), player.getDisplayName())), ChatType.SYSTEM, net.minecraft.util.Util.DUMMY_UUID);
			});
		}
	}
}
