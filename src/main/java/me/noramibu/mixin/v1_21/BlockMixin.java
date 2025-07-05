package me.noramibu.mixin.v1_21;

import me.noramibu.bettershulkers.accessor.ShulkerMaterialAccessor;
import me.noramibu.bettershulkers.util.ShulkerUtil;
import me.noramibu.mixin.annotation.MCVer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@MCVer(min = "1.21", max = "1.21.7")
@Mixin(Block.class)
public class BlockMixin {
    @Inject(method = "onPlaced", at = @At("TAIL"))
    private void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack, CallbackInfo ci) {
        if (world.isClient) {
            return;
        }

        if (state.getBlock() instanceof ShulkerBoxBlock) {
            ShulkerUtil.getShulkerMaterial(itemStack).ifPresent(materialId -> {
                BlockEntity blockEntity = world.getBlockEntity(pos);
                if (blockEntity instanceof ShulkerMaterialAccessor accessor) {
                    accessor.setMaterial(materialId);
                }
            });
        }
    }
} 