package com.buuz135.luckymining;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public final class LuckyMiningTags {

    public static final TagKey<Block> ORES = tag("ores");
    public static final TagKey<Block> STONES = tag("stones");

    private LuckyMiningTags() {
    }

    private static TagKey<Block> tag(String path) {
        return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(LuckyMining.MOD_ID, path));
    }
}
