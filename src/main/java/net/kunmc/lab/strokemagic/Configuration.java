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

    public int getMaxStroke() {
        return config.getInt("MaxStroke");
    }

    public Material getRodMaterial() {
        return Material.valueOf(config.getString("RodMaterial"));
    }

    public int getYawInputBorderDegree() {
        return config.getInt("YawInputBorderDegree");
    }

    private void set(String key, Object obj) {
        config.set(key, obj);
        StrokeMagic.getInstance().saveConfig();
    }

    public void setYawInputBorderDegree(int degree) {
        set("YawInputBorderDegree", degree);
    }

    public int getPitchInputBorderDegree() {
        return config.getInt("PitchInputBorderDegree");
    }

    public void setPitchInputBorderDegree(int degree) {
        set("PitchInputBorderDegree", degree);
    }

    public int getRightClickHoldOffDelay() {
        return config.getInt("RightClickHoldOffDelay");
    }

    public void setRightClickHoldOffDelay(int tick) {
        set("RightClickHoldOffDelay", tick);
    }

    public Map<String, String> getMagicConfig(String magicName) {
        MemorySection section = ((MemorySection) config.get("Magics." + magicName));
        if (section == null) return null;
        Map<String, String> settings = new HashMap<>();
        for (String key : section.getKeys(false)) {
            settings.put(key, section.getString(key));
        }
        return settings;
    }
}
