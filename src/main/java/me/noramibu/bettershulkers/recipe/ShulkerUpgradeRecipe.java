package me.noramibu.bettershulkers.recipe;

import eu.pb4.polymer.core.api.utils.PolymerObject;
import me.noramibu.bettershulkers.util.ShulkerUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;

public class ShulkerUpgradeRecipe extends SpecialCraftingRecipe {

    public ShulkerUpgradeRecipe(CraftingRecipeCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingRecipeInput input, World world) {
        // Prevent OutOfBoundsException
        // Also acts as a super quick escape path
        if (input.getWidth() != 3 && input.getHeight() != 3) {
            return false;
        }

        // Check center for a shulker
        if (!ShulkerUtil.isShulkerBox(input.getStackInSlot(1, 1))) {
            return false;
        }

        // Check 4 corners for obsidian
        if (!input.getStackInSlot(0, 0).isOf(Items.OBSIDIAN) ||
                !input.getStackInSlot(0, 2).isOf(Items.OBSIDIAN) ||
                !input.getStackInSlot(2, 0).isOf(Items.OBSIDIAN) ||
                !input.getStackInSlot(2, 2).isOf(Items.OBSIDIAN)) {
            return false;
        }

        // Check materials
        Item materialItem = input.getStackInSlot(1, 0).getItem();
        // Quick escape
        if (materialItem == Items.AIR) {
            return false;
        }

        return input.getStackInSlot(0, 1).isOf(materialItem)  &&
                input.getStackInSlot(2, 1).isOf(materialItem) &&
                input.getStackInSlot(1, 2).isOf(materialItem);
    }

    @Override
    public ItemStack craft(CraftingRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
        // Get material
        ItemStack materialItem = input.getStackInSlot(1, 0);
        // Get Shulker
        ItemStack shulkerStack = input.getStackInSlot(1, 1).copy();
        // Apply material
        ShulkerUtil.setMaterialForShulker(shulkerStack, materialItem);
        return shulkerStack;
    }

    @Override
    public boolean showNotification() {
        return true;
    }

    @Override
    public boolean isIgnoredInRecipeBook() {
        return true;
    }

    @Override
    public RecipeSerializer<? extends SpecialCraftingRecipe> getSerializer() {
        return BetterShulkersRecipes.SHULKER_UPGRADE_SERIALIZER;
    }

    @Override
    public CraftingRecipeCategory getCategory() {
        return CraftingRecipeCategory.MISC;
    }


    public static class Serializer extends SpecialRecipeSerializer<ShulkerUpgradeRecipe> implements PolymerObject {

        public Serializer(Factory<ShulkerUpgradeRecipe> factory) {
            super(factory);
        }

        @Override
        public PacketCodec<RegistryByteBuf, ShulkerUpgradeRecipe> packetCodec() {
            return null;
        }
    }
}
