package com.github.noramibu.bettershulkers.util;

import com.github.noramibu.bettershulkers.BetterShulkers;
import com.github.noramibu.bettershulkers.Config;
import com.github.noramibu.bettershulkers.abstraction.AbstractionManager;
import com.github.noramibu.bettershulkers.interfaces.ForceInventory;
import com.github.noramibu.bettershulkers.interfaces.ShulkerViewer;
import com.github.noramibu.bettershulkers.mixin.AbstractContainerAccessor;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
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
     * Saves the inventory to the Component
     * @param inventory The inventory of the active shulker
     * @param player Player that was viewing the screen
     */
    public static void saveShulkerInventory(NonNullList<ItemStack> inventory, ServerPlayer player) {
        int smallestSize = getSmallestListIndex(inventory);
        if (smallestSize != -1) {
            NonNullList<ItemStack> newInventory = NonNullList.withSize(smallestSize + 1, ItemStack.EMPTY);
            for (int i = 0; i <= smallestSize; i++) {
                newInventory.set(i, inventory.get(i));
            }
            ((ShulkerViewer)player).getViewedStack().set(DataComponents.CONTAINER, ItemContainerContents.fromItems(newInventory));
        } else {
            ((ShulkerViewer)player).getViewedStack().set(DataComponents.CONTAINER, ItemContainerContents.fromItems(List.of()));
        }
    }

    private static int getSmallestListIndex(NonNullList<ItemStack> itemStacks) {
        int lastIndex = -1;
        for (int i = 0; i < 27; i++) {
            if (itemStacks.get(i) != ItemStack.EMPTY) {
                lastIndex = i;
            }
        }
        return lastIndex;
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
        addToShulker(inventory, itemToAdd);
        shulkerStack.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(inventory));
    }

    private static void addToShulker(NonNullList<ItemStack> inventory, ItemStack itemToAdd) {
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack slot = inventory.get(i);
            if (slot.isEmpty()) {
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
    }

    /**
     * Adds the item to the shulker box's inventory
     * @param container The shulker box's UI
     * @param itemToAdd The item to be inserted
     * @param update Whether to send an update packet
     */
    public static void addToShulkerInventory(AbstractContainerMenu container, ItemStack itemToAdd, boolean update) {
        iterateThroughInventory(container, itemToAdd, 0, 27, update);
    }

    /**
     * Adds the item to the player's inventory
     * @param container The shulker box's UI
     * @param itemToAdd The item to be inserted
     * @param update Whether to send an update packet
     */
    public static void addToPlayerInventory(AbstractContainerMenu container, ItemStack itemToAdd, boolean update) {
        iterateThroughInventory(container, itemToAdd, 27, 63, update);
    }

    private static void iterateThroughInventory(AbstractContainerMenu container, ItemStack itemToAdd, int start, int stop, boolean update) {
        NonNullList<Slot> slots = ((AbstractContainerAccessor)container).getSlots();
        boolean markDirty = false;

        for (int i = start; i < stop; i++) {
            Slot slot = slots.get(i);
            ItemStack stack = slot.getItem();
            if (stack.isEmpty()) {
                slot.set(itemToAdd.copy());
                itemToAdd.setCount(0);
                markDirty = true;
                break;
            } else if (canFit(stack, itemToAdd)) {
                int toAdd = Math.min(itemToAdd.getCount(), stack.getMaxStackSize() - stack.getCount());
                stack.grow(toAdd);
                itemToAdd.shrink(toAdd);
                markDirty = true;
            }

            if (itemToAdd.isEmpty()) {
                break;
            }
        }

        if (markDirty && update) {
            container.broadcastChanges();
        }
    }

    /**
     * Switches the shulker inventory without closing the existing shulker's inventory screen
     * @param player The player viewing the screen
     * @param newShulker The new shulker to view
     */
    public static void seamlesslySwitchShulkerInventory(ServerPlayer player, ItemStack newShulker) {
        saveShulkerInventory(player.containerMenu.getItems(), player);
        ((ShulkerViewer) player).setViewing(newShulker, null);
        NonNullList<ItemStack> newInventory = getInventoryFromShulker(newShulker);
        ShulkerBoxBlockEntity blockEntity = ((ShulkerViewer)player).getViewedEntity();
        ((ForceInventory)blockEntity).setInventory(newInventory);
        player.containerMenu.broadcastChanges();
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

    /**
     * If the player has the permissions to open a shulker in their inventory
     * @param player Player to check permissions of
     * @return True if they have sufficient permissions
     */
    public static boolean hasOpenPermission(Player player) {
        return Config.OPEN_SHULKER_FROM_INVENTORY && checkOpenPermission(player);
    }

    private static boolean checkOpenPermission(Player player) {
        if (Config.REQUIRE_PERMISSION_FOR_RIGHT_CLICK_OPEN) {
            return AbstractionManager.ABSTRACTION.permissionCheck(((ServerPlayer) player).createCommandSourceStack(), "bettershulkers.open", player.hasPermissions(2));
        } else {
            return true;
        }
    }
}
