package me.noramibu.bettershulkers.mixin;

import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(DisplayEntity.ItemDisplayEntity.class)
public interface ItemDisplayEntityInvoker {
    @Invoker("setItemStack")
    void invokeSetItemStack(ItemStack stack);

    @Invoker("setItemDisplayContext")
    void invokeSetTransformationMode(ItemDisplayContext mode);

}
