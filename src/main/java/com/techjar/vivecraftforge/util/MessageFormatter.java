package com.techjar.vivecraftforge.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

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
			public ITextComponent getTextComponent() {
				return player.getDisplayName();
			}
		});
		return this;
	}

	public ITextComponent format(String str) {
		ITextComponent textComponent = null;
		StringBuilder sb = new StringBuilder(str);
		while (sb.length() > 0) {
			int openPercent = sb.indexOf("%");
			if (openPercent != -1) {
				if (sb.charAt(openPercent + 1) == '%') {
					textComponent = appendTextComponent(new TextComponentString(sb.substring(0, openPercent + 2)), textComponent);
					sb.delete(0, openPercent + 2);
					continue;
				}
				if (openPercent > 0) {
					textComponent = appendTextComponent(new TextComponentString(sb.substring(0, openPercent)), textComponent);
					sb.delete(0, openPercent + 1);
				}
				int closePercent = sb.indexOf("%");
				if (closePercent == -1) throw new IllegalArgumentException("Missing close % in string: " + str);
				String formatName = sb.substring(0, closePercent);
				FormatObject formatObject = formatMap.get(formatName);
				if (formatObject != null) textComponent = appendTextComponent(formatObject.getTextComponent(), textComponent);
				else textComponent = appendTextComponent(new TextComponentString("%" + formatName + "%"), textComponent);
				sb.delete(0, closePercent + 1);
			} else {
				textComponent = appendTextComponent(new TextComponentString(sb.toString()), textComponent);
				break;
			}
		}
		return textComponent;
	}

	private ITextComponent appendTextComponent(ITextComponent textComponent, ITextComponent appendTo) {
		if (appendTo == null) return textComponent;
		else return appendTo.appendSibling(textComponent);
	}

	public interface FormatObject {
		ITextComponent getTextComponent();
	}
}
