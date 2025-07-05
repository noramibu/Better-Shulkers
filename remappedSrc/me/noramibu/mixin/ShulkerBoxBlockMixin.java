package me.noramibu.mixin;

import me.noramibu.bettershulkers.accessor.ShulkerMaterialAccessor;
import me.noramibu.bettershulkers.util.ShulkerUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootWorldContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ShulkerBoxBlock.class)
public class ShulkerBoxBlockMixin {

    @Inject(method = "getDroppedStacks", at = @At("RETURN"))
    private void onGetDroppedStacks(BlockState state, LootWorldContext.Builder builder, CallbackInfoReturnable<List<ItemStack>> cir) {
        BlockEntity blockEntity = builder.get(LootContextParameters.BLOCK_ENTITY);

        if (blockEntity instanceof ShulkerMaterialAccessor accessor) {
            String material = accessor.getMaterial();
            if (material != null && !material.isEmpty()) {
                List<ItemStack> stacks = cir.getReturnValue();
                for (ItemStack stack : stacks) {
                    if (ShulkerUtil.isShulkerBox(stack)) {
                        ShulkerUtil.setShulkerMaterial(stack, material);
                        break; // Assume only one shulker box drops
                    }
                }
            }
        }
    }
} 