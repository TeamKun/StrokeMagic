package net.kunmc.lab.strokemagic.magic;

import org.bukkit.entity.Player;

public interface Magic {
    void run(Player player);

    String getName();

    String getStroke();

    String getAnnounce();

    String getDescription();
}
