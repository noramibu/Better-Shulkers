package com.github.noramibu.bettershulkers.recipe;

import com.github.noramibu.bettershulkers.Config;
import com.github.noramibu.bettershulkers.util.ShulkerUtil;
import net.minecraft.core.HolderLookup;
//: >=1.21.2
import net.minecraft.network.FriendlyByteBuf;
//: END
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

/**
 * The custom material-assigning recipe
 */
public class ShulkerUpgradeRecipe extends CustomRecipe {
    public ShulkerUpgradeRecipe(CraftingBookCategory craftingBookCategory) {
        super(craftingBookCategory);
    }

    @Override
    public boolean matches(CraftingInput input, Level world) {
        if (!Config.ITEM_PICKUP_TYPE.equals(Config.PickupType.RECIPE)) {
            return false;
        }
        // Prevent OutOfBoundsException
        // Also acts as a super quick escape path
        if (input.width() != 3 || input.height() != 3) {
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

    /*\ <=1.21.1
    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width > 2 && height > 2;
    }
    \END */

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

    //: >=1.21.2
    public static class Serializer extends CustomRecipe.Serializer<ShulkerUpgradeRecipe> {

        public Serializer(CustomRecipe.Serializer.Factory<ShulkerUpgradeRecipe> factory) {
            super(factory);
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ShulkerUpgradeRecipe> streamCodec() {
            return null;
        }
    }
    //: END

    /*\ <=1.21.1
    public static class Serializer extends SimpleCraftingRecipeSerializer<ShulkerUpgradeRecipe> implements FakeRecipe {

        public Serializer(Factory<ShulkerUpgradeRecipe> factory) {
            super(factory);
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ShulkerUpgradeRecipe> streamCodec() {
            return null;
        }
    }
    \END */
}
