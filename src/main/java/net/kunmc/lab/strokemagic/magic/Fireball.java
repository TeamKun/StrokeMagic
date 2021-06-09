package net.kunmc.lab.strokemagic.magic;

import net.kunmc.lab.strokemagic.StrokeMagic;
import net.kunmc.lab.strokemagic.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class Fireball implements Magic, Listener {
    private final String name;
    private final String stroke;
    private final String description;
    private final int affectRadius;
    private final String metadataKey = "FireballMeta";

    public Fireball() {
        Map<String, String> config = StrokeMagic.getConfiguration().getMagicConfig("Fireball");
        name = config.get("name");
        stroke = config.get("stroke");
        description = config.get("description");
        affectRadius = Integer.parseInt(config.get("affectRadius"));
        Bukkit.getPluginManager().registerEvents(this, StrokeMagic.getInstance());
    }

    @Override
    public void onReady(Player p) {

    }

    @Override
    public void run(Player p) {
        org.bukkit.entity.Fireball fireball = ((org.bukkit.entity.Fireball) p.getWorld().spawnEntity(p.getEyeLocation(), EntityType.FIREBALL));
        fireball.setMetadata(metadataKey, new FixedMetadataValue(StrokeMagic.getInstance(), null));
        fireball.setYield(0);
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

    @EventHandler
    public void onFireballHit(ProjectileHitEvent e) {
        Projectile projectile = e.getEntity();
        if (projectile.hasMetadata(metadataKey)) {
            Entity hitEntity = e.getHitEntity();
            if (hitEntity != null) {
                hitEntity.setFireTicks(80);
                Utils.sphereAround(hitEntity.getLocation(), affectRadius).forEach(x -> {
                    Block block = x.getLocation().add(0, 1, 0).getBlock();
                    if (block.getType().equals(Material.AIR) || block.getType().equals(Material.CAVE_AIR)) {
                        block.setType(Material.FIRE);
                    }
                });
            }

            Block hitBlock = e.getHitBlock();
            if (hitBlock != null) {
                Utils.sphereAround(hitBlock.getLocation(), affectRadius).forEach(x -> {
                    Block block = x.getLocation().add(0, 1, 0).getBlock();
                    if (block.getType().equals(Material.AIR) || block.getType().equals(Material.CAVE_AIR)) {
                        block.setType(Material.FIRE);
                    }
                });
            }
        }
    }
}
