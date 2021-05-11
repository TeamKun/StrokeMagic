package net.kunmc.lab.strokemagic;

import net.kunmc.lab.strokemagic.event.PlayerToggleRightClickEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StrokeListener implements Listener {
    private final Map<UUID, Boolean> isHolding = new HashMap<>();
    private final Map<UUID, Float> lastYaws = new HashMap<>();
    private final Map<UUID, Float> lastPitches = new HashMap<>();
    private final PlayerStrokeHandler strokeHandler = PlayerStrokeHandler.getInstance();

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        isHolding.putIfAbsent(uuid, false);
        if (!isHolding.get(uuid)) return;

        float currentYaw = p.getLocation().getYaw();
        float currentPitch = p.getLocation().getPitch();
        float yawDiff = currentYaw - lastYaws.get(uuid);
        float pitchDiff = currentPitch - lastPitches.get(uuid);
        if (Math.abs(yawDiff) < 12 && Math.abs(pitchDiff) < 12) return;

        String stroke = detectStroke(yawDiff, pitchDiff);
        lastYaws.put(uuid, currentYaw);
        lastPitches.put(uuid, currentPitch);
        strokeHandler.addStroke(uuid, stroke);
    }

    private String detectStroke(float yawDiff, float pitchDiff) {
        if (Math.abs(yawDiff) > Math.abs(pitchDiff)) {
            //360°部分を跨ぐと反転するので180超えたら正負反転
            if (Math.abs(yawDiff) > 180) yawDiff = -yawDiff;
            return yawDiff > 0 ? "→" : "←";
        } else {
            return pitchDiff > 0 ? "↓" : "↑";
        }
    }

    @EventHandler
    public void onRightClick(PlayerToggleRightClickEvent e) {
        if (!e.getPlayer().getInventory().getItemInMainHand().getType().equals(StrokeMagic.RodMaterial)) return;
        if (e.getHand().equals(EquipmentSlot.OFF_HAND)) return;

        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        if (e.isHolding()) {
            isHolding.put(uuid, true);
            lastYaws.put(uuid, p.getLocation().getYaw());
            lastPitches.put(uuid, p.getLocation().getPitch());
        } else {
            isHolding.put(uuid, false);
            strokeHandler.activateMagic(uuid);
        }
    }

    @EventHandler
    public void onItemChange(PlayerItemHeldEvent e) {
        UUID uuid = e.getPlayer().getUniqueId();
        strokeHandler.cancel(uuid);
        isHolding.put(uuid, false);
    }
}
