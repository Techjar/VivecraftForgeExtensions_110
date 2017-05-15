package com.techjar.vivecraftforge.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.text.StrSubstitutor;

import java.util.HashMap;
import java.util.Map;

public class MessageFormatter {
	private Map<String, FormatObject> formatMap = new HashMap<String, FormatObject>();

	public MessageFormatter formatter(String name, FormatObject formatObject) {
		formatMap.put(name, formatObject);
		return this;
	}

	public MessageFormatter player(final EntityPlayer player) {
		formatMap.put("player", new FormatObject() {
			@Override
			public String getText() {
				return player.getDisplayName().getUnformattedText();
			}
		});
		return this;
	}

	public ITextComponent format(String str) {
		HashMap<String, String> map = new HashMap<String, String>();
		for (Map.Entry<String, FormatObject> entry : formatMap.entrySet()) {
			map.put(entry.getKey(), entry.getValue().getText());
		}
		StrSubstitutor substitutor = new StrSubstitutor(map, "%", "%", '%');
		return new TextComponentString(substitutor.replace(str));
	}

	public interface FormatObject {
		String getText();
	}
}
