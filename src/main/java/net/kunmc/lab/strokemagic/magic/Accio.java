package net.kunmc.lab.strokemagic.magic;

import net.kunmc.lab.strokemagic.StrokeMagic;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class Accio implements Magic {
    private final String name;
    private final String stroke;
    private final String description;
    private final int maxDistance;

    public Accio() {
        Map<String, String> config = StrokeMagic.getConfiguration().getMagicConfig("Accio");
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
        Vector direction = p.getLocation().getDirection();
        Location to = p.getEyeLocation().add(direction.getX(), -0.4, direction.getZ());

        Entity hitEntity = p.getTargetEntity(maxDistance);
        if (hitEntity != null) {
            to.setPitch(hitEntity.getLocation().getPitch());
            to.setYaw(hitEntity.getLocation().getYaw());
            hitEntity.teleport(to);
            return;
        }

        Block hitBlock = p.getTargetBlock(maxDistance);
        if (hitBlock != null) {
            BlockData blockData = hitBlock.getBlockData();
            hitBlock.setType(Material.AIR);
            FallingBlock fallingBlock = hitBlock.getWorld().spawnFallingBlock(hitBlock.getLocation(), blockData);
            fallingBlock.teleport(to);
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
