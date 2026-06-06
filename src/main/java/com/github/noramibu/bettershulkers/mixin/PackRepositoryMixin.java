/**
 * Copyright (c) 2026 noramibu, QPCrummer
 * This project is Licensed under <a href="https://github.com/noramibu/Better-Shulkers/blob/main/LICENSE">MIT</a>
 */
package com.github.noramibu.bettershulkers.mixin;

import com.github.noramibu.bettershulkers.BetterShulkers;
import com.github.noramibu.bettershulkers.BetterShulkersPack;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackSelectionConfig;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.PackSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PackRepository.class)
public class PackRepositoryMixin {
    private static final PackLocationInfo BETTERSHULKERS_PACK_LOCATION = new PackLocationInfo(
            BetterShulkers.MOD_ID + "_mod",
            Component.literal("Better Shulkers"),
            PackSource.BUILT_IN,
            Optional.empty());

    private static final PackSelectionConfig BETTERSHULKERS_SELECTION_CONFIG =
            new PackSelectionConfig(true, Pack.Position.TOP, false);

    private static final Pack.ResourcesSupplier BETTERSHULKERS_PACK_SUPPLIER = new Pack.ResourcesSupplier() {
        @Override
        public PackResources openPrimary(PackLocationInfo info) {
            return new BetterShulkersPack(info);
        }

        @Override
        public PackResources openFull(PackLocationInfo info, Pack.Metadata metadata) {
            return new BetterShulkersPack(info);
        }
    };

    @Inject(method = "discoverAvailable", at = @At("RETURN"), cancellable = true)
    private void bettershulkers$addInternalPack(CallbackInfoReturnable<Map<String, Pack>> cir) {
        Pack pack = Pack.readMetaAndCreate(
                BETTERSHULKERS_PACK_LOCATION,
                BETTERSHULKERS_PACK_SUPPLIER,
                PackType.SERVER_DATA,
                BETTERSHULKERS_SELECTION_CONFIG);

        if (pack == null) {
            BetterShulkers.LOGGER.warn("Failed to create Better Shulkers internal datapack");
            return;
        }

        Map<String, Pack> discovered = new TreeMap<>(cir.getReturnValue());
        discovered.put(pack.getId(), pack);
        cir.setReturnValue(discovered);
    }
}
