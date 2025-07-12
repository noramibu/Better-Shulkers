package com.github.noramibu.bettershulkers.fabric;

import com.github.noramibu.bettershulkers.BetterShulkers;
import com.github.noramibu.bettershulkers.abstraction.AbstractionManager;
import com.github.noramibu.bettershulkers.fabric.abstraction.FabricAbstraction;
import net.fabricmc.api.ModInitializer;

public final class BetterShulkersFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        AbstractionManager.ABSTRACTION = new FabricAbstraction();
        BetterShulkers.init();
    }
}
