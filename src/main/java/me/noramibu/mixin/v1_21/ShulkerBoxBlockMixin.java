package me.noramibu.mixin.v1_21;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.moulberry.mixinconstraints.annotations.IfMinecraftVersion;
import me.noramibu.bettershulkers.accessor.ForceInventory;
import me.noramibu.bettershulkers.accessor.RemoteInventory;
import me.noramibu.bettershulkers.accessor.ShulkerMaterialAccessor;
import me.noramibu.bettershulkers.util.ShulkerUtil;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@IfMinecraftVersion(minVersion = "1.21")
@Mixin(ShulkerBoxBlock.class)
public abstract class ShulkerBoxBlockMixin implements RemoteInventory {
    @ModifyReturnValue(method = "getDroppedStacks", at = @At("RETURN"))
    private List<ItemStack> onGetDroppedStacks(List<ItemStack> original, @Local(argsOnly = true) LootContextParameterSet.Builder builder) {
        BlockEntity blockEntity = builder.get(LootContextParameters.BLOCK_ENTITY);

        if (blockEntity instanceof ShulkerMaterialAccessor accessor) {
            String material = accessor.getMaterial();
            if (material != null && !material.isEmpty()) {
                for (ItemStack stack : original) {
                    if (ShulkerUtil.isShulkerBox(stack)) {
                        ShulkerUtil.setShulkerMaterial(stack, material);
                        break;
                    }
                }
            }
        }
        return original;
    }

    @Override
    public void openInventory(ServerPlayerEntity player, ItemStack stack) {
        ShulkerBoxBlockEntity blockEntity = new ShulkerBoxBlockEntity(player.getBlockPos(), Blocks.SHULKER_BOX.getDefaultState());
        DefaultedList<ItemStack> inventoryFrom = ((ContainerComponentAccessor)(Object)stack.getComponents().get(DataComponentTypes.CONTAINER)).getStacks();
        DefaultedList<ItemStack> inventory = DefaultedList.ofSize(27, ItemStack.EMPTY);
        for (int i = 0; i < 27; i++) {
            if (i < inventoryFrom.size()) {
                inventory.set(i, inventoryFrom.get(i));
            }
        }
        ((ForceInventory)blockEntity).setInventory(inventory);
        ((ForceInventory)blockEntity).setForced();
        player.openHandledScreen(blockEntity);
    }
} 