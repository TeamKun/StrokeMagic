package net.kunmc.lab.strokemagic.magic;

import net.kunmc.lab.strokemagic.StrokeMagic;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class HomenumRevelio implements Magic {
    private final String name;
    private final String stroke;
    private final String description;
    private final int range;
    private final int duration;

    public HomenumRevelio() {
        Map<String, String> config = StrokeMagic.getConfiguration().getMagicConfig("HomenumRevelio");
        name = config.get("name");
        stroke = config.get("stroke");
        description = config.get("description");
        range = Integer.parseInt(config.get("range"));
        duration = Integer.parseInt(config.get("duration"));
    }

    @Override
    public void onReady(Player p) {

    }

    @Override
    public void run(Player p) {
        Bukkit.selectEntities(p, String.format("@e[type=minecraft:player,distance=1..%d]", range)).stream()
                .map(x -> ((Player) x))
                .forEach(x -> {
                    x.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, duration, 1, false, false));
                });
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
