package net.kunmc.lab.strokemagic.magic;

import net.kunmc.lab.strokemagic.StrokeMagic;
import net.kunmc.lab.strokemagic.util.Utils;
import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class BlackHole implements Magic {
    private final String name;
    private final String stroke;
    private final String description;
    private final double maxDistance;
    private final double radius;

    public BlackHole() {
        Map<String, String> config = StrokeMagic.getConfiguration().getMagicConfig("BlackHole");
        name = config.get("name");
        stroke = config.get("stroke");
        description = config.get("description");
        maxDistance = Double.parseDouble(config.get("maxDistance"));
        radius = Double.parseDouble(config.get("radius"));
    }

    @Override
    public void onReady(Player p) {

    }

    @Override
    public void run(Player p) {
        new BukkitRunnable() {
            private final Location origin = p.getEyeLocation().clone();
            private final Location point = p.getEyeLocation().clone().add(origin.getDirection().clone().multiply(3));

            @Override
            public void run() {
                point.add(point.getDirection().multiply(0.75));
                p.getWorld().spawnParticle(Particle.REDSTONE, point, 3, new Particle.DustOptions(Color.BLACK, 10));
                if (point.distance(origin) > maxDistance) {
                    this.cancel();
                }

                Utils.sphereAround(point, (int) radius).forEach(x -> {
                    if (x.getType().equals(Material.AIR)) {
                        return;
                    }

                    BlockData blockData = x.getBlockData();
                    x.setType(Material.AIR);
                    FallingBlock fallingBlock = x.getWorld().spawnFallingBlock(x.getLocation(), blockData);
                    Vector vector = point.toVector().subtract(x.getLocation().toVector());
                    fallingBlock.setVelocity(vector.multiply(0.35 / vector.length()));
                });

                Bukkit.selectEntities(Bukkit.getConsoleSender(),
                        String.format("@e[x=%f,y=%f,z=%f,distance=0..%f]", point.getX(), point.getY(), point.getZ(), radius))
                        .stream()
                        .filter(x -> !x.equals(p))
                        .forEach(x -> {
                            Vector vector = point.toVector().subtract(x.getLocation().toVector());
                            x.setVelocity(vector.multiply(0.35 / vector.length()));
                        });
            }
        }.runTaskTimer(StrokeMagic.getInstance(), 0, 4);
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
