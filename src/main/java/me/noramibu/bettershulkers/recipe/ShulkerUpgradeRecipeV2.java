package me.noramibu.bettershulkers.recipe;

import eu.pb4.polymer.core.api.item.PolymerRecipe;
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
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ShulkerUpgradeRecipeV2 extends SpecialCraftingRecipe implements PolymerRecipe {

    public ShulkerUpgradeRecipeV2(CraftingRecipeCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingRecipeInput input, World world) {
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
    public boolean fits(int width, int height) {
        return width > 2 && height > 2;
    }

    @Override
    public ItemStack getResult(RegistryWrapper.WrapperLookup registriesLookup) {
        return Items.SHULKER_BOX.getDefaultStack();
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
    public RecipeSerializer<?> getSerializer() {
        return BetterShulkersRecipes.SHULKER_UPGRADE_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return BetterShulkersRecipes.SHULKER_UPGRADE_RECIPE_TYPE;
    }

    @Override
    public CraftingRecipeCategory getCategory() {
        return CraftingRecipeCategory.MISC;
    }

    @Override
    public @Nullable Recipe<?> getPolymerReplacement(ServerPlayerEntity player) {
        return PolymerRecipe.createCraftingRecipe(this);
    }

    public static class Serializer extends SpecialRecipeSerializer<ShulkerUpgradeRecipeV2> implements PolymerObject {

        public Serializer(Factory<ShulkerUpgradeRecipeV2> factory) {
            super(factory);
        }

        @Override
        public PacketCodec<RegistryByteBuf, ShulkerUpgradeRecipeV2> packetCodec() {
            return null;
        }
    }
}
