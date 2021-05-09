package net.kunmc.lab.strokemagic;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerStrokeManager {
    private final Map<UUID, String> strokes = new HashMap<>();
    private static final PlayerStrokeManager singleton = new PlayerStrokeManager();

    public static PlayerStrokeManager getInstance() {
        return singleton;
    }

    private PlayerStrokeManager() {
    }

    public void addStroke(UUID uuid, String stroke) {
        Player p = Bukkit.getPlayer(uuid);
        if (p == null) return;

        String newStroke = getStroke(uuid) + stroke;
        strokes.put(uuid, newStroke);
        p.sendTitle("", newStroke, 0, 65536, 0);
    }

    private String getStroke(UUID uuid) {
        strokes.putIfAbsent(uuid, "");
        return strokes.get(uuid);
    }

    public void resetStroke(UUID uuid) {
        Player p = Bukkit.getPlayer(uuid);
        if (p != null) p.sendTitle("", ChatColor.DARK_RED + strokes.get(uuid), 0, 10, 0);
        strokes.put(uuid, "");
    }
}
