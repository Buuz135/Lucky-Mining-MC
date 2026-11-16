package com.buuz135.luckymining.neoforge;

import com.buuz135.luckymining.LuckyMining;
import com.buuz135.luckymining.LuckyMiningHandler;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.BlockEvent;

@Mod(LuckyMining.MOD_ID)
public final class LuckyMiningNeoForge {

    public LuckyMiningNeoForge(IEventBus modEventBus) {
        LuckyMining.init();
        NeoForge.EVENT_BUS.addListener(EventPriority.LOWEST, this::onBlockBroken);
    }

    private void onBlockBroken(BlockEvent.BreakEvent event) {
        if (event.getLevel() instanceof ServerLevel level && event.getPlayer() instanceof ServerPlayer player) {
            LuckyMiningHandler.onBlockBroken(level, player, event.getPos(), event.getState());
        }
    }
}
