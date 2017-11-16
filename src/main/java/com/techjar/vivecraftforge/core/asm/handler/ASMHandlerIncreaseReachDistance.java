package com.techjar.vivecraftforge.core.asm.handler;

import com.techjar.vivecraftforge.core.asm.ObfNames;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import net.minecraftforge.fml.relauncher.Side;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.techjar.vivecraftforge.core.asm.ASMClassHandler;
import com.techjar.vivecraftforge.core.asm.ASMMethodHandler;
import com.techjar.vivecraftforge.core.asm.ASMUtil;
import com.techjar.vivecraftforge.core.asm.ClassTuple;
import com.techjar.vivecraftforge.core.asm.MethodTuple;
import com.techjar.vivecraftforge.util.LogHelper;

public class ASMHandlerIncreaseReachDistance extends ASMClassHandler {
	@Override
	public ClassTuple getDesiredClass() {
		return new ClassTuple("net.minecraft.network.NetHandlerPlayServer", ObfNames.NETHANDLERPLAYSERVER);
	}

	@Override
	public ASMMethodHandler[] getMethodHandlers() {
		return new ASMMethodHandler[]{new PlacementMethodHandler(), new DiggingMethodHandler(), new EntityMethodHandler()};
	}

	@Override
	public boolean getComputeFrames() {
		return false;
	}

	@Override
	public boolean shouldPatchClass() {
		return FMLLaunchHandler.side() == Side.SERVER;
	}
	
	public static class PlacementMethodHandler implements ASMMethodHandler {
		@Override
		public MethodTuple getDesiredMethod() {
			return new MethodTuple("processTryUseItemOnBlock", "(Lnet/minecraft/network/play/client/CPacketPlayerTryUseItemOnBlock;)V", "a", "(L" + ObfNames.CPACKETPLAYERTRYUSEITEMONBLOCK + ";)V");
		}

		@Override
		public void patchMethod(MethodNode methodNode, ClassNode classNode, boolean obfuscated) {
			AbstractInsnNode insert = ASMUtil.findNthInstruction(methodNode, Opcodes.DSTORE, 1, 7);
			if (insert == null) return;
			InsnList insnList = new InsnList();
			insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
			insnList.add(new FieldInsnNode(Opcodes.GETFIELD, obfuscated ? ObfNames.NETHANDLERPLAYSERVER : "net/minecraft/network/NetHandlerPlayServer", obfuscated ? "b" : "player", obfuscated ? "L" + ObfNames.ENTITYPLAYERMP + ";" : "Lnet/minecraft/entity/player/EntityPlayerMP;"));
			insnList.add(new VarInsnNode(Opcodes.DLOAD, 7));
			insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/techjar/vivecraftforge/util/ASMDelegator", "playerBlockReachDistance", obfuscated ? "(L" + ObfNames.ENTITYPLAYER + ";D)D" : "(Lnet/minecraft/entity/player/EntityPlayer;D)D", false));
			insnList.add(new VarInsnNode(Opcodes.DSTORE, 7));
			methodNode.instructions.insert(insert, insnList);
			LogHelper.debug("Inserted delegate method call.");
		}
	}
	
	public static class DiggingMethodHandler implements ASMMethodHandler {
		@Override
		public MethodTuple getDesiredMethod() {
			return new MethodTuple("processPlayerDigging", "(Lnet/minecraft/network/play/client/CPacketPlayerDigging;)V", "a", "(L" + ObfNames.CPACKETPLAYERDIGGING + ";)V");
		}

		@Override
		public void patchMethod(MethodNode methodNode, ClassNode classNode, boolean obfuscated) {
			AbstractInsnNode insert = ASMUtil.findNthInstruction(methodNode, Opcodes.DSTORE, 1, 13);
			if (insert == null) return;
			InsnList insnList = new InsnList();
			insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
			insnList.add(new FieldInsnNode(Opcodes.GETFIELD, obfuscated ? ObfNames.NETHANDLERPLAYSERVER : "net/minecraft/network/NetHandlerPlayServer", obfuscated ? "b" : "player", obfuscated ? "L" + ObfNames.ENTITYPLAYERMP + ";" : "Lnet/minecraft/entity/player/EntityPlayerMP;"));
			insnList.add(new VarInsnNode(Opcodes.DLOAD, 13));
			insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/techjar/vivecraftforge/util/ASMDelegator", "playerBlockReachDistance", obfuscated ? "(L" + ObfNames.ENTITYPLAYER + ";D)D" : "(Lnet/minecraft/entity/player/EntityPlayer;D)D", false));
			insnList.add(new VarInsnNode(Opcodes.DSTORE, 13));
			methodNode.instructions.insert(insert, insnList);
			LogHelper.debug("Inserted delegate method call.");
		}
	}
	
	public static class EntityMethodHandler implements ASMMethodHandler {
		@Override
		public MethodTuple getDesiredMethod() {
			return new MethodTuple("processUseEntity", "(Lnet/minecraft/network/play/client/CPacketUseEntity;)V", "a", "(L" + ObfNames.CPACKETUSEENTITY + ";)V");
		}

		@Override
		public void patchMethod(MethodNode methodNode, ClassNode classNode, boolean obfuscated) {
			AbstractInsnNode insert = ASMUtil.findFirstInstruction(methodNode, Opcodes.DSTORE, 5);
			if (insert == null) return;
			InsnList insnList = new InsnList();
			insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
			insnList.add(new FieldInsnNode(Opcodes.GETFIELD, obfuscated ? ObfNames.NETHANDLERPLAYSERVER : "net/minecraft/network/NetHandlerPlayServer", obfuscated ? "b" : "player", obfuscated ? "L" + ObfNames.ENTITYPLAYERMP + ";" : "Lnet/minecraft/entity/player/EntityPlayerMP;"));
			insnList.add(new VarInsnNode(Opcodes.DLOAD, 5));
			insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/techjar/vivecraftforge/util/ASMDelegator", "playerEntityReachDistance", obfuscated ? "(L" + ObfNames.ENTITYPLAYER + ";D)D" : "(Lnet/minecraft/entity/player/EntityPlayer;D)D", false));
			insnList.add(new VarInsnNode(Opcodes.DSTORE, 5));
			insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
			insnList.add(new FieldInsnNode(Opcodes.GETFIELD, obfuscated ? ObfNames.NETHANDLERPLAYSERVER : "net/minecraft/network/NetHandlerPlayServer", obfuscated ? "b" : "player", obfuscated ? "L" + ObfNames.ENTITYPLAYERMP + ";" : "Lnet/minecraft/entity/player/EntityPlayerMP;"));
			insnList.add(new VarInsnNode(Opcodes.ILOAD, 4));
			insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/techjar/vivecraftforge/util/ASMDelegator", "playerEntitySeenOverride", obfuscated ? "(L" + ObfNames.ENTITYPLAYER + ";Z)Z" : "(Lnet/minecraft/entity/player/EntityPlayer;Z)Z", false));
			insnList.add(new VarInsnNode(Opcodes.ISTORE, 4));
			methodNode.instructions.insert(insert, insnList);
			LogHelper.debug("Inserted delegate method call.");
		}
	}
}
