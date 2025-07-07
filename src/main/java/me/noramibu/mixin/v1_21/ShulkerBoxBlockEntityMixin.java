package me.noramibu.mixin.v1_21;

import com.moulberry.mixinconstraints.annotations.IfMinecraftVersion;
import me.noramibu.bettershulkers.accessor.ForceInventory;
import me.noramibu.bettershulkers.accessor.ShulkerViewer;
import net.minecraft.block.BlockState;
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
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@IfMinecraftVersion(minVersion = "1.21")
@Mixin(ShulkerBoxBlockEntity.class)
public abstract class ShulkerBoxBlockEntityMixin extends LootableContainerBlockEntity implements ForceInventory {

    @Shadow
    private DefaultedList<ItemStack> inventory;
    @Unique
    private boolean forced;

    protected ShulkerBoxBlockEntityMixin(net.minecraft.block.entity.BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
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

    @Unique
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