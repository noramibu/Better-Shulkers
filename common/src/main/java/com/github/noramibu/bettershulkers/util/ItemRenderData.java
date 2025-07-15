package com.github.noramibu.bettershulkers.util;

import com.github.noramibu.bettershulkers.mixin.DisplayEntityAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Display;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.joml.Vector3f;

/**
 * How specific types of items should render with an ItemDisplay entity
 * @param posOffset The offset from the center of the Shulkerbox
 * @param defaultYaw The yaw for a shulker facing up
 * @param defaultPitch The pitch for a shulker facing up
 */
public record ItemRenderData(Vec3 posOffset, float defaultYaw, float defaultPitch) {
    public static ItemRenderData getRenderData(Item item, Level level, Display.ItemDisplay display) {
        Vec3 posOffset;
        if (item instanceof BlockItem blockItem) {
            Block block = blockItem.getBlock();
            double blockHeight = block.defaultBlockState().getShape(level, BlockPos.ZERO).bounds().getYsize();
            if (block instanceof BaseEntityBlock) {
                // Type: Block Entity
                posOffset = new Vec3(0, 0.3, 0);
            } else if (block.hasDynamicShape() || blockHeight > 1) {
                // Type: Item
                display.getEntityData().set(DisplayEntityAccessor.getScale(), new Vector3f(0.8F, 0.8F, 1.0F));
                posOffset = new Vec3(0F, 0.5, -0.15F);
            } else {
                VoxelShape shape = block.defaultBlockState().getShape(level, BlockPos.ZERO, CollisionContext.empty());
                if (Block.isShapeFullBlock(shape)) {
                    // Type: Full
                    posOffset = new Vec3(0, 0.3, 0);
                } else if (Block.isFaceFull(shape, Direction.DOWN) || Block.isFaceFull(shape, Direction.UP)) {
                    // Type: Slab/Stair
                    posOffset = new Vec3(0, 0.3, 0);
                } else {
                    // Type: Partial Block
                    display.getEntityData().set(DisplayEntityAccessor.getScale(), new Vector3f(0.8F, 0.8F, 1.0F));
                    posOffset = new Vec3(0F, 0.5, 0F);
                }
            }
        } else {
            // Type: Item
            display.getEntityData().set(DisplayEntityAccessor.getScale(), new Vector3f(0.8F, 0.8F, 1.0F));
            posOffset = new Vec3(0F, 0.5, 0F);
        }
        return new ItemRenderData(posOffset, 180.0F, -90.0F); // These are magic values; Some may say, "magic numbers"
    }

    /**
     * Transforms the offsets relative to the top face
     * @param topPos Offset position for the top face
     * @param to The {@link Direction} to transform to
     * @return The new offset
     */
    public static Vec3 transformFromTop(Vec3 topPos, Direction to) {
        double x = topPos.x(), y = topPos.y(), z = topPos.z();

        return switch (to) {
            case UP     -> new Vec3(x, y, z);
            case DOWN   -> new Vec3(x, -y, -z);
            case SOUTH  -> new Vec3(x, z, y);
            case NORTH  -> new Vec3(-x, z, -y);
            case WEST   -> new Vec3(-y, z, -x);
            case EAST   -> new Vec3(y, z, x);
        };
    }

    /**
     * Transforms the pitch relative to the top face
     * @param topPitch The pitch of the item display on the top face
     * @param to The {@link Direction} to translate to
     * @return The new pitch
     */
    public static float transformPitchFromTop(float topPitch, Direction to) {
        return switch (to) {
            case DOWN -> topPitch + 180;
            case UP -> topPitch;
            case NORTH, SOUTH, EAST, WEST -> topPitch + 90;
        };
    }

    /**
     * Transforms the yaw relative to the top face
     * @param topYaw The yaw of the item display on the top face
     * @param to The {@link Direction} to translate to
     * @return The new yaw
     */
    public static float transformYawFromTop(float topYaw, Direction to) {
        return switch (to) {
            case UP, DOWN, NORTH -> topYaw;
            case SOUTH -> topYaw - 180;
            case WEST -> topYaw - 90;
            case EAST -> topYaw + 90;
        };
    }
}
