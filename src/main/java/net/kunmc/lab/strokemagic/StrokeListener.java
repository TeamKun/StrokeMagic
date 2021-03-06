package net.kunmc.lab.strokemagic;

import net.kunmc.lab.strokemagic.event.PlayerToggleRightClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StrokeListener implements Listener {
    private final JavaPlugin plugin = StrokeMagic.getInstance();
    private final Map<UUID, Boolean> isHolding = new HashMap<>();
    private final Map<UUID, Float> lastYaws = new HashMap<>();
    private final Map<UUID, Float> lastPitches = new HashMap<>();
    private final Map<UUID, BukkitTask> povTasks = new HashMap<>();
    private final PlayerStrokeHandler strokeHandler = PlayerStrokeHandler.getInstance();
    private final Material RodMaterial = StrokeMagic.getConfiguration().getRodMaterial();

    private class RetrievePlayerPOVTask extends BukkitRunnable {
        UUID uuid;

        public RetrievePlayerPOVTask(UUID uuid) {
            this.uuid = uuid;
        }

        @Override
        public void run() {
            Player p = Bukkit.getPlayer(uuid);
            if (p == null) return;

            isHolding.putIfAbsent(uuid, false);
            if (!isHolding.get(uuid)) return;

            float currentYaw = p.getLocation().getYaw();
            float currentPitch = p.getLocation().getPitch();
            float yawDiff = calcYawDiff(currentYaw, lastYaws.get(uuid));
            float pitchDiff = currentPitch - lastPitches.get(uuid);
            if (Math.abs(yawDiff) < StrokeMagic.getConfiguration().getYawInputBorderDegree()
                    && Math.abs(pitchDiff) < StrokeMagic.getConfiguration().getPitchInputBorderDegree()) return;

            String stroke = detectStroke(yawDiff, pitchDiff);
            lastYaws.put(uuid, currentYaw);
            lastPitches.put(uuid, currentPitch);
            strokeHandler.addStroke(uuid, stroke);
        }
    }

    private float calcYawDiff(float yaw1, float yaw2) {
        float yawDiff = yaw1 - yaw2;
        if (yawDiff > 270) yawDiff = -360 + yawDiff;
        if (yawDiff < -270) yawDiff = 360 + yawDiff;
        return yawDiff;
    }

    private String detectStroke(float yawDiff, float pitchDiff) {
        if (Math.abs(yawDiff) > Math.abs(pitchDiff)) {
            return yawDiff > 0 ? "???" : "???";
        } else {
            return pitchDiff > 0 ? "???" : "???";
        }
    }

    @EventHandler
    public void onRightClick(PlayerToggleRightClickEvent e) {
        if (!e.getPlayer().getInventory().getItemInMainHand().getType().equals(RodMaterial)) return;
        if (e.getHand().equals(EquipmentSlot.OFF_HAND)) return;

        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        if (e.isHolding()) {
            isHolding.put(uuid, true);
            BukkitTask task = new RetrievePlayerPOVTask(uuid).runTaskTimer(plugin, 0, 1);
            povTasks.put(uuid, task);

            lastYaws.put(uuid, p.getLocation().getYaw());
            lastPitches.put(uuid, p.getLocation().getPitch());
        } else {
            isHolding.put(uuid, false);
            if (povTasks.get(uuid) != null) povTasks.get(uuid).cancel();

            strokeHandler.activateMagic(uuid);
        }
    }

    @EventHandler
    public void onItemChange(PlayerItemHeldEvent e) {
        UUID uuid = e.getPlayer().getUniqueId();
        strokeHandler.cancel(uuid);
        isHolding.put(uuid, false);
        if (povTasks.get(uuid) != null) povTasks.get(uuid).cancel();
    }
}
