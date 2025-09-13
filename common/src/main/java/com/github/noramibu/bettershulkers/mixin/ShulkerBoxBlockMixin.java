package com.github.noramibu.bettershulkers.mixin;

import com.github.noramibu.bettershulkers.Config;
import com.github.noramibu.bettershulkers.interfaces.MaterialDisplay;
import com.github.noramibu.bettershulkers.util.ShulkerUtil;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(ShulkerBoxBlock.class)
public abstract class ShulkerBoxBlockMixin extends BaseEntityBlock {
    protected ShulkerBoxBlockMixin(Properties settings) {
        super(settings);
    }

    @ModifyReturnValue(method = "getDrops", at = @At("RETURN"))
    private List<ItemStack> bettershulkers$onGetDrops(List<ItemStack> original, @Local(argsOnly = true) LootParams.Builder builder) {
        if (Config.ITEM_PICKUP_TYPE.equals(Config.PickupType.NONE)) {
            return original;
        }
        BlockEntity blockEntity = builder.getParameter(LootContextParams.BLOCK_ENTITY);

        Item material = ShulkerUtil.getMaterialFromShulkerBlock(blockEntity);
        if (material != null) {
            for (ItemStack stack : original) {
                if (ShulkerUtil.isShulkerBox(stack)) {
                    ShulkerUtil.setMaterialForShulker(stack, material.getDefaultInstance());
                    break;
                }
            }
        }
        return original;
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        ShulkerBoxBlockEntity blockEntity = (ShulkerBoxBlockEntity) world.getBlockEntity(pos);
        ((MaterialDisplay)blockEntity).createDisplay(itemStack);
        super.setPlacedBy(world, pos, state, placer, itemStack);
    }
}