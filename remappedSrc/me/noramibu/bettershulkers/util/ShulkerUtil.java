package me.noramibu.bettershulkers.util;

import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.component.Component;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;

import java.util.List;
import java.util.Optional;

public class ShulkerUtil {

    public static final String MATERIAL_PREFIX = "Material: ";

    public static boolean isShulkerBox(ItemStack stack) {
        return stack.getItem() instanceof BlockItem && ((BlockItem) stack.getItem()).getBlock() instanceof ShulkerBoxBlock;
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
            return true; // No container component means it's empty and can fit items.
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
} 