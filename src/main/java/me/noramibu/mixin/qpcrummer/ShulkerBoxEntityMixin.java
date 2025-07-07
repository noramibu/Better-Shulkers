package me.noramibu.mixin.qpcrummer;

import me.noramibu.bettershulkers.accessor.ForceInventory;
import me.noramibu.bettershulkers.accessor.ShulkerViewer;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ShulkerBoxBlockEntity.class)
public abstract class ShulkerBoxEntityMixin extends LootableContainerBlockEntity implements ForceInventory {

    @Shadow private DefaultedList<ItemStack> inventory;
    private boolean forced;

    protected ShulkerBoxEntityMixin(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    @Override
    public void setInventory(DefaultedList<ItemStack> inventory) {
        this.inventory = inventory;
    }

    @Override
    public void setForced() {
        this.forced = true;
    }

    @Override
    public boolean forced() {
        return this.forced;
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private static void skipTicks(World world, BlockPos pos, BlockState state, ShulkerBoxBlockEntity blockEntity, CallbackInfo ci) {
        if (((ForceInventory)blockEntity).forced()) {
            ci.cancel();
        }
    }

    @Inject(method = "onClose", at = @At("HEAD"), cancellable = true)
    private void removeWhenClosed(PlayerEntity player, CallbackInfo ci) {
        if (this.forced()) {
            if (player instanceof ServerPlayerEntity serverPlayer) {
                System.out.println(((ShulkerViewer)serverPlayer).getViewedStack());
                int smallestSize = this.getSmallestListIndex();
                if (smallestSize != -1) {
                    DefaultedList<ItemStack> newInventory = DefaultedList.ofSize(smallestSize + 1, ItemStack.EMPTY);
                    for (int i = 0; i <= smallestSize; i++) {
                        newInventory.set(i, this.inventory.get(i));
                    }
                    ((ShulkerViewer)serverPlayer).getViewedStack().set(DataComponentTypes.CONTAINER, ContainerComponent.fromStacks(newInventory));
                } else {
                    ((ShulkerViewer)serverPlayer).getViewedStack().set(DataComponentTypes.CONTAINER, ContainerComponent.fromStacks(List.of()));
                }
                ((ShulkerViewer)serverPlayer).setViewing(null);
            }
            this.markRemoved();
            ci.cancel();
        }
    }

    private int getSmallestListIndex() {
        int lastIndex = -1;
        for (int i = 0; i < 27; i++) {
            if (this.inventory.get(i) != ItemStack.EMPTY) {
                lastIndex = i;
            }
        }
        return lastIndex;
    }

    @Inject(method = "onOpen", at = @At("HEAD"), cancellable = true)
    private void ignoreOpening(PlayerEntity player, CallbackInfo ci) {
        if (this.forced()) {
            ci.cancel();
        }
    }
}
