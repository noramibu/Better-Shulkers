package me.noramibu.mixin.v1_21;

import me.noramibu.bettershulkers.accessor.ShulkerMaterialAccessor;
import me.noramibu.mixin.annotation.MCVer;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@MCVer(min = "1.21")
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
} 