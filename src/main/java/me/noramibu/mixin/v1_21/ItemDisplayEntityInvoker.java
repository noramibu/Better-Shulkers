package me.noramibu.mixin.v1_21;

import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(DisplayEntity.ItemDisplayEntity.class)
public interface ItemDisplayEntityInvoker {
    @Invoker("setItemStack")
    void invokeSetItemStack(ItemStack stack);

    @Invoker("setTransformationMode")
    void invokeSetTransformationMode(ModelTransformationMode mode);

}
