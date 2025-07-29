package com.github.noramibu.bettershulkers.mixin;

/*\ <=1.21.1
import com.github.noramibu.bettershulkers.recipe.FakeRecipe;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.spongepowered.asm.mixin.injection.At;
import java.util.ArrayList;
import java.util.List;
\END */
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundUpdateRecipesPacket;
import org.spongepowered.asm.mixin.Mixin;

/**
 * @author Patbox
 * This code is borrowed from Polymer
 * https://github.com/Patbox/polymer/blob/dev/1.21/polymer-core/src/main/java/eu/pb4/polymer/core/mixin/item/packet/SynchronizeRecipesS2CPacketMixin.java
 */
@Mixin(ClientboundUpdateRecipesPacket.class)
public abstract class RecipeFilterMixin implements Packet<ClientGamePacketListener> {
    /*\ <=1.21.1
    @ModifyReturnValue(method = "method_55955", at = @At("TAIL"))
    private static List<RecipeHolder<?>> filterRecipes(List<RecipeHolder<?>> recipes) {
        List<RecipeHolder<?>> list = new ArrayList<>();
        for (var recipe : recipes) {
            if (!(recipe.value() instanceof FakeRecipe)) {
                list.add(recipe);
            }
        }
        return list;
    }
    @Override
    public boolean isSkippable() {
        return true;
    }
    \END */
}
