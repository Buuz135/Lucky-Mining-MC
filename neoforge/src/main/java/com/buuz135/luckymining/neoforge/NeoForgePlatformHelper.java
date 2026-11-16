package com.buuz135.luckymining.neoforge;

import com.buuz135.luckymining.platform.services.PlatformHelper;
import net.neoforged.fml.loading.FMLPaths;

import java.nio.file.Path;

public final class NeoForgePlatformHelper implements PlatformHelper {

    @Override
    public Path configDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }
}
