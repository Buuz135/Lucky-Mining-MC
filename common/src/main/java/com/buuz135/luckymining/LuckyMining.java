package com.buuz135.luckymining;

import com.buuz135.luckymining.config.LuckyMiningConfig;
import com.buuz135.luckymining.platform.Services;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class LuckyMining {

    public static final String MOD_ID = "luckymining";
    public static final String MOD_NAME = "Lucky Mining";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);
    public static final ResourceKey<Enchantment> ENCHANTMENT = ResourceKey.create(
            Registries.ENCHANTMENT,
            ResourceLocation.fromNamespaceAndPath(MOD_ID, "lucky_mining")
    );
    private static LuckyMiningConfig config;

    private LuckyMining() {
    }

    public static void init() {
        config = LuckyMiningConfig.load(Services.PLATFORM.configDirectory().resolve("luckymining.json"));
        LOGGER.info("Loaded {}", MOD_NAME);
    }

    public static LuckyMiningConfig config() {
        return config;
    }
}
