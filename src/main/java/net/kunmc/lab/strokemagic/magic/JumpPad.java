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
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JumpPad implements Magic, Listener {
    private final JavaPlugin plugin = StrokeMagic.getInstance();
    private final String name;
    private final String stroke;
    private final String description;
    private final String metadataKey = "JumpPadSnowball";
    private final List<Block> jumpPads = new ArrayList<>();
    //Listの場合,地面に着地することなく2回以上連続でジャンプパッドに乗るとaddとremoveの回数がズレるのでMap
    private final Map<Player, Boolean> isPlayerFalling = new HashMap<>();

    public JumpPad() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        Map<String, String> config = StrokeMagic.getConfiguration().getMagicConfig("JumpPad");
        name = config.get("name");
        stroke = config.get("stroke");
        description = config.get("description");
    }

    @Override
    public void run(Player player) {
        Entity ball = player.getWorld().spawnEntity(player.getEyeLocation(), EntityType.SNOWBALL);
        ball.setVelocity(player.getLocation().getDirection());
        ball.setMetadata(metadataKey, new FixedMetadataValue(plugin, 256));
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
    public void onSnowballHit(ProjectileHitEvent e) {
        Entity projectile = e.getEntity();
        if (projectile.getType().equals(EntityType.SNOWBALL) && projectile.hasMetadata(metadataKey)) {
            Block b = projectile.getLocation().getBlock();
            b.setType(Material.PRISMARINE_SLAB);
            jumpPads.add(b);

            new BukkitRunnable() {
                @Override
                public void run() {
                    jumpPads.remove(b);
                    b.setType(Material.AIR);
                }
            }.runTaskLater(plugin, 100);
        }
    }

    @EventHandler
    public void onJumpPad(PlayerMoveEvent e) {
        Location to = e.getTo();
        if (jumpPads.contains(to.getBlock())) {
            Player p = e.getPlayer();
            isPlayerFalling.put(p, true);
            p.setVelocity(p.getVelocity().add(new Vector(0, 1.2, 0)));
        }
    }

    @EventHandler
    public void onFallingDamage(EntityDamageEvent e) {
        if (!e.getCause().equals(EntityDamageEvent.DamageCause.FALL)) return;
        if (!(e.getEntity() instanceof Player)) return;
        Player p = ((Player) e.getEntity());
        if (isPlayerFalling.get(p) != null && isPlayerFalling.get(p)) {
            p.setFallDistance(0);
            isPlayerFalling.put(p, false);
            e.setCancelled(true);
        }
    }
}
