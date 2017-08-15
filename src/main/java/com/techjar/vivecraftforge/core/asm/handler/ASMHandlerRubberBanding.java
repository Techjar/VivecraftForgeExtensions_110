package com.techjar.vivecraftforge.core.asm.handler;

import com.techjar.vivecraftforge.core.asm.ASMClassHandler;
import com.techjar.vivecraftforge.core.asm.ASMMethodHandler;
import com.techjar.vivecraftforge.core.asm.ASMUtil;
import com.techjar.vivecraftforge.core.asm.ClassTuple;
import com.techjar.vivecraftforge.core.asm.MethodTuple;
import com.techjar.vivecraftforge.core.asm.ObfNames;
import com.techjar.vivecraftforge.util.VivecraftForgeLog;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import net.minecraftforge.fml.relauncher.Side;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class ASMHandlerRubberBanding extends ASMClassHandler {
	@Override
	public ClassTuple getDesiredClass() {
		return new ClassTuple("net.minecraft.network.NetHandlerPlayServer", ObfNames.NETHANDLERPLAYSERVER);
	}

	@Override
	public ASMMethodHandler[] getMethodHandlers() {
		return new ASMMethodHandler[]{new MethodHandler()};
	}

	@Override
	public boolean getComputeFrames() {
		return false;
	}

	@Override
	public boolean shouldPatchClass() {
		return FMLLaunchHandler.side() == Side.SERVER;
	}

	public static class MethodHandler implements ASMMethodHandler {
		@Override
		public MethodTuple getDesiredMethod() {
			return new MethodTuple("processPlayer", "(Lnet/minecraft/network/play/client/CPacketPlayer;)V", "a", "(L" + ObfNames.CPACKETPLAYER + ";)V");
		}

		@Override
		public void patchMethod(MethodNode methodNode, ClassNode classNode, boolean obfuscated) {
			LdcInsnNode ldc1 = (LdcInsnNode)ASMUtil.findFirstInstruction(methodNode, Opcodes.LDC, 300F);
			InsnList insnList = new InsnList();
			insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
			insnList.add(new FieldInsnNode(Opcodes.GETFIELD, obfuscated ? ObfNames.NETHANDLERPLAYSERVER : "net/minecraft/network/NetHandlerPlayServer", obfuscated ? "b" : "player", obfuscated ? "L" + ObfNames.ENTITYPLAYERMP + ";" : "Lnet/minecraft/entity/player/EntityPlayerMP;"));
			insnList.add(new InsnNode(Opcodes.ICONST_1));
			insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/techjar/vivecraftforge/util/ASMDelegator", "movedTooQuicklyThreshold", obfuscated ? "(L" + ObfNames.ENTITYPLAYER + ";Z)F" : "(Lnet/minecraft/entity/player/EntityPlayer;Z)F", false));
			methodNode.instructions.insert(ldc1, insnList);
			methodNode.instructions.remove(ldc1);
			VivecraftForgeLog.debug("Replaced float constant " + ldc1.cst + " with delegate method call.");

			LdcInsnNode ldc2 = (LdcInsnNode)ASMUtil.findFirstInstruction(methodNode, Opcodes.LDC, 100F);
			insnList.clear();
			insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
			insnList.add(new FieldInsnNode(Opcodes.GETFIELD, obfuscated ? ObfNames.NETHANDLERPLAYSERVER : "net/minecraft/network/NetHandlerPlayServer", obfuscated ? "b" : "player", obfuscated ? "L" + ObfNames.ENTITYPLAYERMP + ";" : "Lnet/minecraft/entity/player/EntityPlayerMP;"));
			insnList.add(new InsnNode(Opcodes.ICONST_0));
			insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/techjar/vivecraftforge/util/ASMDelegator", "movedTooQuicklyThreshold", obfuscated ? "(L" + ObfNames.ENTITYPLAYER + ";Z)F" : "(Lnet/minecraft/entity/player/EntityPlayer;Z)F", false));
			methodNode.instructions.insert(ldc2, insnList);
			methodNode.instructions.remove(ldc2);
			VivecraftForgeLog.debug("Replaced float constant " + ldc2.cst + " with delegate method call.");

			LdcInsnNode ldc3 = (LdcInsnNode)ASMUtil.findNthInstruction(methodNode, Opcodes.LDC, 1, 0.0625D);
			insnList.clear();
			insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
			insnList.add(new FieldInsnNode(Opcodes.GETFIELD, obfuscated ? ObfNames.NETHANDLERPLAYSERVER : "net/minecraft/network/NetHandlerPlayServer", obfuscated ? "b" : "player", obfuscated ? "L" + ObfNames.ENTITYPLAYERMP + ";" : "Lnet/minecraft/entity/player/EntityPlayerMP;"));
			insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/techjar/vivecraftforge/util/ASMDelegator", "movedWronglyThreshold", obfuscated ? "(L" + ObfNames.ENTITYPLAYER + ";)D" : "(Lnet/minecraft/entity/player/EntityPlayer;)D", false));
			methodNode.instructions.insert(ldc3, insnList);
			methodNode.instructions.remove(ldc3);
			VivecraftForgeLog.debug("Replaced second double constant " + ldc3.cst + " with delegate method call.");
		}
	}
}
