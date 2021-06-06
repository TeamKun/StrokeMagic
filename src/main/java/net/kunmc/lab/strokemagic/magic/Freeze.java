package net.kunmc.lab.strokemagic.magic;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import net.kunmc.lab.strokemagic.StrokeMagic;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;

public class Freeze implements Magic {
    private final String name;
    private final String stroke;
    private final String description;
    private final int maxDistance;
    private final int duration;

    public Freeze() {
        Map<String, String> config = StrokeMagic.getConfiguration().getMagicConfig("Freeze");
        name = config.get("name");
        stroke = config.get("stroke");
        description = config.get("description");
        maxDistance = Integer.parseInt(config.get("maxDistance"));
        duration = Integer.parseInt(config.get("duration"));
    }

    @Override
    public void run(Player p) {
        Entity entity = p.getTargetEntity(maxDistance);
        if (entity == null) {
            return;
        }

        if (entity instanceof LivingEntity) {
            ((LivingEntity) entity).setAI(false);
            if (entity instanceof Player) {
                ((Player) entity).setWalkSpeed(0.0F);
            }
        }

        Listener jumpCanceller = new JumpCanceller(entity.getUniqueId());
        Bukkit.getPluginManager().registerEvents(jumpCanceller, StrokeMagic.getInstance());
        new BukkitRunnable() {
            int count = 0;

            @Override
            public void run() {
                BoundingBox boundingBox = entity.getBoundingBox();
                Location location = new Location(entity.getWorld(), boundingBox.getCenterX(), boundingBox.getMaxY(), boundingBox.getCenterZ());
                entity.getWorld().spawnParticle(Particle.SNOW_SHOVEL, location, 2);
                count++;
                if (count >= duration) {
                    this.cancel();
                }
            }
        }.runTaskTimerAsynchronously(StrokeMagic.getInstance(), 0, 1);

        Block block = entity.getLocation().getBlock();
        BlockData originBlockData = block.getBlockData();
        new BukkitRunnable() {
            @Override
            public void run() {
                block.setBlockData(originBlockData);
                if (entity instanceof LivingEntity) {
                    ((LivingEntity) entity).setAI(true);
                    if (entity instanceof Player) {
                        ((Player) entity).setWalkSpeed(0.2F);
                    }
                }
                PlayerJumpEvent.getHandlerList().unregister(jumpCanceller);
            }
        }.runTaskLater(StrokeMagic.getInstance(), duration);
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

    private static class JumpCanceller implements Listener {
        private final UUID uuid;

        public JumpCanceller(UUID uuid) {
            this.uuid = uuid;
        }

        @EventHandler
        public void onJump(PlayerJumpEvent e) {
            if (e.getPlayer().getUniqueId().equals(uuid)) {
                e.setCancelled(true);
            }
        }
    }
}
