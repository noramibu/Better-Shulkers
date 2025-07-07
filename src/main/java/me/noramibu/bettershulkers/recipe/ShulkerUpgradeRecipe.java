package me.noramibu.bettershulkers.recipe;

import com.mojang.serialization.MapCodec;
import me.noramibu.bettershulkers.BetterShulkers;
import me.noramibu.bettershulkers.util.ShulkerUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;

public class ShulkerUpgradeRecipe implements CraftingRecipe {

    @Override
    public boolean matches(CraftingRecipeInput inventory, World world) {
        if (inventory.getWidth() != 3 || inventory.getHeight() != 3) {
            return false;
        }

        if (!ShulkerUtil.isShulkerBox(inventory.getStackInSlot(1, 1))) {
            return false;
        }

        if (!inventory.getStackInSlot(0, 0).isOf(Items.OBSIDIAN) ||
            !inventory.getStackInSlot(0, 2).isOf(Items.OBSIDIAN) ||
            !inventory.getStackInSlot(2, 0).isOf(Items.OBSIDIAN) ||
            !inventory.getStackInSlot(2, 2).isOf(Items.OBSIDIAN)) {
            return false;
        }

        Item material = null;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if ((row == 1 && col == 1) || (row % 2 == 0 && col % 2 == 0)) {
                    continue;
                }
                ItemStack currentStack = inventory.getStackInSlot(row, col);
                if (currentStack.isEmpty()) {
                    return false;
                }
                if (material == null) {
                    material = currentStack.getItem();
                } else {
                    if (!currentStack.isOf(material)) {
                        return false;
                    }
                }
            }
        }
        return material != null;
    }

    @Override
    public ItemStack craft(CraftingRecipeInput inventory, RegistryWrapper.WrapperLookup lookup) {
        ItemStack materialStack = inventory.getStackInSlot(0, 1);
        ItemStack shulkerStack = inventory.getStackInSlot(1, 1).copy();
        shulkerStack.setCount(1);
        String materialId = ShulkerUtil.getItemId(materialStack);
        ShulkerUtil.setShulkerMaterial(shulkerStack, materialId);
        return shulkerStack;
    }

    @Override
    public boolean fits(int width, int height) {
        return width >= 3 && height >= 3;
    }

    @Override
    public ItemStack getResult(RegistryWrapper.WrapperLookup registriesLookup) {
        return ItemStack.EMPTY;
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
    public CraftingRecipeCategory getCategory() {
        return CraftingRecipeCategory.MISC;
    }

    /*
    public static class Serializer implements RecipeSerializer<ShulkerUpgradeRecipe> {
        public static final MapCodec<ShulkerUpgradeRecipe> CODEC = MapCodec.unit(ShulkerUpgradeRecipe::new);
        public static final PacketCodec<RegistryByteBuf, ShulkerUpgradeRecipe> PACKET_CODEC = PacketCodec.unit(new ShulkerUpgradeRecipe());

        @Override
        public MapCodec<ShulkerUpgradeRecipe> codec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, ShulkerUpgradeRecipe> packetCodec() {
            return PACKET_CODEC;
        }
    }

     */
} 