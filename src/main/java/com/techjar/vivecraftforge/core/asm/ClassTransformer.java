package com.techjar.vivecraftforge.core.asm;

import java.util.ArrayList;
import java.util.List;

import com.techjar.vivecraftforge.core.asm.handler.*;
import com.techjar.vivecraftforge.util.LogHelper;

import net.minecraft.launchwrapper.IClassTransformer;

public class ClassTransformer implements IClassTransformer {
	private static final List<ASMClassHandler> asmHandlers = new ArrayList<ASMClassHandler>();
	static {
		asmHandlers.add(new ASMHandlerIncreaseReachDistance());
		asmHandlers.add(new ASMHandlerCreeperRadius());
		asmHandlers.add(new ASMHandlerEndermanLook());
		asmHandlers.add(new ASMHandlerRubberBanding());
	}

	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes) {
		for (ASMClassHandler handler : asmHandlers) {
			if (!handler.shouldPatchClass()) continue;
			ClassTuple tuple = handler.getDesiredClass();
			if (name.equals(tuple.classNameObf)) {
				LogHelper.debug("Patching class: " + name + " (" + tuple.className + ") using " + handler.getClass().getSimpleName());
				bytes = handler.patchClass(bytes, true);
			} else if (name.equals(tuple.className)) {
				LogHelper.debug("Patching class: " + name + " using " + handler.getClass().getSimpleName());
				bytes = handler.patchClass(bytes, false);
			}
		}
		return bytes;
	}
}
