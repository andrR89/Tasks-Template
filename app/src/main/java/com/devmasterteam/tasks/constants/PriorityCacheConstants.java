package com.devmasterteam.tasks.constants;

import com.devmasterteam.tasks.entities.PriorityEntity;

import java.util.HashMap;
import java.util.List;

public class PriorityCacheConstants {

    private static HashMap<Integer, String> PRIORITY_CACHE = new HashMap<>();

    private PriorityCacheConstants(){

    }

    public static void setValues(List<PriorityEntity> values){
        for(PriorityEntity entity:values){
            PRIORITY_CACHE.put(entity.getId(), entity.getDescription());
        }
    }


    public static String get(int priorityId) {
        return PRIORITY_CACHE.get(priorityId);
    }
}
