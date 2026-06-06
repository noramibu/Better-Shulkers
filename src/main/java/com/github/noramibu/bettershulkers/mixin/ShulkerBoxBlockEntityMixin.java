/**
 * Copyright (c) 2026 noramibu, QPCrummer
 * This project is Licensed under <a href="https://github.com/noramibu/Better-Shulkers/blob/main/LICENSE">MIT</a>
 */
package com.github.noramibu.bettershulkers.mixin;

import com.github.noramibu.bettershulkers.gamerules.BetterShulkersGameRules;
import com.github.noramibu.bettershulkers.material.ItemDataStorage;
import com.github.noramibu.bettershulkers.material.ShulkerMaterialManager;
import com.github.noramibu.bettershulkers.material.display.Animation;
import com.github.noramibu.bettershulkers.material.display.ItemRenderData;
import com.github.noramibu.bettershulkers.material.display.UpdatingAnimation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ContainerUser;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ShulkerBoxBlockEntity.class)
public abstract class ShulkerBoxBlockEntityMixin extends RandomizableContainerBlockEntity implements ItemDataStorage {
    private static final String LORE_ID = "lore";
    private CustomData data = CustomData.EMPTY;
    private ItemLore lore = ItemLore.EMPTY;
    private Display.ItemDisplay display;

    protected ShulkerBoxBlockEntityMixin(BlockEntityType<?> type, BlockPos worldPosition, BlockState blockState) {
        super(type, worldPosition, blockState);
    }

    @Inject(method = "loadAdditional", at = @At("HEAD"))
    private void bettershulkers$loadCustomData(ValueInput input, CallbackInfo ci) {
        this.data = input.read(ShulkerMaterialManager.MATERIAL_ID, CustomData.CODEC)
                .orElse(CustomData.EMPTY);
        this.lore = input.read(LORE_ID, ItemLore.CODEC)
                .orElse(ItemLore.EMPTY);
    }

    @Inject(method = "saveAdditional", at = @At("HEAD"))
    private void bettershulkers$saveCustomData(ValueOutput output, CallbackInfo ci) {
        output.store(ShulkerMaterialManager.MATERIAL_ID, CustomData.CODEC, this.data);
        output.store(LORE_ID, ItemLore.CODEC, this.lore);
    }

    @Override
    public void storeItemData(ItemStack stack) {
        this.data = stack.get(DataComponents.CUSTOM_DATA);
        this.lore = stack.get(DataComponents.LORE);
    }

    @Override
    public CustomData getData() {
        return this.data;
    }

    @Override
    public ItemLore getLore() {
        return this.lore;
    }

    @Inject(method = "stopOpen", at = @At("HEAD"))
    private void bettershulkers$stopAnimation(ContainerUser containerUser, CallbackInfo ci) {
        // Item Display animation
        if (this.display != null
                && !this.level.isClientSide()
                && ((ServerLevel) this.level).getGameRules().get(BetterShulkersGameRules.DO_SHULKER_DISPLAY_ANIMATIONS)) {
            Animation animation = new Animation(10, 270F, 0F, -0.4987F, -0.01F, 0.015F, 0.01F);
            ((UpdatingAnimation)this.display).addAnimation(animation);
        }
    }

    @Inject(method = "startOpen", at = @At("HEAD"))
    private void bettershulkers$openAnimation(ContainerUser containerUser, CallbackInfo ci) {
        // Item Display animation
        if (this.display != null
                && !this.level.isClientSide()
                && ((ServerLevel) this.level).getGameRules().get(BetterShulkersGameRules.DO_SHULKER_DISPLAY_ANIMATIONS)) {
            Animation animation = new Animation(10, 0F,270F, 0F,-0.4987F, 0F, 0F);
            animation.execute(this.display);
            ((UpdatingAnimation)this.display).addAnimation(animation);
        }
    }

    @Override
    public void setLevel(Level world) {
        super.setLevel(world);
        if (!world.isClientSide()) {
            Item material = ShulkerMaterialManager.getMaterial(this.data);
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
        if (((ServerLevel) this.level).getGameRules().get(BetterShulkersGameRules.DO_SHULKER_MATERIAL_DISPLAYS)
                && material != null) {
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
        this.display.snapTo(pos.x, pos.y, pos.z, yaw, pitch);
    }
}
