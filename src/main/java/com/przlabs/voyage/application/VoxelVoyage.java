package com.przlabs.voyage.application;

import com.przlabs.voyage.entity.PrzlabsCrystal;
import com.przlabs.voyage.entity.PrzlabsDragon;
import com.przlabs.voyage.entity.PrzlabsEntity;
import com.przlabs.voyage.entity.PrzlabsRedBalloon;
import com.przlabs.voyage.listener.EntityListener;
import com.przlabs.voyage.listener.PlayerInteractListener;
import net.minecraft.server.v1_12_R1.Entity;
import net.minecraft.server.v1_12_R1.EntityTypes;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VoxelVoyage extends JavaPlugin {
    private static final Logger VOYAGE_LOGGER = Logger.getLogger(VoxelVoyage.class.getName());

    public static final Logger LOGGER = Logger.getLogger("Minecraft");
    public static TreeMap<UUID, TreeMap<UUID, Entity>> VOYAGE_ENTITIES = new TreeMap<>();
    public static TreeMap<String, Entity> selected = new TreeMap<>();
    public static TreeSet<String> flying = new TreeSet<>();
    public static TreeSet<String> permitted = new TreeSet<>();
    public static String password;
    public static int voyageItem = 371;
    public static boolean forceSpawning = false;

    static {
        registerCustomEntitiesWithServer();
    }

    public static Entity getNearestEntity(Player p) {
        if (!VOYAGE_ENTITIES.containsKey(p.getWorld().getUID())) {
            p.sendMessage(ChatColor.RED + "No Voyage entity found.");
            return null;
        }
        Entity closest = null;
        double range = 99999999;

        double bx = p.getLocation().getX();
        double by = p.getLocation().getY();
        double bz = p.getLocation().getZ();

        for (Entity ent : VOYAGE_ENTITIES.get(p.getWorld().getUID()).values()) {
            switch (ent.getAirTicks()) {
                case 12347:
                case 12348:
                case 12349:
                case 12350:
                    double erange = Math.pow(bx - ent.locX, 2) + Math.pow(by - ent.locY, 2) + Math.pow(bz - ent.locZ, 2);

                    if (ent.dead) {
                        continue;
                    }

                    if (erange < range) {
                        range = erange;
                        closest = ent;
                    }
                    break;

                default:
                    break;
            }
        }

        if (closest != null && Math.pow(range, 0.5) <= 40) {
            return closest;
        } else {
            p.sendMessage(ChatColor.RED + "No Voyage entity found.");
            return null;
        }
    }

    public static boolean isPermitted(Player user) {
        return (user.isOp() || permitted.contains(user.getName()));
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void onEnable() {
        loadProps();

        for (World world : Bukkit.getWorlds()) {
            CraftWorld craftWorld = (CraftWorld) world;
            for (Entity entity : craftWorld.getHandle().entityList) {
                switch (entity.getAirTicks()) {
                    case 12346:
                        entity.die();
                        break;

                    case 12347:
                    case 12348:
                    case 12349:
                    case 12350:
                        if (!VOYAGE_ENTITIES.containsKey(world.getUID())) {
                            VOYAGE_ENTITIES.put(world.getUID(), new TreeMap<>());
                        }

                        VOYAGE_ENTITIES.get(world.getUID()).put(entity.getUniqueID(), entity);
                        break;

                    default:
                        break;
                }
            }
        }

        Bukkit.getPluginManager().registerEvents(new PlayerInteractListener(), this);
        Bukkit.getPluginManager().registerEvents(new EntityListener(), this);

        PluginDescriptionFile pdfFile = this.getDescription();
        LOGGER.info(pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled! Let's fly.");
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void loadProps() {
        File f = new File("plugins/VoxelVoyage/voyage.properties");
        if (!f.exists()) {
            f.getParentFile().mkdirs();
            try {
                f.createNewFile();
                saveProps();
            } catch (IOException ex) {
                VOYAGE_LOGGER.log(Level.SEVERE, null, ex);
            }
        }
        Properties prop = new Properties();
        try {
            prop.load(new FileReader(f));
        } catch (IOException ex) {
            VOYAGE_LOGGER.log(Level.SEVERE, null, ex);
        }
        password = prop.getProperty("Password", null);
        if (password != null && password.equalsIgnoreCase("null")) {
            password = null;
        }
        voyageItem = Integer.parseInt(prop.getProperty("VoyageItem", "371"));
        forceSpawning = Boolean.parseBoolean(prop.getProperty("ForceSpawning"));
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void saveProps() {
        File f = new File("plugins/VoxelVoyage/voyage.properties");
        if (!f.exists()) {
            f.getParentFile().mkdirs();
            try {
                f.createNewFile();
            } catch (IOException ex) {
                VOYAGE_LOGGER.log(Level.SEVERE, null, ex);
            }
        }
        Properties prop = new Properties();
        prop.setProperty("Password", (password == null ? "null" : password));
        prop.setProperty("VoyageItem", String.valueOf(voyageItem));
        prop.setProperty("ForceSpawning", Boolean.toString(forceSpawning));
        try {
            prop.store(new PrintWriter(f), null);
        } catch (IOException ex) {
            VOYAGE_LOGGER.log(Level.SEVERE, null, ex);
        }
    }


    private static void registerCustomEntitiesWithServer() {
        try {
            EntityTypes et = EntityTypes.class.getConstructor().newInstance();
            Method addEntity = EntityTypes.class.getDeclaredMethod("a", int.class, String.class, Class.class, String.class);
            addEntity.setAccessible(true);

            registerCustomEntity(et, addEntity, 63, "przlabs_dragon", PrzlabsDragon.class, "PrzlabsDragon");
            registerCustomEntity(et, addEntity, 200, "przlabs_crystal", PrzlabsCrystal.class, "PrzlabsCrystal");
            registerCustomEntity(et, addEntity, 200, "przlabs_red_balloon", PrzlabsRedBalloon.class, "PrzlabsRedBalloon");
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | InvocationTargetException | IllegalArgumentException ex) {
            VOYAGE_LOGGER.log(Level.SEVERE, "[VoxelVoyage] PrzlabsDragon entity failed to register!", ex);
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    private static void registerCustomEntity(
            EntityTypes entityTypes,
            Method addEntity,
            int entityId,
            String entityKey,
            Class<? extends PrzlabsEntity> entityClass,
            String entityName) throws IllegalAccessException, InvocationTargetException {
        addEntity.invoke(entityTypes, entityId, entityKey, entityClass, entityName);

        VOYAGE_LOGGER.log(Level.INFO, "[VoxelVoyage] {0} entity registered!", entityName);
    }
}
