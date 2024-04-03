package net.plaaasma.nerdorgcore.mapdata;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;

public class HomeData extends SavedData {
    private static final String DATA_NAME = "player_homes";
    private final HashMap<String, CompoundTag> dataMap = new HashMap<>();

    @Override
    public CompoundTag save(CompoundTag pCompoundTag) {
        CompoundTag dataTag = new CompoundTag();

        for (Map.Entry<String, CompoundTag> entry : dataMap.entrySet()) {
            dataTag.put(entry.getKey(), entry.getValue());
        }
        pCompoundTag.put(DATA_NAME, dataTag);

        return pCompoundTag;
    }

    public HashMap<String, CompoundTag> getDataMap() {
        return dataMap;
    }

    public static HomeData load(CompoundTag pCompoundTag) {
        HomeData savedData = new HomeData();
        CompoundTag dataTag = pCompoundTag.getCompound(DATA_NAME);
        for (String key : dataTag.getAllKeys()) {
            savedData.dataMap.put(key, dataTag.getCompound(key));
        }
        return savedData;
    }

    public static HomeData get(ServerLevel world) {
        return world.getDataStorage().computeIfAbsent(HomeData::load, HomeData::new, DATA_NAME);
    }
}
