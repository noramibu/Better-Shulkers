/**
 * Copyright (c) 2026 noramibu, QPCrummer
 * This project is Licensed under <a href="https://github.com/noramibu/Better-Shulkers/blob/main/LICENSE">MIT</a>
 */
package com.github.noramibu.bettershulkers.mixin;

import com.github.noramibu.bettershulkers.material.ItemDataStorage;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import java.util.List;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShulkerBoxBlock.class)
public abstract class ShulkerBoxBlockMixin extends BaseEntityBlock {
    private ItemStack from;

    protected ShulkerBoxBlockMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "getStateForPlacement", at = @At("HEAD"))
    private void bettershulkers$setStoredItem(BlockPlaceContext context, CallbackInfoReturnable<BlockState> cir) {
        this.from = context.getItemInHand();
    }

    // Save CustomData
    @ModifyReturnValue(
            method = "newBlockEntity",
            at = @At("RETURN")
    )
    private BlockEntity bettershulkers$modifyShulkerEntity(BlockEntity original) {
        if (from != null) {
            ((ItemDataStorage) original).storeItemData(this.from);
        }
        return original;
    }

    // Restore CustomData
    @ModifyReturnValue(method = "getDrops", at = @At("RETURN"))
    private List<ItemStack> onGetDrops(List<ItemStack> original, @Local(argsOnly = true) LootParams.Builder builder) {
        BlockEntity blockEntity = builder.getParameter(LootContextParams.BLOCK_ENTITY);
        if (original.size() == 1) {
            ItemStack item = original.getFirst();
            if (item.is(ItemTags.SHULKER_BOXES)) {
                CustomData data = ((ItemDataStorage) blockEntity).getData();
                if (data != null && !data.isEmpty()) {
                    item.set(DataComponents.CUSTOM_DATA, data);
                }

                ItemLore lore = ((ItemDataStorage) blockEntity).getLore();
                if (lore != null && !lore.lines().isEmpty()) {
                    item.set(DataComponents.LORE, lore);
                }

                ItemEnchantments enchantments = ((ItemDataStorage) blockEntity).getEnchantments();
                if (enchantments != null && !enchantments.isEmpty()) {
                    item.set(DataComponents.ENCHANTMENTS, enchantments);
                }
            }
        }

        return original;
    }

}
