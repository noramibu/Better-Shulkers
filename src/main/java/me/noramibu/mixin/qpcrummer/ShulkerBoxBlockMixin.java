package me.noramibu.mixin.qpcrummer;

import me.noramibu.bettershulkers.accessor.ForceInventory;
import me.noramibu.bettershulkers.accessor.RemoteInventory;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ShulkerBoxBlock.class)
public abstract class ShulkerBoxBlockMixin implements RemoteInventory {
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
