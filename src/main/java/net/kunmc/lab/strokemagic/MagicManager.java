package net.kunmc.lab.strokemagic;

import net.kunmc.lab.strokemagic.magic.Magic;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MagicManager {
    private final Map<String, Magic> strokeToMagicMap = new HashMap<>();
    private static final MagicManager singleton = new MagicManager();

    public static MagicManager getInstance() {
        return singleton;
    }

    private MagicManager() {
    }

    public boolean registerMagic(@NotNull Magic magic) {
        return strokeToMagicMap.putIfAbsent(magic.getStroke(), magic) == null;
    }

    public void forceRegisterMagic(@NotNull Magic magic) {
        strokeToMagicMap.put(magic.getStroke(), magic);
    }

    public @Nullable Magic getMagic(@NotNull String stroke) {
        return strokeToMagicMap.get(stroke);
    }

    public @NotNull Collection<Magic> getRegisteredMagics() {
        return strokeToMagicMap.values();
    }
}
