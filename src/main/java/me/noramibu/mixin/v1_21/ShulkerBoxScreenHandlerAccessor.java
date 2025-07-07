package me.noramibu.mixin.v1_21;

import com.moulberry.mixinconstraints.annotations.IfMinecraftVersion;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.ShulkerBoxScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@IfMinecraftVersion(minVersion = "1.21")
@Mixin(ShulkerBoxScreenHandler.class)
public interface ShulkerBoxScreenHandlerAccessor {
    @Accessor("inventory")
    Inventory getInventory();
} 