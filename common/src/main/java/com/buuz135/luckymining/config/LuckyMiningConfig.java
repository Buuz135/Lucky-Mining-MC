package com.buuz135.luckymining.config;

import com.buuz135.luckymining.LuckyMining;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public final class LuckyMiningConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private double tier1Chance = 0.10;
    private double tier2Chance = 0.15;
    private double tier3Chance = 0.20;

    public static LuckyMiningConfig load(Path path) {
        LuckyMiningConfig config = new LuckyMiningConfig();
        if (Files.exists(path)) {
            try (Reader reader = Files.newBufferedReader(path)) {
                LuckyMiningConfig loaded = GSON.fromJson(reader, LuckyMiningConfig.class);
                if (loaded != null) {
                    config = loaded;
                }
            } catch (IOException | JsonParseException exception) {
                LuckyMining.LOGGER.error("Could not read config from {}", path, exception);
            }
        }

        boolean changed = config.validate();
        if (!Files.exists(path) || changed) {
            config.save(path);
        }
        return config;
    }

    public double chance(int level) {
        return switch (Math.clamp(level, 1, 3)) {
            case 1 -> tier1Chance;
            case 2 -> tier2Chance;
            default -> tier3Chance;
        };
    }

    private boolean validate() {
        boolean changed = false;
        if (!validChance(tier1Chance)) {
            tier1Chance = 0.10;
            changed = true;
        }
        if (!validChance(tier2Chance)) {
            tier2Chance = 0.15;
            changed = true;
        }
        if (!validChance(tier3Chance)) {
            tier3Chance = 0.20;
            changed = true;
        }
        return changed;
    }

    private boolean validChance(double chance) {
        return Double.isFinite(chance) && chance >= 0.0 && chance <= 1.0;
    }

    private void save(Path path) {
        try {
            Files.createDirectories(path.getParent());
            try (Writer writer = Files.newBufferedWriter(path)) {
                GSON.toJson(this, writer);
            }
        } catch (IOException exception) {
            LuckyMining.LOGGER.error("Could not write config to {}", path, exception);
        }
    }
}
