package me.noramibu.mixin.v1_21_5;

import com.moulberry.mixinconstraints.annotations.IfMinecraftVersion;
import me.noramibu.bettershulkers.BetterShulkers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@IfMinecraftVersion(minVersion = "1.21", maxVersion = "1.21.5")
@Mixin(Item.class)
public abstract class ItemMixin {
    @Inject(method = "use", at = @At("HEAD"), require = 0)
    private void bettershulkers$onUse(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<Object> cir) {
        String side = world.isClient() ? "CLIENT" : "SERVER";
        ItemStack stack = user.getStackInHand(hand);
        if (!stack.isEmpty()) {
            BetterShulkers.LOGGER.info("[{}] Right-clicked with item: {}", side, stack.getItem());
        }

    }
} 