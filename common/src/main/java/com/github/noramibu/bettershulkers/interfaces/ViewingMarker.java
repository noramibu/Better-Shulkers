package com.github.noramibu.bettershulkers.interfaces;

import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public interface ViewingMarker {
    ServerPlayer getViewer();
    void setViewer(@Nullable ServerPlayer player);
    default boolean isBeingViewed() {
        return this.getViewer() != null;
    }
}
