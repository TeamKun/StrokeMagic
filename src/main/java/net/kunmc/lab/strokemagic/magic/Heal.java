package net.kunmc.lab.strokemagic.magic;

import net.kunmc.lab.strokemagic.StrokeMagic;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class Heal implements Magic {
    private final String name;
    private final String stroke;
    private final String description;

    public Heal() {
        Map<String, String> config = StrokeMagic.getConfiguration().getHealConfig();
        name = config.get("name");
        stroke = config.get("stroke");
        description = config.get("description");
    }

    @Override
    public void run(Player p) {
        double currentHealth = p.getHealth();
        if (currentHealth <= 16.0) {
            p.setHealth(currentHealth + 4.0);
        } else {
            p.setHealth(20.0);
        }
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
