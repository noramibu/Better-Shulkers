package com.github.noramibu.bettershulkers.fabric;

import com.github.noramibu.bettershulkers.BetterShulkers;
import net.fabricmc.api.ModInitializer;

public final class BetterShulkersFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        BetterShulkers.init();
    }
}
