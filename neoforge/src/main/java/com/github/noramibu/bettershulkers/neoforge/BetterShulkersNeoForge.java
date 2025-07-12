package com.github.noramibu.bettershulkers.neoforge;

import com.github.noramibu.bettershulkers.BetterShulkers;
import com.github.noramibu.bettershulkers.abstraction.AbstractionManager;
import com.github.noramibu.bettershulkers.neoforge.abstraction.NeoForgeAbstraction;
import net.neoforged.fml.common.Mod;

@Mod(BetterShulkers.MOD_ID)
public final class BetterShulkersNeoForge {
    public BetterShulkersNeoForge() {
        AbstractionManager.ABSTRACTION = new NeoForgeAbstraction();
        BetterShulkers.init();
    }
}
