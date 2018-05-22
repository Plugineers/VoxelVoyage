package com.thevoxelbox.voyage;

import com.thevoxelbox.voyage.entities.Crystal;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityListener;


public class VEntity extends EntityListener {
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if ((((CraftEntity) event.getEntity()).getHandle() instanceof PrzlabsEntity)) {
            VoxelVoyage.log.info("[VoxelVoyage] Spawning VoyageEntity ID " + event.getEntity().getEntityId());
            event.setCancelled(false);
        } else if ((((CraftEntity) event.getEntity()).getHandle() instanceof Crystal)) {
            event.setCancelled(false);
        }
        else if (!VoxelVoyage.SPAWN_ENTITIES) {
            event.setCancelled(true);
        }
    }
}
