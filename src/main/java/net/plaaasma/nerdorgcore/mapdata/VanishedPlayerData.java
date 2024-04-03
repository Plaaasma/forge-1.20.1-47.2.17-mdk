package net.plaaasma.nerdorgcore.mapdata;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.Map;

public class VanishedPlayerData extends SavedData {
    private static final String DATA_NAME = "vanished_players";
    private final HashMap<String, Boolean> dataMap = new HashMap<>();

    @Override
    public CompoundTag save(CompoundTag pCompoundTag) {
        CompoundTag dataTag = new CompoundTag();

        for (Map.Entry<String, Boolean> entry : dataMap.entrySet()) {
            dataTag.putBoolean(entry.getKey(), entry.getValue());
        }
        pCompoundTag.put(DATA_NAME, dataTag);

        return pCompoundTag;
    }

    public HashMap<String, Boolean> getDataMap() {
        return dataMap;
    }

    public static VanishedPlayerData load(CompoundTag pCompoundTag) {
        VanishedPlayerData savedData = new VanishedPlayerData();
        CompoundTag dataTag = pCompoundTag.getCompound(DATA_NAME);
        for (String key : dataTag.getAllKeys()) {
            savedData.dataMap.put(key, dataTag.getBoolean(key));
        }
        return savedData;
    }

    public static VanishedPlayerData get(ServerLevel world) {
        return world.getDataStorage().computeIfAbsent(VanishedPlayerData::load, VanishedPlayerData::new, DATA_NAME);
    }
}
