package net.kunmc.lab.strokemagic;

import net.kunmc.lab.strokemagic.command.CommandHandler;
import net.kunmc.lab.strokemagic.magic.*;
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
        manager.registerMagic(new Fireball());
        manager.registerMagic(new Heal());
        manager.registerMagic(new Depulso());
        getServer().getPluginManager().registerEvents(new PlayerToggleRightClickEventDispatcher(), this);
        getServer().getPluginManager().registerEvents(new StrokeListener(), this);

        getServer().getPluginCommand("strokemagic").setExecutor(new CommandHandler());
        getServer().getPluginCommand("strokemagic").setTabCompleter(new CommandHandler());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
