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

/**
 * Contains all methods for interfacing with Shulker materials
 */
public class ShulkerMaterialManager {
    /**
     * The ID for storing materials in Components
     */
    public static final String MATERIAL_ID = "material";
    private static final String LORE_PREFIX = "Material: ";

    /**
     * Gets the material from a Shulker Box item
     * @param shulker Shulker Box ItemStack
     * @return The type of material as an Item
     */
    public static Item getMaterial(ItemStack shulker) {
        CustomData data = shulker.get(DataComponents.CUSTOM_DATA);
        return getMaterial(data);
    }

    /**
     * Gets the material from ItemStack data
     * @param data CustomData from an ItemStack (typically stored in a BlockEntity)
     * @return The type of material as an Item
     */
    public static Item getMaterial(CustomData data) {
        if (data == null) {
            return null;
        } else {
            Optional<Integer> material = data.copyTag().getInt(MATERIAL_ID);
            return material.map(Item::byId).orElse(null);
        }
    }

    /**
     * Sets the material for a Shulker Box item
     * @param shulker Shulker Box ItemStack
     * @param material The type of material to set
     */
    public static void setMaterial(ItemStack shulker, Item material) {
        CustomData data = shulker.get(DataComponents.CUSTOM_DATA);
        ItemLore lore = shulker.get(DataComponents.LORE);

        final boolean[] hasMaterial = {false};
        CompoundTag tag;
        if (data == null || data.isEmpty()) {
            tag = new CompoundTag();
        } else {
            tag = data.copyTag();
            hasMaterial[0] = tag.contains(MATERIAL_ID);
        }
        tag.putInt(MATERIAL_ID, Item.getId(material));
        shulker.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));

        Component line = Component.literal(LORE_PREFIX).append(getTranslatedItem(material));
        if (lore == null || lore.lines().isEmpty()) {
            shulker.set(DataComponents.LORE, new ItemLore(List.of(line)));
        } else if (hasMaterial[0]) {
            List<Component> newLines = new ArrayList<>(lore.lines());
            boolean found = false;

            for (int i = 0; i < newLines.size(); i++) {
                Component heldLine = newLines.get(i);
                if (heldLine.getString().startsWith(LORE_PREFIX)) {
                    newLines.set(i, line);
                    found = true;
                    break;
                }
            }

            if (!found) {
                newLines.add(line);
            }

            ItemLore newLore = new ItemLore(newLines);
            shulker.set(DataComponents.LORE, newLore);
        }
    }

    private static Component getTranslatedItem(Item item) {
        String translationKey = item.getDescriptionId();
        return Component.translatable(translationKey);
    }

    /**
     * Removes a material from a Shulker Box item
     * @param shulker Shulker Box ItemStack
     */
    public static void removeMaterial(ItemStack shulker) {
        CustomData data = shulker.get(DataComponents.CUSTOM_DATA);
        ItemLore lore = shulker.get(DataComponents.LORE);

        if (data != null) {
            CompoundTag tag = data.copyTag();
            tag.remove(MATERIAL_ID);
            shulker.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        }


        if (lore != null) {
            List<Component> newLines = new ArrayList<>(lore.lines());
            for (int i = 0; i < newLines.size(); i++) {
                Component heldLine = newLines.get(i);
                if (heldLine.getString().startsWith(LORE_PREFIX)) {
                    newLines.remove(i);
                    break;
                }
            }
            ItemLore newLore = new ItemLore(newLines);
            shulker.set(DataComponents.LORE, newLore);
        }
    }

    /**
     * If the given ItemStack is the same as the Shulker Box's material item
     * @param shulker Shulker Box ItemStack with the Material filter
     * @param test The ItemStack being tested
     * @param allowNoMaterial If a Shulker Box with no material set should always return true
     * @return True if it passes through the filter
     */
    public static boolean matchesMaterialFilter(ItemStack shulker, ItemStack test, boolean allowNoMaterial) {
        Item material = getMaterial(shulker);
        return material == null ? allowNoMaterial : test.is(material);
    }
}
