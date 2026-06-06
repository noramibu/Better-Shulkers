/**
 * Copyright (c) 2026 noramibu, QPCrummer
 * This project is Licensed under <a href="https://github.com/noramibu/Better-Shulkers/blob/main/LICENSE">MIT</a>
 */
package com.github.noramibu.bettershulkers.mixin;

import com.github.noramibu.bettershulkers.material.ItemDataStorage;
import com.github.noramibu.bettershulkers.material.ShulkerMaterialManager;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ShulkerBoxBlockEntity.class)
public class ShulkerBoxBlockEntityMixin implements ItemDataStorage {
    private static final String LORE_ID = "lore";
    private CustomData data = CustomData.EMPTY;
    private ItemLore lore = ItemLore.EMPTY;

    @Inject(method = "loadAdditional", at = @At("HEAD"))
    private void bettershulkers$loadCustomData(ValueInput input, CallbackInfo ci) {
        this.data = input.read(ShulkerMaterialManager.MATERIAL_ID, CustomData.CODEC)
                .orElse(CustomData.EMPTY);
        this.lore = input.read(LORE_ID, ItemLore.CODEC)
                .orElse(ItemLore.EMPTY);
    }

    @Inject(method = "saveAdditional", at = @At("HEAD"))
    private void bettershulkers$saveCustomData(ValueOutput output, CallbackInfo ci) {
        output.store(ShulkerMaterialManager.MATERIAL_ID, CustomData.CODEC, this.data);
        output.store(LORE_ID, ItemLore.CODEC, this.lore);
    }

    @Override
    public void storeItemData(ItemStack stack) {
        this.data = stack.get(DataComponents.CUSTOM_DATA);
        this.lore = stack.get(DataComponents.LORE);
    }

    @Override
    public CustomData getData() {
        return this.data;
    }

    @Override
    public ItemLore getLore() {
        return this.lore;
    }
}
