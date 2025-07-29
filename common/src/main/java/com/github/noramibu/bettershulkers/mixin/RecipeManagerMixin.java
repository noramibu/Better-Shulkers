package com.github.noramibu.bettershulkers.mixin;

import com.github.noramibu.bettershulkers.BetterShulkers;
import com.github.noramibu.bettershulkers.recipe.ShulkerUpgradeRecipe;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.crafting.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin {
    @Inject(method = "prepare(Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)Lnet/minecraft/world/item/crafting/RecipeMap;", at = @At(value = "INVOKE", target = "Ljava/util/SortedMap;forEach(Ljava/util/function/BiConsumer;)V"))
    private void addBetterShulkersRecipe(ResourceManager resourceManager, ProfilerFiller profilerFiller, CallbackInfoReturnable<RecipeMap> cir, @Local(ordinal = 0) List<RecipeHolder<?>> list) {
        ShulkerUpgradeRecipe recipe = new ShulkerUpgradeRecipe(CraftingBookCategory.MISC);
        ResourceLocation resourceLocation = ResourceLocation.fromNamespaceAndPath(BetterShulkers.MOD_ID, "shulker_upgrade");
        ResourceKey<Recipe<?>> resourceKey = ResourceKey.create(Registries.RECIPE, resourceLocation);
        RecipeHolder<?> recipeHolder = new RecipeHolder<>(resourceKey, recipe);
        list.add(recipeHolder);
    }
}
