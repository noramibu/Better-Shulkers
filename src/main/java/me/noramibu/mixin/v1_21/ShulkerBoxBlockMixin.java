package me.noramibu.mixin.v1_21;

import me.noramibu.bettershulkers.accessor.ShulkerMaterialAccessor;
import me.noramibu.bettershulkers.util.ShulkerUtil;
import me.noramibu.mixin.annotation.MCVer;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@MCVer(min = "1.21", max = "1.21.7")
@Mixin(ShulkerBoxBlock.class)
public abstract class ShulkerBoxBlockMixin {
    @Inject(method = "getDroppedStacks", at = @At("RETURN"))
    private static void onGetDroppedStacks(BlockState state, LootContextParameterSet.Builder builder, CallbackInfoReturnable<List<ItemStack>> cir) {
        BlockEntity blockEntity = builder.get(LootContextParameters.BLOCK_ENTITY);

        if (blockEntity instanceof ShulkerMaterialAccessor accessor) {
            String material = accessor.getMaterial();
            if (material != null && !material.isEmpty()) {
                List<ItemStack> stacks = cir.getReturnValue();
                for (ItemStack stack : stacks) {
                    if (ShulkerUtil.isShulkerBox(stack)) {
                        ShulkerUtil.setShulkerMaterial(stack, material);
                        break;
                    }
                }
            }
        }
    }
} 