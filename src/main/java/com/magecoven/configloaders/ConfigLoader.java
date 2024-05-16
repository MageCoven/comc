package com.magecoven.configloaders;

import com.magecoven.ConfigData;

public interface ConfigLoader<T extends ConfigData> {
    public void save(T configData);
    public T load();
}
