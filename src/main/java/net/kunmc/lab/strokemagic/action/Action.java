package net.kunmc.lab.strokemagic.action;

import org.bukkit.entity.Player;

public interface Action {
    void run(Player player);

    String getName();

    String getStroke();

    String getAnnounce();

    String getDescription();
}
