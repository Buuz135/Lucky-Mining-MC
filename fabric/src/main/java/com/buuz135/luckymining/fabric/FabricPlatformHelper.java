package com.buuz135.luckymining.fabric;

import com.buuz135.luckymining.platform.services.PlatformHelper;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public final class FabricPlatformHelper implements PlatformHelper {

    @Override
    public Path configDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }
}
