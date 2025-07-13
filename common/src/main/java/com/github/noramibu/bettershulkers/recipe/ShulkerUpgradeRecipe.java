package com.github.noramibu.bettershulkers.recipe;

import com.github.noramibu.bettershulkers.util.ShulkerUtil;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class ShulkerUpgradeRecipe extends CustomRecipe {
    public ShulkerUpgradeRecipe(CraftingBookCategory craftingBookCategory) {
        super(craftingBookCategory);
    }

    @Override
    public boolean matches(CraftingInput input, Level world) {
        // Prevent OutOfBoundsException
        // Also acts as a super quick escape path
        if (input.width() != 3 && input.height() != 3) {
            return false;
        }

        // Check center for a shulker
        if (!ShulkerUtil.isShulkerBox(input.getItem(1, 1))) {
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

        return input.getItem(0, 1).is(materialItem)  &&
                input.getItem(2, 1).is(materialItem) &&
                input.getItem(1, 2).is(materialItem);
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider lookup) {
        // Get material
        ItemStack materialItem = input.getItem(1, 0);
        // Get Shulker
        ItemStack shulkerStack = input.getItem(1, 1).copy();
        // Apply material
        ShulkerUtil.setMaterialForShulker(shulkerStack, materialItem);
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
        return BetterShulkersRecipes.SHULKER_UPGRADE_SERIALIZER;
    }

    @Override
    public CraftingBookCategory category() {
        return CraftingBookCategory.MISC;
    }


    public static class Serializer extends CustomRecipe.Serializer<ShulkerUpgradeRecipe> {

        public Serializer(CustomRecipe.Serializer.Factory<ShulkerUpgradeRecipe> factory) {
            super(factory);
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ShulkerUpgradeRecipe> streamCodec() {
            return null;
        }
    }
}
