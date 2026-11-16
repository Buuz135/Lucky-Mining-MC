package com.buuz135.luckymining.platform;

import com.buuz135.luckymining.platform.services.PlatformHelper;

import java.util.ServiceLoader;

public final class Services {

    public static final PlatformHelper PLATFORM = ServiceLoader.load(PlatformHelper.class)
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("No Lucky Mining platform service found"));

    private Services() {
    }
}
