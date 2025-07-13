package com.github.noramibu.bettershulkers.neoforge;

import com.github.noramibu.bettershulkers.BetterShulkers;
import com.github.noramibu.bettershulkers.abstraction.AbstractionManager;
import com.github.noramibu.bettershulkers.neoforge.abstraction.CommandPermission;
import com.github.noramibu.bettershulkers.neoforge.abstraction.NeoForgeAbstraction;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.server.permission.events.PermissionGatherEvent;

@Mod(BetterShulkers.MOD_ID)
public final class BetterShulkersNeoForge {
    public BetterShulkersNeoForge() {
        AbstractionManager.ABSTRACTION = new NeoForgeAbstraction();
        BetterShulkers.init();
        PermissionGatherEvent.Nodes nodes = new PermissionGatherEvent.Nodes();
        nodes.addNodes(CommandPermission.SET_COMMAND_NODE, CommandPermission.RELOAD_COMMAND_NODE);
    }
}
