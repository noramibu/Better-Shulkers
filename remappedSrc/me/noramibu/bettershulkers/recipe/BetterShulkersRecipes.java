package me.noramibu.bettershulkers.recipe;

import me.noramibu.bettershulkers.BetterShulkers;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class BetterShulkersRecipes {
    public static RecipeSerializer<ShulkerUpgradeRecipe> SHULKER_UPGRADE_SERIALIZER;

    public static void register() {
        SHULKER_UPGRADE_SERIALIZER = Registry.register(
                Registries.RECIPE_SERIALIZER,
                Identifier.of(BetterShulkers.MOD_ID, "crafting_special_shulkerupgrade"),
                new ShulkerUpgradeRecipe.Serializer()
        );
    }
} 