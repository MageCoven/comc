package com.magecoven;

import net.fabricmc.api.ModInitializer;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.magecoven.configloaders.ConfigLoader;
import com.magecoven.configloaders.SimpleConfigLoader;

public class COMC implements ModInitializer {
	public static final String MOD_ID = "comc";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private static HashMap<String, ConfigLoader<? extends ConfigData>> configLoaders = new HashMap<>();

	public static <T extends ConfigData> ConfigLoader<T> RegisterSimpleConfigLoader(String modId, String configName,
			Class<T> configClass) {
		
		var key = modId + ":" + configName;
		if (configLoaders.containsKey(key)) {
			throw new RuntimeException("Config loader already registered for " + key);
		}
		
		var configLoader = new SimpleConfigLoader<>(modId, configName, configClass);
		configLoaders.put(key, configLoader);

		return configLoader;
	}

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("ConfigLoader Mod Initialized! :)");
	}
}