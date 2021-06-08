package net.kunmc.lab.strokemagic.magic;

import net.kunmc.lab.strokemagic.StrokeMagic;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeyOfTheKingsLaw implements Magic, Listener {
    private final String name;
    private final String stroke;
    private final String description;
    private final int maxDistance;
    private final double speed;
    private final double gap;
    private final String metadataKey = "KeyOfTheKingsLawTrident";
    private final Map<Player, List<Entity>> playerTridentListMap = new HashMap<>();

    public KeyOfTheKingsLaw() {
        Map<String, String> config = StrokeMagic.getConfiguration().getMagicConfig("KeyOfTheKingsLaw");
        name = config.get("name");
        stroke = config.get("stroke");
        description = config.get("description");
        maxDistance = Integer.parseInt(config.get("maxDistance"));
        speed = Double.parseDouble(config.get("speed"));
        gap = Double.parseDouble(config.get("gap"));
        Bukkit.getPluginManager().registerEvents(this, StrokeMagic.getInstance());
    }

    @Override
    public void onReady(Player p) {
        playerTridentListMap.put(p, new ArrayList<>());
        List<Entity> tridentList = playerTridentListMap.get(p);

        Location eyeLocation = p.getEyeLocation();
        float currentYaw = Math.abs(p.getEyeLocation().getYaw());

        //TODO スポーン位置の調整
        for (int i = 1; i <= 3; i++) {
            p.getWorld().spawnEntity(eyeLocation.clone().add(0, gap * i, 0),
                    EntityType.TRIDENT,
                    CreatureSpawnEvent.SpawnReason.CUSTOM,
                    x -> {
                        x.setGravity(false);
                        x.setVelocity(eyeLocation.getDirection().setY(0).multiply(0.0001));
                        x.setMetadata(metadataKey, new FixedMetadataValue(StrokeMagic.getInstance(), null));
                        tridentList.add(x);
                    });

            double right = Math.toRadians(currentYaw + 90);
            p.getWorld().spawnEntity(eyeLocation.clone().add(Math.sin(right) * gap * i, 0, Math.cos(right) * gap * i),
                    EntityType.TRIDENT,
                    CreatureSpawnEvent.SpawnReason.CUSTOM,
                    x -> {
                        x.setGravity(false);
                        x.setVelocity(eyeLocation.getDirection().setY(0).multiply(0.0001));
                        x.setMetadata(metadataKey, new FixedMetadataValue(StrokeMagic.getInstance(), null));
                        tridentList.add(x);
                    });

            double left = Math.toRadians(currentYaw - 90);
            p.getWorld().spawnEntity(eyeLocation.clone().add(Math.sin(left) * gap * i, 0, Math.cos(left) * gap * i),
                    EntityType.TRIDENT,
                    CreatureSpawnEvent.SpawnReason.CUSTOM,
                    x -> {
                        x.setGravity(false);
                        x.setVelocity(eyeLocation.getDirection().setY(0).multiply(0.0001));
                        x.setMetadata(metadataKey, new FixedMetadataValue(StrokeMagic.getInstance(), null));
                        tridentList.add(x);
                    });
        }
    }

    @Override
    public void run(Player p) {
        Entity targetEntity = p.getTargetEntity(maxDistance);
        Block targetBlock = p.getTargetBlock(maxDistance);
        Location point;
        if (targetEntity != null) {
            point = targetEntity.getBoundingBox().getCenter().toLocation(targetEntity.getWorld());
        } else if (targetBlock != null && !targetBlock.getType().equals(Material.AIR)) {
            point = targetBlock.getBoundingBox().getCenter().toLocation(targetBlock.getWorld());
        } else {
            point = p.getEyeLocation().clone().add(p.getLocation().getDirection().multiply(maxDistance));
        }

        List<Entity> tridentList = playerTridentListMap.get(p);
        tridentList.forEach(x -> {
            Location location = x.getLocation();
            Vector velocity = new Vector(point.getX() - location.getX(),
                    point.getY() - location.getY(),
                    point.getZ() - location.getZ());
            x.setVelocity(velocity.multiply(speed / velocity.length()));

            new BukkitRunnable() {
                @Override
                public void run() {
                    x.remove();
                }
            }.runTaskLaterAsynchronously(StrokeMagic.getInstance(), 60);
        });
        tridentList.clear();
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

    @EventHandler
    public void onTridentHit(ProjectileHitEvent e) {
        Entity projectile = e.getEntity();
        if (projectile.hasMetadata(metadataKey)) {
            projectile.remove();
        }
    }
}
