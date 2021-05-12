package net.kunmc.lab.strokemagic.magic;

import net.kunmc.lab.strokemagic.StrokeMagic;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class Fireball implements Magic {
    private final String name;
    private final String stroke;
    private final String description;

    public Fireball() {
        Map<String, String> config = StrokeMagic.getConfiguration().getFireballConfig();
        name = config.get("name");
        stroke = config.get("stroke");
        description = config.get("description");
    }

    @Override
    public void run(Player p) {
        p.getWorld().spawnEntity(p.getEyeLocation(), EntityType.FIREBALL);
        p.getWorld().playSound(p.getLocation(), Sound.ITEM_FIRECHARGE_USE, 1.0F, 1.0F);
    }

    @Override
    public @NotNull String getName() {
        return this.name;
    }

    @Override
    public @NotNull String getStroke() {
        return this.stroke;
    }
    
    @Override
    public @NotNull String getDescription() {
        return this.description;
    }
}
