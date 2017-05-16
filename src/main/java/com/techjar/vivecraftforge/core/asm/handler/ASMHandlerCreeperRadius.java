package com.techjar.vivecraftforge.core.asm.handler;

import com.techjar.vivecraftforge.core.asm.ObfNames;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import net.minecraftforge.fml.relauncher.Side;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.techjar.vivecraftforge.core.asm.ASMClassHandler;
import com.techjar.vivecraftforge.core.asm.ASMMethodHandler;
import com.techjar.vivecraftforge.core.asm.ASMUtil;
import com.techjar.vivecraftforge.core.asm.ClassTuple;
import com.techjar.vivecraftforge.core.asm.MethodTuple;
import com.techjar.vivecraftforge.util.VivecraftForgeLog;

public class ASMHandlerCreeperRadius extends ASMClassHandler {
	@Override
	public ClassTuple getDesiredClass() {
		return new ClassTuple("net.minecraft.entity.ai.EntityAICreeperSwell", ObfNames.ENTITYAICREEPERSWELL);
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
			return new MethodTuple("shouldExecute", "()Z", "a", "()Z");
		}

		@Override
		public void patchMethod(MethodNode methodNode, ClassNode classNode, boolean obfuscated) {
			LdcInsnNode insn = (LdcInsnNode)ASMUtil.findLastOpcode(methodNode, Opcodes.LDC);
			InsnList insnList = new InsnList();
			insnList.add(new VarInsnNode(Opcodes.ALOAD, 1));
			insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/techjar/vivecraftforge/util/ASMDelegator", "creeperSwellDistance", obfuscated ? "(DL" + ObfNames.ENTITYLIVINGBASE + ";)D" : "(DLnet/minecraft/entity/EntityLivingBase;)D", false));
			methodNode.instructions.insert(insn, insnList);
			VivecraftForgeLog.debug("Inserted delegate method call.");
		}
	}
}
