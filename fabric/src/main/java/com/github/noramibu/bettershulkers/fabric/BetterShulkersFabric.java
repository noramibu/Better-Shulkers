package com.github.noramibu.bettershulkers.fabric;

import com.github.noramibu.bettershulkers.BetterShulkers;
import com.github.noramibu.bettershulkers.abstraction.AbstractionManager;
import com.github.noramibu.bettershulkers.fabric.abstraction.FabricAbstraction;
import com.github.noramibu.bettershulkers.util.ShulkerUtil;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.minecraft.core.component.DataComponents;

/**
 * The entrypoint for Fabric
 */
public final class BetterShulkersFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        AbstractionManager.ABSTRACTION = new FabricAbstraction();
        BetterShulkers.init();
        BetterShulkers.register();
        DefaultItemComponentEvents.MODIFY.register(modifyContext -> {
            modifyContext.modify(ShulkerUtil::earlyIsShulkerBox, (builder, item) -> {
                builder.set(DataComponents.CUSTOM_DATA, BetterShulkers.getShulkerCustomData());
                builder.build();
            });
        });
    }
}
