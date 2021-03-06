package net.kunmc.lab.strokemagic.magic;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface Magic {
    void onReady(Player player);

    void run(Player player);

    @NotNull String getName();

    @NotNull String getStroke();

    @NotNull String getDescription();
}
