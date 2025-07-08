package me.noramibu.mixin.v1_21;

import com.moulberry.mixinconstraints.annotations.IfMinecraftVersion;
import me.noramibu.bettershulkers.accessor.ForceInventory;
import me.noramibu.bettershulkers.accessor.MaterialDisplay;
import me.noramibu.bettershulkers.accessor.ShulkerViewer;
import me.noramibu.bettershulkers.util.ShulkerUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@IfMinecraftVersion(minVersion = "1.21")
@Mixin(ShulkerBoxBlockEntity.class)
public abstract class ShulkerBoxBlockEntityMixin extends LootableContainerBlockEntity implements ForceInventory, MaterialDisplay {

    @Shadow
    private DefaultedList<ItemStack> inventory;
    @Unique
    private boolean forced;
    @Unique
    private DisplayEntity.ItemDisplayEntity display;

    protected ShulkerBoxBlockEntityMixin(net.minecraft.block.entity.BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    @Override
    public void setInventory(DefaultedList<ItemStack> inventory) {
        this.inventory = inventory;
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
    private static void skipTicks(World world, BlockPos pos, BlockState state, ShulkerBoxBlockEntity blockEntity, CallbackInfo ci) {
        if (((ForceInventory)blockEntity).forced()) {
            ci.cancel();
        }
    }

    @Inject(method = "onClose", at = @At("HEAD"), cancellable = true)
    private void removeWhenClosed(PlayerEntity player, CallbackInfo ci) {
        if (this.forced()) {
            if (player instanceof ServerPlayerEntity serverPlayer) {
                int smallestSize = this.getSmallestListIndex();
                if (smallestSize != -1) {
                    DefaultedList<ItemStack> newInventory = DefaultedList.ofSize(smallestSize + 1, ItemStack.EMPTY);
                    for (int i = 0; i <= smallestSize; i++) {
                        newInventory.set(i, this.inventory.get(i));
                    }
                    ((ShulkerViewer)serverPlayer).getViewedStack().set(DataComponentTypes.CONTAINER, ContainerComponent.fromStacks(newInventory));
                } else {
                    ((ShulkerViewer)serverPlayer).getViewedStack().set(DataComponentTypes.CONTAINER, ContainerComponent.fromStacks(List.of()));
                }
                ((ShulkerViewer)serverPlayer).setViewing(null);
            }
            this.markRemoved();
            ci.cancel();
        }
    }

    @Unique
    private int getSmallestListIndex() {
        int lastIndex = -1;
        for (int i = 0; i < 27; i++) {
            if (this.inventory.get(i) != ItemStack.EMPTY) {
                lastIndex = i;
            }
        }
        return lastIndex;
    }

    @Inject(method = "onOpen", at = @At("HEAD"), cancellable = true)
    private void ignoreOpening(PlayerEntity player, CallbackInfo ci) {
        if (this.forced()) {
            ci.cancel();
        }
    }

    @Override
    public void setWorld(World world) {
        super.setWorld(world);
        if (!world.isClient) {
            Item material = ShulkerUtil.getMaterialFromShulkerBlock(this);
            if (material != null) {
                this.createDisplay(material);
            }
        }
    }

    @Override
    public void markRemoved() {
        super.markRemoved();
        if (this.display != null) {
            this.display.remove(Entity.RemovalReason.DISCARDED);
        }
    }

    private void createDisplay(Item material) {
        if (material != null) {
            this.display = new DisplayEntity.ItemDisplayEntity(EntityType.ITEM_DISPLAY, this.getWorld());
            Vec3d positionOfShulker = pos.toCenterPos();

            float pitch = 0.0F;
            float yaw = 0.0F;
            float heightOffset = 0.3F;
            if (material instanceof BlockItem blockItem) {
                Block block = blockItem.getBlock();
                if (!(block instanceof BlockWithEntity)) {
                    double blockHeight = block.getDefaultState().getOutlineShape(this.getWorld(), null).getBoundingBox().getLengthY();
                    if (blockHeight == 0.5) {
                        pitch = 90.0F;
                        positionOfShulker = positionOfShulker.add(0, 0, 0.05);
                    } else {
                        heightOffset += (float) (1 - blockHeight) / 2F;
                    }
                } else {
                    pitch = -90.0F;
                    yaw = 180.0F;
                }
            } else {
                pitch = 90.0F;
                this.display.getDataTracker().set(DisplayEntityAccessor.getScale(), new Vector3f(0.8F, 0.8F, 1.0F));
                positionOfShulker = positionOfShulker.add(-0.02F, 0, 0.02F);
                heightOffset = 0.5F;
            }

            this.display.refreshPositionAndAngles(positionOfShulker.x, positionOfShulker.y + heightOffset, positionOfShulker.z, yaw, pitch);
            this.display.setNoGravity(true);
            ((ItemDisplayEntityInvoker) this.display).invokeSetItemStack(material.getDefaultStack());
            ((ItemDisplayEntityInvoker) this.display).invokeSetTransformationMode(ModelTransformationMode.FIXED);
            ((DisplayEntityAccessor) this.display).invokeSetBillboardMode(DisplayEntity.BillboardMode.FIXED);

            NbtCompound nbt = new NbtCompound();
            this.display.writeNbt(nbt);
            nbt.putBoolean("PersistenceRequired", true);
            this.display.readNbt(nbt);

            this.getWorld().spawnEntity(this.display);
        }
    }

    @Override
    public void createDisplay(ItemStack shulkerStack) {
        Item material = ShulkerUtil.getMaterialFromShulker(shulkerStack);
        this.createDisplay(material);
    }
}