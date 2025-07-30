package com.github.noramibu.bettershulkers.mixin;

import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BaseContainerBlockEntity.class)
public interface BaseContainerBlockEntityAccessor {
    @Accessor("name")
    void setName(Component component);
}
