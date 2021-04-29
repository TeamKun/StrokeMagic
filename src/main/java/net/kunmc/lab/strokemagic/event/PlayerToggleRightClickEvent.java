package net.kunmc.lab.strokemagic.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;

public class PlayerToggleRightClickEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();
    private final boolean isHolding;
    private final EquipmentSlot hand;

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public PlayerToggleRightClickEvent(Player who, EquipmentSlot hand, boolean isHolding) {
        super(who);
        this.hand = hand;
        this.isHolding = isHolding;
    }

    public EquipmentSlot getHand() {
        return this.hand;
    }

    public boolean isHolding() {
        return this.isHolding;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }
}
