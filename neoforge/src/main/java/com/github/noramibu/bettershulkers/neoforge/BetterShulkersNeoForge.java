package com.github.noramibu.bettershulkers.neoforge;

import com.github.noramibu.bettershulkers.BetterShulkers;
import com.github.noramibu.bettershulkers.abstraction.AbstractionManager;
import com.github.noramibu.bettershulkers.neoforge.abstraction.CommandPermission;
import com.github.noramibu.bettershulkers.neoforge.abstraction.NeoForgeAbstraction;
import com.github.noramibu.bettershulkers.util.ShulkerUtil;
import net.minecraft.core.component.DataComponents;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;
import net.neoforged.neoforge.server.permission.events.PermissionGatherEvent;

/**
 * The entrypoint for NeoForge
 */
@Mod(BetterShulkers.MOD_ID)
public final class BetterShulkersNeoForge {

    public BetterShulkersNeoForge(IEventBus modBus) {
        AbstractionManager.ABSTRACTION = new NeoForgeAbstraction();
        BetterShulkers.init();
        NeoForge.EVENT_BUS.addListener((final PermissionGatherEvent.Nodes nodes) -> {
            nodes.addNodes(CommandPermission.SET_COMMAND_NODE, CommandPermission.RELOAD_COMMAND_NODE, CommandPermission.OPEN_NODE);
        });
        modBus.addListener((final ModifyDefaultComponentsEvent event) -> {
            event.modifyMatching(ShulkerUtil::earlyIsShulkerBox, builder -> {
                builder.set(DataComponents.CUSTOM_DATA, BetterShulkers.getShulkerCustomData());
            });
        });
    }
}
