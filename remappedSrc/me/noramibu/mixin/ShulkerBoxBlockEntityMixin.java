package me.noramibu.mixin;

import me.noramibu.bettershulkers.accessor.ShulkerMaterialAccessor;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ShulkerBoxBlockEntity.class)
public abstract class ShulkerBoxBlockEntityMixin implements ShulkerMaterialAccessor {

    @Unique
    private String material;

    @Override
    public void setMaterial(String material) {
        this.material = material;
    }

    @Override
    public String getMaterial() {
        return this.material;
    }

    @Inject(method = "readNbt", at = @At("TAIL"))
    private void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup, CallbackInfo ci) {
        if (nbt.contains("ShulkerMaterial")) {
            this.material = nbt.getString("ShulkerMaterial");
        }
    }

    @Inject(method = "writeNbt", at = @At("TAIL"))
    private void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup, CallbackInfo ci) {
        if (this.material != null) {
            nbt.putString("ShulkerMaterial", this.material);
        }
    }
} 