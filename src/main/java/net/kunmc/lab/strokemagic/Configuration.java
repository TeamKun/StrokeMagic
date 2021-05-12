package net.kunmc.lab.strokemagic;

import org.bukkit.Material;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

public class Configuration {
    private final FileConfiguration config;

    Configuration(FileConfiguration config) {
        this.config = config;
    }

    public Material getRodMaterial() {
        return Material.valueOf(config.getString("RodMaterial"));
    }

    public int getYawInputBorderDegree() {
        return config.getInt("YawInputBorderDegree");
    }

    public int getPitchInputBorderDegree() {
        return config.getInt("PitchInputBorderDegree");
    }

    public int getRightClickHoldOffDelay() {
        return config.getInt("RightClickHoldOffDelay");
    }

    public Map<String, String> getJumpPadConfig() {
        MemorySection section = ((MemorySection) config.get("Magics.JumpPad"));
        if (section == null) return null;
        Map<String, String> settings = new HashMap<>();
        for (String key : section.getKeys(false)) {
            settings.put(key, section.getString(key));
        }
        return settings;
    }

    public Map<String, String> getSkyWalkerConfig() {
        MemorySection section = ((MemorySection) config.get("Magics.SkyWalker"));
        if (section == null) return null;
        Map<String, String> settings = new HashMap<>();
        for (String key : section.getKeys(false)) {
            settings.put(key, section.getString(key));
        }
        return settings;
    }
}
