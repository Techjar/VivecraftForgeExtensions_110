package com.techjar.vivecraftforge.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogHelper {
	private static final Logger logger = LogManager.getLogger("Vivecraft Forge Extensions");

	public static void info(String message, Object... data) {
		logger.info(message, data);
	}

	public static void warning(String message, Object... data) {
		logger.warn(message, data);
	}

	public static void severe(String message, Object... data) {
		logger.error(message, data);
	}

	public static void debug(String message, Object... data) {
		logger.debug(message, data);
	}
}
