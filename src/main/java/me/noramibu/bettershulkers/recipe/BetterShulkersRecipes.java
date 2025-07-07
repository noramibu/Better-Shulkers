package me.noramibu.bettershulkers.recipe;

import me.noramibu.bettershulkers.BetterShulkers;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class BetterShulkersRecipes {
    public static final RecipeSerializer<ShulkerUpgradeRecipe> SHULKER_UPGRADE_SERIALIZER = new ShulkerUpgradeRecipe.Serializer(ShulkerUpgradeRecipe::new);

    public static void register() {
        Registry.register(Registries.RECIPE_SERIALIZER, Identifier.of(BetterShulkers.MOD_ID, "shulker_upgrade"), SHULKER_UPGRADE_SERIALIZER);
    }
} 