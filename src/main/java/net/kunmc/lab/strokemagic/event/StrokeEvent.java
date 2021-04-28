package net.kunmc.lab.strokemagic.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class StrokeEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    protected String stroke;
    private boolean cancelled;

    StrokeEvent(Player who, String stroke) {
        super(who);
        this.stroke = stroke;
    }

    public String getStroke() {
        return this.stroke;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
