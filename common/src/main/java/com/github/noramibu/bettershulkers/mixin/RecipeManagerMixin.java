package com.github.noramibu.bettershulkers.mixin;

import com.github.noramibu.bettershulkers.BetterShulkers;
import com.github.noramibu.bettershulkers.recipe.ShulkerUpgradeRecipe;
/*\ <=1.21.1
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.gson.JsonElement;
\END */
import com.llamalad7.mixinextras.sugar.Local;
//: >=1.21.2
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
//: END
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.crafting.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
/*\ <=1.21.1
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
\END */
//: >=1.21.2
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.List;
//: END

import java.util.Map;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin {
    //: >=1.21.2
    @Inject(method = "prepare(Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)Lnet/minecraft/world/item/crafting/RecipeMap;", at = @At(value = "INVOKE", target = "Ljava/util/SortedMap;forEach(Ljava/util/function/BiConsumer;)V"))
    private void addBetterShulkersRecipe(ResourceManager resourceManager, ProfilerFiller profilerFiller, CallbackInfoReturnable<RecipeMap> cir, @Local(ordinal = 0) List<RecipeHolder<?>> list) {
        ShulkerUpgradeRecipe recipe = new ShulkerUpgradeRecipe(CraftingBookCategory.MISC);
        ResourceLocation resourceLocation = ResourceLocation.fromNamespaceAndPath(BetterShulkers.MOD_ID, "shulker_upgrade");
        ResourceKey<Recipe<?>> resourceKey = ResourceKey.create(Registries.RECIPE, resourceLocation);
        RecipeHolder<?> recipeHolder = new RecipeHolder<>(resourceKey, recipe);
        list.add(recipeHolder);
    }
    //: END

    /*\ <=1.21.1
    @Inject(method = "apply(Ljava/util/Map;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableMultimap$Builder;build()Lcom/google/common/collect/ImmutableMultimap;"))
    private void addBetterShulkerRecipe(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profilerFiller, CallbackInfo ci, @Local(ordinal = 0) ImmutableMultimap.Builder<RecipeType<?>, RecipeHolder<?>> builder, @Local(ordinal = 0) ImmutableMap.Builder<ResourceLocation, RecipeHolder<?>> builder2) {
        ShulkerUpgradeRecipe recipe = new ShulkerUpgradeRecipe(CraftingBookCategory.MISC);
        ResourceLocation resourceLocation = ResourceLocation.fromNamespaceAndPath(BetterShulkers.MOD_ID, "shulker_upgrade");
        RecipeHolder<?> recipeHolder = new RecipeHolder<>(resourceLocation, recipe);
        builder.put(recipe.getType(), recipeHolder);
        builder2.put(resourceLocation, recipeHolder);
    }
    \END */
}
