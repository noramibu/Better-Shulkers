package me.noramibu.mixin.v1_21;

import com.moulberry.mixinconstraints.annotations.IfMinecraftVersion;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@IfMinecraftVersion(minVersion = "1.21")
@Mixin(ContainerComponent.class)
public interface ContainerComponentAccessor {
    @Accessor("stacks")
    DefaultedList<ItemStack> getStacks();
} 