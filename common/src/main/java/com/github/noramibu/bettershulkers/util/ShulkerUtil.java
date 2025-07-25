package com.github.noramibu.bettershulkers.util;

import com.github.noramibu.bettershulkers.BetterShulkers;
import com.github.noramibu.bettershulkers.abstraction.AbstractionManager;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Utilities for Better Shulker
 */
public class ShulkerUtil {
    /**
     * The Lore prefix for a shulker box that has a material
     */
    public static final String MATERIAL_PREFIX = "Material: ";

    /**
     * If an item is a shulker box
     * @param stack Item to check
     * @return If an item is a shulker box
     */
    public static boolean isShulkerBox(ItemStack stack) {
        return AbstractionManager.ABSTRACTION.isShulkerBox(stack);
    }

    /**
     * If an item is a shulker box.<p>
     * Only use this if a check is needed before item tags are instantiated
     * @param item Item to check
     * @return If an item is a shulker box
     */
    public static boolean earlyIsShulkerBox(Item item) {
        return item instanceof BlockItem blockItem && blockItem.getBlock() instanceof ShulkerBoxBlock;
    }

    /**
     * Gets the inventory of a shulker box
     * @param stack Shulker box item
     * @return A list of all items in the shulker's inventory
     */
    public static NonNullList<ItemStack> getInventoryFromShulker(ItemStack stack) {
        ItemContainerContents component = stack.get(DataComponents.CONTAINER);
        if (component == null) {
            return NonNullList.create();
        }
        NonNullList<ItemStack> inventory = NonNullList.withSize(27, ItemStack.EMPTY);
        component.copyInto(inventory);
        return inventory;
    }

    /**
     * If an item can be added to a shulker box
     * @param shulkerStack The shulker box item
     * @param itemToAdd The item to try and insert into the shulker box
     * @return If the item to insert and the shulker box's resource match
     */
    public static boolean canBeAddedToShulker(ItemStack shulkerStack, ItemStack itemToAdd) {
        Item shulkerMaterial = getMaterialFromShulker(shulkerStack);
        return itemToAdd.is(shulkerMaterial);
    }

    private static boolean canFit(ItemStack slot, ItemStack itemStack) {
        if (ItemStack.isSameItemSameComponents(slot, itemStack)) {
            return slot.getMaxStackSize() != slot.getCount();
        }
        return false;
    }

    /**
     * Adds the item to the shulker box
     * @param shulkerStack The shulker box item
     * @param itemToAdd The item to be inserted
     */
    public static void addToShulker(ItemStack shulkerStack, ItemStack itemToAdd) {
        ItemContainerContents container = shulkerStack.get(DataComponents.CONTAINER);
        NonNullList<ItemStack> inventory = NonNullList.withSize(27, ItemStack.EMPTY);
        container.copyInto(inventory);

        for (int i = 0; i < inventory.size(); i++) {
            ItemStack slot = inventory.get(i);
            if(slot.isEmpty()) {
                inventory.set(i, itemToAdd.copy());
                itemToAdd.setCount(0);
                break;
            } else if (canFit(slot, itemToAdd)) {
                int toAdd = Math.min(itemToAdd.getCount(), slot.getMaxStackSize() - slot.getCount());
                slot.grow(toAdd);
                itemToAdd.shrink(toAdd);
            }

            if (itemToAdd.isEmpty()) {
                break;
            }
        }

        shulkerStack.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(inventory));
    }

    /**
     * Gets an id from an item
     * @param stack The item to get the id from
     * @return Minecraft id from the Registry
     */
    public static String getItemId(ItemStack stack) {
        return BuiltInRegistries.ITEM.getKey(stack.getItem()).toString();
    }

    /**
     * Gets an item from an id
     * @param id The id to get an item from
     * @return Item instance of the id from the Registry
     */
    public static Item getItemFromId(String id) {
        /*\ <=1.21.1
        return BuiltInRegistries.ITEM.get(ResourceLocation.parse(id));
        \END */
        //: >=1.21.2
        return BuiltInRegistries.ITEM.getValue(ResourceLocation.parse(id));
        //: END
    }

    /**
     * Gets the material from the Shulker Box's NBT data.
     * @param shulker ItemStack of the Shulker Box
     * @return Item that is the material if present, else null
     */
    @Nullable
    public static Item getMaterialFromShulker(ItemStack shulker) {
        var nbt = shulker.get(DataComponents.CUSTOM_DATA).copyTag();
        //: >=1.21.5
        String materialId = nbt.getString(BetterShulkers.MATERIAL_PATH).get();
        //: END

        /*\ <=1.21.4
        String materialId = nbt.getString(BetterShulkers.MATERIAL_PATH).toString();
        \END */

        if (materialId.isEmpty()) {
            return null;
        }
        return getItemFromId(materialId);
    }

    /**
     * Gets the material from the Shulker Box's NBT data.
     * @param shulker BlockEntity that is the shulker box
     * @return Item that is the material if present, else null
     */
    @Nullable
    public static Item getMaterialFromShulkerBlock(BlockEntity shulker) {
        var component = shulker.components().get(DataComponents.CUSTOM_DATA);
        if (component == null) {
            return null;
        }
        var nbt = component.copyTag();

        //: >=1.21.5
        String materialId = nbt.getString(BetterShulkers.MATERIAL_PATH).get();
        //: END

        /*\ <=1.21.4
        String materialId = nbt.getString(BetterShulkers.MATERIAL_PATH).toString();
        \END */

        if (materialId.isEmpty()) {
            return null;
        }
        return getItemFromId(materialId);
    }

    /**
     * Sets the material for a Shulker Box
     * @param shulker ItemStack of the Shulker Box
     * @param material ItemStack of the material to collect
     */
    public static void setMaterialForShulker(ItemStack shulker, ItemStack material) {
        var nbt = shulker.get(DataComponents.CUSTOM_DATA).copyTag();
        nbt.put(BetterShulkers.MATERIAL_PATH, StringTag.valueOf(getItemId(material)));
        shulker.set(DataComponents.CUSTOM_DATA, CustomData.of(nbt));
        setShulkerMaterialLore(shulker, material.getItem());
    }

    private static void setShulkerMaterialLore(ItemStack shulkerBox, Item material) {
        shulkerBox.set(DataComponents.LORE, new ItemLore(List.of(Component.literal(MATERIAL_PREFIX + material.toString()))));
    }
}
