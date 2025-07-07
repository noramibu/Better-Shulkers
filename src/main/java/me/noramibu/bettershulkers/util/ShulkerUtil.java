package me.noramibu.bettershulkers.util;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.component.Component;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.item.BlockItem;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.Item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtString;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class ShulkerUtil {

    public static final String MATERIAL_PREFIX = "Material: ";

    public static boolean isShulkerBox(ItemStack stack) {
        return stack.isIn(ConventionalItemTags.SHULKER_BOXES);
    }

    public static void setShulkerMaterial(ItemStack shulkerBox, String materialId) {
        shulkerBox.set(DataComponentTypes.LORE, new LoreComponent(List.of(Text.literal(MATERIAL_PREFIX + materialId))));
    }

    public static Optional<String> getShulkerMaterial(ItemStack shulkerBox) {
        for (Component<?> component : shulkerBox.getComponents()) {
            if (component.type() == DataComponentTypes.LORE) {
                if (component.value() instanceof LoreComponent lore) {
                    for (Text line : lore.lines()) {
                        String lineStr = line.getString();
                        if (lineStr.startsWith(MATERIAL_PREFIX)) {
                            return Optional.of(lineStr.substring(MATERIAL_PREFIX.length()));
                        }
                    }
                }
                break;
            }
        }
        return Optional.empty();
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
        ContainerComponent container = null;
        for (Component<?> component : shulkerStack.getComponents()) {
            if (component.type() == DataComponentTypes.CONTAINER) {
                if (component.value() instanceof ContainerComponent foundContainer) {
                    container = foundContainer;
                }
                break;
            }
        }

        if (container == null) {
            return true;
        }

        DefaultedList<ItemStack> inventory = DefaultedList.ofSize(27, ItemStack.EMPTY);
        container.copyTo(inventory);

        ItemStack toAdd = itemToAdd.copy();
        for (int i = 0; i < inventory.size() && !toAdd.isEmpty(); i++) {
            ItemStack slot = inventory.get(i);
            if (slot.isEmpty()) {
                return true;
            }
            if (ItemStack.areItemsAndComponentsEqual(slot, toAdd)) {
                int space = slot.getMaxCount() - slot.getCount();
                if (space >= toAdd.getCount()) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void addToShulker(ItemStack shulkerStack, ItemStack itemToAdd) {
        if (itemToAdd.isEmpty() || !canBeAddedToShulker(shulkerStack, itemToAdd)) {
            return;
        }

        DefaultedList<ItemStack> inventory = DefaultedList.ofSize(27, ItemStack.EMPTY);
        ContainerComponent container = null;
        for (Component<?> component : shulkerStack.getComponents()) {
            if (component.type() == DataComponentTypes.CONTAINER) {
                if (component.value() instanceof ContainerComponent foundContainer) {
                    container = foundContainer;
                }
                break;
            }
        }
        
        if (container != null) {
            container.copyTo(inventory);
        }

        for (int i = 0; i < inventory.size(); i++) {
            ItemStack slot = inventory.get(i);
            if(slot.isEmpty()) {
                inventory.set(i, itemToAdd.copy());
                itemToAdd.setCount(0);
                break;
            } else if (ItemStack.areItemsAndComponentsEqual(slot, itemToAdd) && slot.getCount() < slot.getMaxCount()) {
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

    /**
     * Gets the material from the Shulker Box's NBT data.
     * @param shulker ItemStack of the Shulker Box
     * @return Item that is the material if present, else null
     */
    @Nullable
    public static Item getMaterialFromShulker(ItemStack shulker) {
        NbtComponent component = shulker.get(DataComponentTypes.CUSTOM_DATA);
        String materialId = component.copyNbt().getString(MATERIAL_PREFIX);
        if (materialId.isEmpty()) {
            return null;
        }
        return Registries.ITEM.get(Identifier.of(materialId));
    }

    /**
     * Sets the material for a Shulker Box
     * @param shulker ItemStack of the Shulker Box
     * @param material ItemStack of the material to collect
     */
    public static void setMaterialForShulker(ItemStack shulker, ItemStack material) {
        NbtComponent component = shulker.get(DataComponentTypes.CUSTOM_DATA);
        component.apply((nbtCompound -> {
            nbtCompound.put(MATERIAL_PREFIX, NbtString.of(getItemId(material)));
        }));
    }
} 