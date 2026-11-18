package com.buuz135.luckymining;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.DustColorTransitionOptions;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public final class LuckyMiningHandler {

    private static final long STREAK_TIMEOUT_MILLIS = 3_000L;
    private static final int MAX_SCALING_STEPS = 10;
    private static final DustColorTransitionOptions SPREAD_PARTICLE = new DustColorTransitionOptions(
            0x54F841,
            0xFFD000,
            0.5F
    );
    private static final Map<ServerPlayer, MiningStreak> MINING_STREAKS = new WeakHashMap<>();

    private LuckyMiningHandler() {
    }

    public static void onBlockBroken(ServerLevel level, ServerPlayer player, BlockPos minedPos, BlockState minedState) {
        long now = Util.getMillis();
        MiningStreak streak = MINING_STREAKS.get(player);
        if (streak != null && (streak.block() != minedState.getBlock() || now - streak.lastBreakMillis() > STREAK_TIMEOUT_MILLIS)) {
            MINING_STREAKS.remove(player);
            streak = null;
        }

        if (!minedState.is(LuckyMiningTags.ORES) || !player.hasCorrectToolForDrops(minedState)) {
            return;
        }

        Registry<Enchantment> enchantments = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        Holder<Enchantment> enchantment = enchantments.wrapAsHolder(enchantments.getValueOrThrow(LuckyMining.ENCHANTMENT));
        int enchantmentLevel = EnchantmentHelper.getItemEnchantmentLevel(enchantment, player.getMainHandItem());
        if (enchantmentLevel == 0) {
            return;
        }

        double tierChance = LuckyMining.config().chance(enchantmentLevel);
        int scalingSteps = streak == null ? 0 : streak.scalingSteps();
        double maximumChance = Math.min(1.0, tierChance * 2.0);
        double currentChance = Math.clamp(
                tierChance + (scalingSteps + player.getLuck()) * tierChance / 10.0,
                0.0,
                maximumChance
        );
        MINING_STREAKS.put(player, new MiningStreak(
                minedState.getBlock(),
                Math.min(MAX_SCALING_STEPS, scalingSteps + 1),
                now
        ));

        if (level.getRandom().nextDouble() >= currentChance) {
            return;
        }

        List<BlockPos> candidates = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            BlockPos candidate = minedPos.relative(direction);
            if (level.getBlockState(candidate).is(LuckyMiningTags.STONES)) {
                candidates.add(candidate);
            }
        }

        if (candidates.isEmpty()) {
            return;
        }

        BlockPos target = candidates.get(level.getRandom().nextInt(candidates.size()));
        if (!level.setBlockAndUpdate(target, minedState)) {
            return;
        }

        spawnParticles(level, target);
        level.playSound(null, target, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, 10.0F, 1.0F);
    }

    private static void spawnParticles(ServerLevel level, BlockPos target) {
        for (int xOffset = 0; xOffset <= 10; xOffset++) {
            for (int zOffset = 0; zOffset <= 10; zOffset++) {
                double x = target.getX() + xOffset / 10.0;
                double z = target.getZ() + zOffset / 10.0;
                double risingSpeed = 0.2 + level.getRandom().nextDouble() * 0.4;
                double stillSpeed = 0.01 + level.getRandom().nextDouble() * 0.04;
                level.sendParticles(SPREAD_PARTICLE, x, target.getY(), z, 0, 0.0, 1.0, 0.0, risingSpeed);
                level.sendParticles(SPREAD_PARTICLE, x, target.getY() + 1.0, z, 0, 0.0, 1.0, 0.0, stillSpeed);
            }
        }
    }

    private record MiningStreak(Block block, int scalingSteps, long lastBreakMillis) {
    }
}
