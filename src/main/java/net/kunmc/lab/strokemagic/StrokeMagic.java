package net.kunmc.lab.strokemagic;

import net.kunmc.lab.strokemagic.magic.JumpPad;
import net.kunmc.lab.strokemagic.magic.SkyWalker;
import org.bukkit.plugin.java.JavaPlugin;

public final class StrokeMagic extends JavaPlugin {
    private static StrokeMagic INSTANCE;
    private static Configuration config;
    private final MagicManager manager = MagicManager.getInstance();

    public static StrokeMagic getInstance() {
        return INSTANCE;
    }

    public static Configuration getConfiguration() {
        return config;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        INSTANCE = this;
        config = new Configuration(getConfig());
        manager.registerMagic(new SkyWalker());
        manager.registerMagic(new JumpPad());
        getServer().getPluginManager().registerEvents(new PlayerToggleRightClickEventDispatcher(), this);
        getServer().getPluginManager().registerEvents(new StrokeListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
