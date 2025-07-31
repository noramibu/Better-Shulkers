package com.github.noramibu.bettershulkers.mixin;

import com.github.noramibu.bettershulkers.Config;
import com.github.noramibu.bettershulkers.enchantment.MaterialCollector;
import com.github.noramibu.bettershulkers.interfaces.ForceInventory;
import com.github.noramibu.bettershulkers.interfaces.MaterialDisplay;
import com.github.noramibu.bettershulkers.interfaces.RemoteInventory;
import com.github.noramibu.bettershulkers.interfaces.ShulkerViewer;
import com.github.noramibu.bettershulkers.util.ShulkerUtil;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Blocks;
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
public abstract class ShulkerBoxBlockMixin extends BaseEntityBlock implements RemoteInventory {
    protected ShulkerBoxBlockMixin(Properties settings) {
        super(settings);
    }

    @ModifyReturnValue(method = "getDrops", at = @At("RETURN"))
    private List<ItemStack> onGetDrops(List<ItemStack> original, @Local(argsOnly = true) LootParams.Builder builder) {
        if (Config.ITEM_PICKUP_TYPE.equals(Config.PickupType.NONE)) {
            return original;
        }
        BlockEntity blockEntity = builder.getParameter(LootContextParams.BLOCK_ENTITY);

        Item material = ShulkerUtil.getMaterialFromShulkerBlock(blockEntity);
        if (material != null) {
            for (ItemStack stack : original) {
                if (ShulkerUtil.isShulkerBox(stack)) {
                    ShulkerUtil.setMaterialForShulker(stack, material.getDefaultInstance());
                    if (Config.ITEM_PICKUP_TYPE.equals(Config.PickupType.ENCHANTMENT)) {
                        stack.enchant(Holder.direct(blockEntity.getLevel().getServer().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getValue(MaterialCollector.MATERIAL_COLLECTOR)), 1);
                    }
                    break;
                }
            }
        }
        return original;
    }

    @Override
    public void openInventory(ServerPlayer player, ItemStack stack) {
        ShulkerBoxBlockEntity blockEntity = new ShulkerBoxBlockEntity(player.blockPosition(), Blocks.SHULKER_BOX.defaultBlockState());
        ((BaseContainerBlockEntityAccessor)blockEntity).setName(stack.getDisplayName());
        ((ShulkerViewer) player).setViewing(stack, blockEntity);
        NonNullList<ItemStack> inventory = ShulkerUtil.getInventoryFromShulker(stack);
        ((ForceInventory)blockEntity).setInventory(inventory);
        ((ForceInventory)blockEntity).setForced();

        player.openMenu(blockEntity);
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        ShulkerBoxBlockEntity blockEntity = (ShulkerBoxBlockEntity) world.getBlockEntity(pos);
        ((MaterialDisplay)blockEntity).createDisplay(itemStack);
        super.setPlacedBy(world, pos, state, placer, itemStack);
    }
}