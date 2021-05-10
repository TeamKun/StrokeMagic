package net.kunmc.lab.strokemagic.magic;

import net.kunmc.lab.strokemagic.StrokeMagic;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class SkyWalker implements Magic {
    private final int radius = 2;
    private final String name = "SkyWalker";
    private final String stroke = "→←→←";
    private final String announce = "空中散歩術！";
    private final String description = "";

    public SkyWalker() {
    }

    @Override
    public void run(Player player) {
        new BukkitRunnable() {
            int count = 0;

            public void run() {
                Location startLoc = player.getLocation().subtract(radius, 1, radius);

                for (int x = startLoc.getBlockX(); x < startLoc.getBlockX() + radius * 2; x++) {
                    for (int z = startLoc.getBlockZ(); z < startLoc.getBlockZ() + radius * 2; z++) {
                        Location loc = new Location(startLoc.getWorld(), x, startLoc.getBlockY(), z);
                        Block b = loc.getBlock();
                        if (b.getType() == Material.AIR) {
                            b.setType(Material.CYAN_STAINED_GLASS);

                            new BukkitRunnable() {
                                public void run() {
                                    b.setType(Material.AIR);
                                }
                            }.runTaskLater(StrokeMagic.getInstance(), 60L);
                        }
                    }
                }

                if (count > 200) cancel();
                count++;
            }
        }.runTaskTimer(StrokeMagic.getInstance(), 0L, 1L);
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
    public @NotNull String getAnnounce() {
        return this.announce;
    }

    @Override
    public @NotNull String getDescription() {
        return this.description;
    }
}
