package net.kunmc.lab.strokemagic.magic;

import net.kunmc.lab.strokemagic.StrokeMagic;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class KeyOfTheKingsLaw implements Magic {
    private final String name;
    private final String stroke;
    private final String description;

    public KeyOfTheKingsLaw() {
        Map<String, String> config = StrokeMagic.getConfiguration().getMagicConfig("KeyOfTheKingsLaw");
        name = config.get("name");
        stroke = config.get("stroke");
        description = config.get("description");
    }

    @Override
    public void onReady(Player p) {

    }

    @Override
    public void run(Player p) {
        
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
