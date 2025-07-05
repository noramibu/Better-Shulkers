package me.noramibu.bettershulkers.event;

import me.noramibu.bettershulkers.BetterShulkers;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.TypedActionResult;

public class UseItemListener {
    public static void register() {
        UseItemCallback.EVENT.register((player, world, hand) -> {
            if (!world.isClient()) return TypedActionResult.pass(player.getStackInHand(hand));

            if (player.getStackInHand(hand).getItem() == Items.STICK) {
                player.sendMessage(Text.literal("You used a stick!"), true);
                return TypedActionResult.success(player.getStackInHand(hand));
            }
            net.minecraft.util.TypedActionResult<ItemStack> result = TypedActionResult.success(player.getStackInHand(hand));
            return result;
        });
    }
} 