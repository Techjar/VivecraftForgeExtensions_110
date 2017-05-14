package com.techjar.vivecraftforge.core.asm;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public interface ASMMethodHandler {
	MethodTuple getDesiredMethod();
	void patchMethod(MethodNode methodNode, ClassNode classNode, boolean obfuscated);
}
