package com.github.noramibu.bettershulkers.mixin;

import com.github.noramibu.bettershulkers.Config;
import com.github.noramibu.bettershulkers.interfaces.ForceInventory;
import com.github.noramibu.bettershulkers.interfaces.MaterialDisplay;
import com.github.noramibu.bettershulkers.interfaces.ShulkerViewer;
import com.github.noramibu.bettershulkers.interfaces.UpdatingAnimation;
import com.github.noramibu.bettershulkers.util.Animation;
import com.github.noramibu.bettershulkers.util.ItemRenderData;
import com.github.noramibu.bettershulkers.util.ShulkerUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.protocol.game.ClientboundSetCursorItemPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Display;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ShulkerBoxBlockEntity.class)
public abstract class ShulkerBoxBlockEntityMixin extends RandomizableContainerBlockEntity implements ForceInventory, MaterialDisplay {

    @Shadow private NonNullList<ItemStack> itemStacks;
    @Unique
    private boolean forced;
    @Unique
    private Display.ItemDisplay display;

    protected ShulkerBoxBlockEntityMixin(net.minecraft.world.level.block.entity.BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    @Override
    public void setInventory(NonNullList<ItemStack> inventory) {
        this.itemStacks = inventory;
    }

    @Override
    public void setForced() {
        this.forced = true;
    }

    @Override
    public boolean forced() {
        return this.forced;
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private static void skipTicks(Level world, BlockPos pos, BlockState state, ShulkerBoxBlockEntity blockEntity, CallbackInfo ci) {
        if (((ForceInventory)blockEntity).forced()) {
            ci.cancel();
        }
    }

    @Inject(method = "stopOpen", at = @At("HEAD"), cancellable = true)
    private void removeWhenClosed(Player player, CallbackInfo ci) {
        if (this.forced()) {
            if (player instanceof ServerPlayer serverPlayer) {
                ShulkerUtil.saveShulkerInventory(this.itemStacks, serverPlayer);
                ((ShulkerViewer)serverPlayer).setViewing(null, null);
                // Prevent ghost items
                serverPlayer.connection.send(new ClientboundSetCursorItemPacket(ItemStack.EMPTY));
            }
            this.setRemoved();
            ci.cancel();
        }

        // Item Display animation
        if (this.display != null) {
            Animation animation = new Animation(10, 270F, 0F, -0.4987F, -0.01F, 0.015F, 0.01F);
            ((UpdatingAnimation)this.display).addAnimation(animation);
        }
    }

    @Inject(method = "startOpen", at = @At("HEAD"), cancellable = true)
    private void ignoreOpening(Player player, CallbackInfo ci) {
        if (this.forced()) {
            ci.cancel();
        }

        // Item Display animation
        if (this.display != null) {
            Animation animation = new Animation(10, 0F,270F, 0F,-0.4987F, 0F, 0F);
            animation.execute(this.display);
            ((UpdatingAnimation)this.display).addAnimation(animation);
        }
    }

    @Override
    public void setLevel(Level world) {
        super.setLevel(world);
        if (!world.isClientSide()) {
            Item material = ShulkerUtil.getMaterialFromShulkerBlock(this);
            this.createDisplay(material);
        }
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        if (this.display != null) {
            this.display.remove(Entity.RemovalReason.DISCARDED);
        }
    }

    private void createDisplay(Item material) {
        if (Config.SHOW_MATERIAL_DISPLAY && material != null) {
            this.display = new Display.ItemDisplay(EntityType.ITEM_DISPLAY, this.getLevel());
            Vec3 positionOfShulker = this.worldPosition.getCenter();
            Direction rotation = this.getBlockState().getValue(BlockStateProperties.FACING);

            ItemRenderData renderData = ItemRenderData.getRenderData(this.display);
            Vec3 positionOffset = renderData.posOffset();
            float pitch = renderData.defaultPitch();
            float yaw = renderData.defaultYaw();

            Vec3 modifiedPositionOffset = ItemRenderData.transformFromTop(positionOffset, rotation);

            Vec3 finalPos = positionOfShulker.add(modifiedPositionOffset);
            float finalYaw = ItemRenderData.transformYawFromTop(yaw, rotation);
            float finalPitch = ItemRenderData.transformPitchFromTop(pitch, rotation);
            setDisplayPos(finalPos, finalYaw, finalPitch);

            this.display.setNoGravity(true);
            this.display.getEntityData().set(DisplayEntityAccessor.getTranslation(), new Vector3f(0, 0, 0F));
            ((ItemDisplayInvoker) this.display).invokeSetItemStack(material.getDefaultInstance());
            ((ItemDisplayInvoker) this.display).invokeSetTransformationMode(ItemDisplayContext.GUI);
            ((DisplayEntityAccessor) this.display).invokeSetBillboardConstraints(Display.BillboardConstraints.FIXED);
            this.getLevel().addFreshEntity(this.display);
        }
    }

    private void setDisplayPos(Vec3 pos, float yaw, float pitch) {
        //: >=1.21.5
        this.display.snapTo(pos.x, pos.y, pos.z, yaw, pitch);
        //: END

        /*\ <=1.21.4
        this.display.moveTo(pos.x, pos.y, pos.z, yaw, pitch);
        \END */
    }

    @Override
    public void createDisplay(ItemStack shulkerStack) {
        Item material = ShulkerUtil.getMaterialFromShulker(shulkerStack);
        this.createDisplay(material);
    }

    @Override
    public void saveWithFullMetadata(ValueOutput valueOutput) {
        if (!this.forced()) {
            super.saveWithFullMetadata(valueOutput);
        }
    }

    @Override
    public void saveWithId(ValueOutput valueOutput) {
        if (!this.forced()) {
            super.saveWithId(valueOutput);
        }
    }

    @Override
    public void saveWithoutMetadata(ValueOutput valueOutput) {
        if (!this.forced()) {
            super.saveWithoutMetadata(valueOutput);
        }
    }

    @Override
    public void saveCustomOnly(ValueOutput valueOutput) {
        if (!this.forced()) {
            super.saveCustomOnly(valueOutput);
        }
    }

    @Inject(method = "saveAdditional", at = @At("HEAD"), cancellable = true)
    private void doNotSaveData(ValueOutput valueOutput, CallbackInfo ci) {
        if (this.forced()) {
            ci.cancel();
        }
    }
}
