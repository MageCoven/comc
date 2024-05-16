package com.magecoven.configloaders;

import com.magecoven.COMC;
import com.magecoven.ConfigData;
import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import net.fabricmc.loader.api.FabricLoader;

public class SimpleConfigLoader<T extends ConfigData> implements ConfigLoader<T> {
    private Path configPath;
    private Class<T> configClass;

    public SimpleConfigLoader(String modId, String configName, Class<T> configClass) {
        this.configPath = FabricLoader.getInstance().getConfigDir().resolve(modId).resolve(configName);
        this.configClass = configClass;
    }

    public void save(T configData) {
        COMC.LOGGER.info("Saving config file: " + configPath);

        try {
            Files.createDirectories(configPath.getParent());
            var tomlWriter = new TomlWriter();
            tomlWriter.write(configData, configPath.toFile());
        } catch (IOException e) {
            COMC.LOGGER.error("Failed to save config file: " + configPath, e);
        }
    }

    public T load() {
        COMC.LOGGER.info("Loading config file: " + configPath);
        try {
            Toml toml = new Toml().read(this.configPath.toFile());
            return toml.to(this.configClass);
        } catch (Exception e) {
            COMC.LOGGER.error("Failed to load config file: " + configPath + " Using default config.", e);
            return this.createDefaultConfig();
        }
    }

    private T createDefaultConfig() {
        try {
            var constructor = this.configClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            var defaultConfig = constructor.newInstance();

            this.save(defaultConfig);

            return defaultConfig;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load config file", e);
        }
    }
}
