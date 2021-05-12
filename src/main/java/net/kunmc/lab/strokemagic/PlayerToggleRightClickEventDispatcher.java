package net.kunmc.lab.strokemagic;

import net.kunmc.lab.strokemagic.event.PlayerToggleRightClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

public class PlayerToggleRightClickEventDispatcher implements Listener {
    Map<Player, Boolean> isHoldingMainHand = new HashMap<>();
    Map<Player, Boolean> isHoldingOffHand = new HashMap<>();
    Map<Player, BukkitTask> toggleOffMainHandTasks = new HashMap<>();
    Map<Player, BukkitTask> toggleOffOffHandTasks = new HashMap<>();
    private final int taskDelay = StrokeMagic.getConfiguration().getRightClickHoldOffDelay();

    @EventHandler
    public void onInteractInMainHand(PlayerInteractEvent e) {
        Action action = e.getAction();
        if (action.equals(Action.LEFT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_BLOCK)) return;
        if (e.getHand() != null && e.getHand().equals(EquipmentSlot.OFF_HAND)) return;

        Player p = e.getPlayer();
        if (toggleOffMainHandTasks.get(p) != null) toggleOffMainHandTasks.get(p).cancel();
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                isHoldingMainHand.put(p, false);
                Bukkit.getPluginManager().callEvent(new PlayerToggleRightClickEvent(p, e.getHand(), false));
            }
        }.runTaskLater(StrokeMagic.getInstance(), taskDelay);
        toggleOffMainHandTasks.put(p, task);

        isHoldingMainHand.putIfAbsent(p, false);
        if (isHoldingMainHand.get(p)) return;
        isHoldingMainHand.put(p, true);
        Bukkit.getPluginManager().callEvent(new PlayerToggleRightClickEvent(p, e.getHand(), true));
    }

    @EventHandler
    public void onInteractInOffHand(PlayerInteractEvent e) {
        Action action = e.getAction();
        if (action.equals(Action.LEFT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_BLOCK)) return;
        if (e.getHand() != null && e.getHand().equals(EquipmentSlot.HAND)) return;

        Player p = e.getPlayer();
        if (toggleOffOffHandTasks.get(p) != null) toggleOffOffHandTasks.get(p).cancel();
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                isHoldingOffHand.put(p, false);
                Bukkit.getPluginManager().callEvent(new PlayerToggleRightClickEvent(p, e.getHand(), false));
            }
        }.runTaskLater(StrokeMagic.getInstance(), taskDelay);
        toggleOffOffHandTasks.put(p, task);

        isHoldingOffHand.putIfAbsent(p, false);
        if (isHoldingOffHand.get(p)) return;
        isHoldingOffHand.put(p, true);
        Bukkit.getPluginManager().callEvent(new PlayerToggleRightClickEvent(p, e.getHand(), true));
    }
}
