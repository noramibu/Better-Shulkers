package com.github.noramibuu.bettershulkers.fabric;

import com.github.noramibuu.bettershulkers.BetterShulkers;
import net.fabricmc.api.ModInitializer;

public final class BetterShulkersFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        BetterShulkers.init();
    }
}
