package net.kunmc.lab.strokemagic;

import net.kunmc.lab.strokemagic.magic.JumpPad;
import net.kunmc.lab.strokemagic.magic.SkyWalker;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

public final class StrokeMagic extends JavaPlugin {
    public static final Material RodMaterial = Material.BLAZE_ROD;
    private static StrokeMagic INSTANCE;
    private final MagicManager manager = MagicManager.getInstance();

    public static StrokeMagic getInstance() {
        return INSTANCE;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        INSTANCE = this;
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
