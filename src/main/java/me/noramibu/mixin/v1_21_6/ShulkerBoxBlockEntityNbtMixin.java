package me.noramibu.mixin.v1_21_6;

import com.moulberry.mixinconstraints.annotations.IfMinecraftVersion;
import me.noramibu.bettershulkers.accessor.ShulkerMaterialAccessor;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@IfMinecraftVersion(minVersion = "1.21.6")
@Mixin(ShulkerBoxBlockEntity.class)
public abstract class ShulkerBoxBlockEntityNbtMixin {
    @Inject(method = "readNbt(Lnet/minecraft/class_2487;)V", at = @At("HEAD"), require = 0)
    private void readNbt(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("ShulkerMaterial")) {
            ((ShulkerMaterialAccessor) this).setMaterial(nbt.getString("ShulkerMaterial"));
        }
    }

    @Inject(method = "writeNbt(Lnet/minecraft/class_2487;)V", at = @At("HEAD"), require = 0)
    private void writeNbt(NbtCompound nbt, CallbackInfo ci) {
        String material = ((ShulkerMaterialAccessor) this).getMaterial();
        if (material != null) {
            nbt.putString("ShulkerMaterial", material);
        }
    }
} 