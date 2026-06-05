package com.github.noramibu.bettershulkers.recipe;

import com.github.noramibu.bettershulkers.gamerules.BetterShulkersGameRules;
import com.github.noramibu.bettershulkers.gamerules.PickupType;
import com.github.noramibu.bettershulkers.material.ShulkerMaterialManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

/**
 * The custom material-assigning recipe
 */
public class ShulkerUpgradeRecipe extends CustomRecipe implements FakeRecipe {
    public ShulkerUpgradeRecipe() {
        super();
    }

    @Override
    public boolean matches(CraftingInput input, Level world) {
        if (world.isClientSide() || ((ServerLevel) world).getGameRules().get(BetterShulkersGameRules.PICKUPTYPE) != PickupType.RECIPE) {
            return false;
        }
        // Prevent OutOfBoundsException
        // Also acts as a super quick escape path
        if (input.width() != 3 || input.height() != 3) {
            return false;
        }

        // Check center for a shulker
        if (input.getItem(1, 1).is(ItemTags.SHULKER_BOXES)) {
            return false;
        }

        // Check 4 corners for obsidian
        if (!input.getItem(0, 0).is(Items.OBSIDIAN) ||
                !input.getItem(0, 2).is(Items.OBSIDIAN) ||
                !input.getItem(2, 0).is(Items.OBSIDIAN) ||
                !input.getItem(2, 2).is(Items.OBSIDIAN)) {
            return false;
        }

        // Check materials
        Item materialItem = input.getItem(1, 0).getItem();
        // Quick escape
        if (materialItem == Items.AIR) {
            return false;
        }

        return input.getItem(0, 1).is(materialItem) &&
                input.getItem(2, 1).is(materialItem) &&
                input.getItem(1, 2).is(materialItem);
    }

    @Override
    public ItemStack assemble(CraftingInput input) {
        // Get material
        ItemStack materialItem = input.getItem(1, 0);
        // Get Shulker
        ItemStack shulkerStack = input.getItem(1, 1).copy();
        // Apply material
        ShulkerMaterialManager.setMaterial(shulkerStack, materialItem.getItem());
        return shulkerStack;
    }

    @Override
    public boolean showNotification() {
        return true;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return null;
    }

    @Override
    public CraftingBookCategory category() {
        return CraftingBookCategory.MISC;
    }
}
