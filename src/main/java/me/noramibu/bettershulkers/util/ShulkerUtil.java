package me.noramibu.bettershulkers.util;

import me.noramibu.bettershulkers.BetterShulkers;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtString;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ShulkerUtil {

    public static final String MATERIAL_PREFIX = "Material: ";

    public static boolean isShulkerBox(ItemStack stack) {
        return stack.isIn(ConventionalItemTags.SHULKER_BOXES);
    }

    public static boolean earlyIsShulkerBox(Item item) {
        return item instanceof BlockItem blockItem && blockItem.getBlock() instanceof ShulkerBoxBlock;
    }

    public static DefaultedList<ItemStack> getInventoryFromShulker(ItemStack stack) {
        ContainerComponent component = stack.get(DataComponentTypes.CONTAINER);
        if (component == null) {
            return DefaultedList.of();
        }
        DefaultedList<ItemStack> inventory = DefaultedList.ofSize(27, ItemStack.EMPTY);
        component.copyTo(inventory);
        return inventory;
    }

    public static boolean canBeAddedToShulker(ItemStack shulkerStack, ItemStack itemToAdd) {
        Item shulkerMaterial = getMaterialFromShulker(shulkerStack);
        return itemToAdd.isOf(shulkerMaterial);
    }

    private static boolean canFit(ItemStack slot, ItemStack itemStack) {
        if (ItemStack.areItemsAndComponentsEqual(slot, itemStack)) {
            return slot.getMaxCount() != slot.getCount();
        }
        return false;
    }

    public static void addToShulker(ItemStack shulkerStack, ItemStack itemToAdd) {
        ContainerComponent container = shulkerStack.get(DataComponentTypes.CONTAINER);
        DefaultedList<ItemStack> inventory = DefaultedList.ofSize(27, ItemStack.EMPTY);
        container.copyTo(inventory);

        for (int i = 0; i < inventory.size(); i++) {
            ItemStack slot = inventory.get(i);
            if(slot.isEmpty()) {
                inventory.set(i, itemToAdd.copy());
                itemToAdd.setCount(0);
                break;
            } else if (canFit(slot, itemToAdd)) {
                int toAdd = Math.min(itemToAdd.getCount(), slot.getMaxCount() - slot.getCount());
                slot.increment(toAdd);
                itemToAdd.decrement(toAdd);
            }

            if (itemToAdd.isEmpty()) {
                break;
            }
        }

        shulkerStack.set(DataComponentTypes.CONTAINER, ContainerComponent.fromStacks(inventory));
    }

    public static String getItemId(ItemStack stack) {
        return Registries.ITEM.getId(stack.getItem()).toString();
    }

    public static Item getItemFromId(String id) {
        return Registries.ITEM.get(Identifier.of(id));
    }

    /**
     * Gets the material from the Shulker Box's NBT data.
     * @param shulker ItemStack of the Shulker Box
     * @return Item that is the material if present, else null
     */
    @Nullable
    public static Item getMaterialFromShulker(ItemStack shulker) {
        var nbt = shulker.get(DataComponentTypes.CUSTOM_DATA).copyNbt();
        String materialId = nbt.getString(BetterShulkers.MATERIAL_PATH);
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
        var component = shulker.getComponents().get(DataComponentTypes.CUSTOM_DATA);
        if (component == null) {
            return null;
        }
        var nbt = component.copyNbt();
        String materialId = nbt.getString(BetterShulkers.MATERIAL_PATH);
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
        var nbt = shulker.get(DataComponentTypes.CUSTOM_DATA).copyNbt();
        nbt.put(BetterShulkers.MATERIAL_PATH, NbtString.of(getItemId(material)));
        shulker.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(nbt));
        setShulkerMaterialLore(shulker, material.getItem());
    }

    private static void setShulkerMaterialLore(ItemStack shulkerBox, Item material) {
        shulkerBox.set(DataComponentTypes.LORE, new LoreComponent(List.of(Text.literal(MATERIAL_PREFIX + material.toString()))));
    }
} 