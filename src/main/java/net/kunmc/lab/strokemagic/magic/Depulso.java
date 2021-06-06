package net.kunmc.lab.strokemagic.magic;

import com.destroystokyo.paper.entity.TargetEntityInfo;
import net.kunmc.lab.strokemagic.StrokeMagic;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.bukkit.entity.Entity;
import java.util.Map;

public class Depulso implements Magic {
    private final String name;
    private final String stroke;
    private final String description;
    private final int maxDistance;
    private final int power;

    public Depulso() {
        Map<String, String> config = StrokeMagic.getConfiguration().getMagicConfig("Depulso");
        name = config.get("name");
        stroke = config.get("stroke");
        description = config.get("description");
        maxDistance = Integer.parseInt(config.get("maxDistance"));
        power = Integer.parseInt(config.get("power"));
    }

    @Override
    public void run(Player player) {
        Entity entity = player.getTargetEntity(maxDistance);
        if (entity == null) {
            return;
        }

        Vector newVelocity = entity.getVelocity().add(player.getLocation().getDirection().multiply(power));
        entity.setVelocity(newVelocity);
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
