package net.kunmc.lab.strokemagic.magic;

import net.kunmc.lab.strokemagic.StrokeMagic;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class Lightning implements Magic {
    private final String name;
    private final String stroke;
    private final String description;
    private final int maxDistance;

    public Lightning() {
        Map<String, String> config = StrokeMagic.getConfiguration().getMagicConfig("Lightning");
        name = config.get("name");
        stroke = config.get("stroke");
        description = config.get("description");
        maxDistance = Integer.parseInt(config.get("maxDistance"));
    }

    @Override
    public void onReady(Player p) {

    }

    @Override
    public void run(Player p) {
        Entity targetEntity = p.getTargetEntity(maxDistance);
        Block targetBlock = p.getTargetBlock(maxDistance);
        if (targetEntity != null) {
            targetEntity.getWorld().strikeLightning(targetEntity.getLocation());
        } else if (targetBlock != null) {
            targetBlock.getWorld().strikeLightning(targetBlock.getLocation());
        } else {
            Location point = p.getEyeLocation().add(p.getLocation().getDirection().multiply(maxDistance));
            p.getWorld().strikeLightning(point);
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
