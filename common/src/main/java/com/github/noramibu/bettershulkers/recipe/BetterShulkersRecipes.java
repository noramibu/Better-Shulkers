package com.github.noramibu.bettershulkers.recipe;

import com.github.noramibu.bettershulkers.BetterShulkers;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class BetterShulkersRecipes {
    public static final RecipeSerializer<ShulkerUpgradeRecipe> SHULKER_UPGRADE_SERIALIZER = new ShulkerUpgradeRecipe.Serializer(ShulkerUpgradeRecipe::new);

    public static void register() {
        Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, ResourceLocation.fromNamespaceAndPath(BetterShulkers.MOD_ID, "shulker_upgrade"), SHULKER_UPGRADE_SERIALIZER);
    }
}
