package me.noramibu.bettershulkers.recipe;

import me.noramibu.bettershulkers.BetterShulkers;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class BetterShulkersRecipes {
    public static final RecipeSerializer<ShulkerUpgradeRecipeV2> SHULKER_UPGRADE_SERIALIZER = new ShulkerUpgradeRecipeV2.Serializer(ShulkerUpgradeRecipeV2::new);
    public static final RecipeType<ShulkerUpgradeRecipeV2> SHULKER_UPGRADE_RECIPE_TYPE = RecipeType.register("shulker_upgrade");

    public static void register() {
        Registry.register(Registries.RECIPE_SERIALIZER, Identifier.of(BetterShulkers.MOD_ID, "shulker_upgrade"), SHULKER_UPGRADE_SERIALIZER);
    }
} 