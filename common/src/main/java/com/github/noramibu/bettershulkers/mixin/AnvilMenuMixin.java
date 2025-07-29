package com.github.noramibu.bettershulkers.mixin;

import com.github.noramibu.bettershulkers.Config;
import com.github.noramibu.bettershulkers.enchantment.MaterialCollector;
import com.github.noramibu.bettershulkers.util.ShulkerUtil;
import com.llamalad7.mixinextras.sugar.Local;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilMenu.class)
public abstract class AnvilMenuMixin extends ItemCombinerMenu {
    @Shadow @Final private DataSlot cost;

    public AnvilMenuMixin(@Nullable MenuType<?> menuType, int i, Inventory inventory, ContainerLevelAccess containerLevelAccess, ItemCombinerMenuSlotDefinition itemCombinerMenuSlotDefinition) {
        super(menuType, i, inventory, containerLevelAccess, itemCombinerMenuSlotDefinition);
    }

    @Inject(method = "createResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/ItemEnchantments$Mutable;<init>(Lnet/minecraft/world/item/enchantment/ItemEnchantments;)V"), cancellable = true)
    private void checkForShulkerMaterial(CallbackInfo ci, @Local(ordinal = 0) ItemStack stack1, @Local(ordinal = 2) ItemStack stack2) {
        if (Config.ITEM_PICKUP_TYPE.equals(Config.PickupType.ENCHANTMENT)) {
            ItemStack material = null;
            ItemStack shulker = null;

            if (ShulkerUtil.isShulkerBox(stack1)) {
                shulker = stack1;
            } else {
                material = stack1;
            }

            if (!stack2.isEmpty()) {
                if (shulker == null && ShulkerUtil.isShulkerBox(stack2)) {
                    shulker = stack2;
                } else {
                    material = stack2;
                }
            }

            if (material != null && shulker != null) {
                ItemEnchantments enchants = EnchantmentHelper.getEnchantmentsForCrafting(shulker);

                for (Object2IntMap.Entry<Holder<Enchantment>> entry : enchants.entrySet()) {
                    Holder<Enchantment> holder = entry.getKey();
                    if (holder.is(MaterialCollector.MATERIAL_COLLECTOR)) {
                        ItemStack clone = shulker.copy();
                        ShulkerUtil.setMaterialForShulker(clone, material);
                        this.cost.set(1);
                        this.resultSlots.setItem(0, clone);
                        ci.cancel();
                        break;
                    }
                }
            }
        }
    }
}
