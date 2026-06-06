/**
 * Copyright (c) 2026 noramibu, QPCrummer
 * This project is Licensed under <a href="https://github.com/noramibu/Better-Shulkers/blob/main/LICENSE">MIT</a>
 */
package com.github.noramibu.bettershulkers.material;

import java.util.*;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemLore;

public class ShulkerMaterialManager {
    private static final String MATERIAL_ID = "material";
    private static final String LORE_PREFIX = "Material: ";

    public static Item getMaterial(ItemStack shulker) {
        CustomData data = shulker.get(DataComponents.CUSTOM_DATA);
        if (data == null) {
            return null;
        } else {
            Optional<Integer> material = data.copyTag().getInt(MATERIAL_ID);
            return material.map(Item::byId).orElse(null);
        }
    }

    public static void setMaterial(ItemStack shulker, Item material) {
        CustomData data = shulker.get(DataComponents.CUSTOM_DATA);
        ItemLore lore = shulker.get(DataComponents.LORE);

        final boolean[] hasMaterial = {false};
        if (data == null) {
            CompoundTag tag = new CompoundTag();
            tag.putInt(MATERIAL_ID, Item.getId(material));
            shulker.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        } else {
            data.update(tag -> {
                hasMaterial[0] = tag.contains(MATERIAL_ID);
                tag.putInt(MATERIAL_ID, Item.getId(material));
            });
        }

        Component line = Component.literal(LORE_PREFIX).append(getTranslatedItem(material));
        if (lore == null) {
            shulker.set(DataComponents.LORE, new ItemLore(List.of(line)));
            return;
        } else if (hasMaterial[0]) {
            // Search for Lore
            for (int i = 0; i < lore.lines().size(); i++) {
                Component heldLine = lore.lines().get(i);
                if (heldLine.getString().startsWith(LORE_PREFIX)) {
                    lore.lines().set(i, line);
                    return;
                }
            }
        }

        // Add new lore as last effort
        shulker.set(DataComponents.LORE, lore.withLineAdded(line));
    }

    private static Component getTranslatedItem(Item item) {
        String translationKey = item.getDescriptionId();
        return Component.translatable(translationKey);
    }

    public static void removeMaterial(ItemStack shulker) {
        CustomData data = shulker.get(DataComponents.CUSTOM_DATA);
        ItemLore lore = shulker.get(DataComponents.LORE);

        if (data != null) {
            CompoundTag tag = data.copyTag();
            tag.remove(MATERIAL_ID);
            shulker.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        }

        if (lore != null) {
            for (int i = 0; i < lore.lines().size(); i++) {
                Component heldLine = lore.lines().get(i);
                if (heldLine.getString().startsWith(LORE_PREFIX)) {
                    lore.lines().remove(i);
                    return;
                }
            }
        }
    }

    public static boolean matchesMaterialFilter(ItemStack shulker, ItemStack test, boolean allowNoMaterial) {
        Item material = getMaterial(shulker);
        return material == null ? allowNoMaterial : test.is(material);
    }
}
