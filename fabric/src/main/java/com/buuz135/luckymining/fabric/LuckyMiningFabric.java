package com.buuz135.luckymining.fabric;

import com.buuz135.luckymining.LuckyMining;
import com.buuz135.luckymining.LuckyMiningHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public final class LuckyMiningFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        LuckyMining.init();
        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
            if (world instanceof ServerLevel level && player instanceof ServerPlayer serverPlayer) {
                LuckyMiningHandler.onBlockBroken(level, serverPlayer, pos, state);
            }
        });
    }
}
