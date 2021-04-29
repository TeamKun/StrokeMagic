package net.kunmc.lab.strokemagic;

import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

public final class StrokeMagic extends JavaPlugin {
    public static final Material RodMaterial = Material.BLAZE_ROD;
    private static StrokeMagic INSTANCE;

    public static StrokeMagic getInstance() {
        return INSTANCE;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        INSTANCE = this;
        getServer().getPluginManager().registerEvents(new PlayerToggleRightClickEventDispatcher(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
