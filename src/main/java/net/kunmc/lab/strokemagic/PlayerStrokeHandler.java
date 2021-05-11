package net.kunmc.lab.strokemagic;

import net.kunmc.lab.strokemagic.magic.Magic;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerStrokeHandler {
    private final Map<UUID, String> strokes = new HashMap<>();
    private final Map<UUID, Boolean> isMatched = new HashMap<>();
    private final MagicManager magicManager = MagicManager.getInstance();
    private static final PlayerStrokeHandler singleton = new PlayerStrokeHandler();

    public static PlayerStrokeHandler getInstance() {
        return singleton;
    }

    private PlayerStrokeHandler() {
    }

    public void addStroke(UUID uuid, String stroke) {
        Player p = Bukkit.getPlayer(uuid);
        if (p == null) return;

        isMatched.putIfAbsent(uuid, false);
        if (isMatched.get(uuid)) return;

        String newStroke = getStroke(uuid) + stroke;
        strokes.put(uuid, newStroke);

        if (newStroke.length() >= 16) {
            isMatched.put(uuid, true);
        }

        if (isStrokeMatched(newStroke)) {
            p.sendTitle(ChatColor.AQUA + newStroke, magicManager.getMagic(newStroke).getName(), 5, 65536, 0);
            isMatched.put(uuid, true);
        } else {
            p.sendTitle("", newStroke, 0, 65536, 0);
        }
    }

    private void explode() {

    }

    private String getStroke(UUID uuid) {
        strokes.putIfAbsent(uuid, "");
        return strokes.get(uuid);
    }

    public void cancel(UUID uuid) {
        Player p = Bukkit.getPlayer(uuid);
        String stroke = strokes.get(uuid);
        if (stroke == null) stroke = "";
        if (p != null) p.sendTitle("", ChatColor.DARK_RED + stroke, 0, 10, 0);
        strokes.put(uuid, "");
        isMatched.put(uuid, false);
    }

    public void activateMagic(UUID uuid) {
        String stroke = strokes.get(uuid);
        Player p = Bukkit.getPlayer(uuid);
        if (p == null) return;
        if (isStrokeMatched(stroke)) {
            Magic magic = magicManager.getMagic(stroke);
            p.sendTitle(magic.getAnnounce(), ChatColor.AQUA + stroke, 5, 20, 5);
            magic.run(p);
            strokes.put(uuid, "");
            isMatched.put(uuid, false);
        } else {
            cancel(uuid);
        }
    }

    private boolean isStrokeMatched(String stroke) {
        return magicManager.getMagic(stroke) != null;
    }
}
