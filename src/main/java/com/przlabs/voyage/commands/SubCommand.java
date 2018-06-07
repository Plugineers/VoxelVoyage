package com.przlabs.voyage.commands;
import com.przlabs.voyage.application.VoxelVoyage;
import com.przlabs.voyage.entity.PrzlabsCrystal;
import com.przlabs.voyage.entity.PrzlabsDragon;
import com.przlabs.voyage.entity.PrzlabsEntity;
import net.minecraft.server.v1_12_R1.Entity;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;

import static com.przlabs.voyage.application.VoxelVoyage.getNearestEntity;


public enum SubCommand {
    CREATE("create", "voxelvoyage.create") {
        @Override
        public boolean executeCommand(Player player, Command command, String s, String[] args) {
            double speed = 3;
            PrzlabsDragon dragon = null;
            if (args.length == 1) {
                try {
                    speed = Double.valueOf(args[0]);
                } catch (NumberFormatException e) {
                    player.sendMessage(ChatColor.RED + "Invalid format.");
                    return true;
                }
            }
            dragon = new PrzlabsDragon(((CraftWorld) player.getWorld()).getHandle(), true, player.getLocation(), speed);

            if (((CraftWorld) player.getWorld()).getHandle().addEntity(dragon, CreatureSpawnEvent.SpawnReason.CUSTOM)) {
                player.sendMessage(ChatColor.GREEN + "Done!");
            } else {
                player.sendMessage(ChatColor.RED + "Failure :(");
            }
            return true;
        }
    },
    CRYSTAL("crystal", "voxelvoyage.crystal") {
        @Override
        public boolean executeCommand(Player player, Command command, String s, String[] args) {
            PrzlabsCrystal crystal = new PrzlabsCrystal(((CraftWorld) player.getWorld()).getHandle(), true);
            crystal.setPosition(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
            if (((CraftWorld) player.getWorld()).getHandle().addEntity(crystal, CreatureSpawnEvent.SpawnReason.CUSTOM)) {
                player.sendMessage(ChatColor.GREEN + "Done!");
            } else {
                player.sendMessage(ChatColor.RED + "Failure :(");
            }
            return true;
        }
    },
    SHINYBALLOON("shinyballoon", "voxelvoyage.shinyballoon") {
        @Override
        public boolean executeCommand(Player player, Command command, String s, String[] args) {
            PrzlabsCrystal crystal = new PrzlabsCrystal(((CraftWorld) player.getWorld()).getHandle(), true);
            crystal.setPosition(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
            if (((CraftWorld) player.getWorld()).getHandle().addEntity(crystal, CreatureSpawnEvent.SpawnReason.CUSTOM)) {
                player.sendMessage(ChatColor.GREEN + "Done!");
            } else {
                player.sendMessage(ChatColor.RED + "Failure :(");
            }
            return true;
        }
    },
    CTRL("ctrl", "voxelvoyage.ctrl") {
        @Override
        public boolean executeCommand(Player player, Command command, String s, String[] args) {
            performRightClick(player, 1);
            return true;
        }
    },
    CTRLROT("ctrlrot", "voxelvoyage.ctrlrot") {
        @Override
        public boolean executeCommand(Player player, Command command, String s, String[] args) {
            performRightClick(player, 1);
            return true;
        }
    },
    CTRLPOS("ctrlpos", "voxelvoyage.ctrlpos") {
        @Override
        public boolean executeCommand(Player player, Command command, String s, String[] args) {
            performRightClick(player, 8);
            return true;
        }
    },
    SELECT("select", "voxelvoyage.select") {
        @Override
        public boolean executeCommand(Player player, Command command, String s, String[] args) {
            Entity focus = getNearestEntity(player);
            if (focus != null) {
                VoxelVoyage.selected.put(player.getName(), focus);
                player.sendMessage(ChatColor.GOLD + "VoyageEntity selected");
            }
            return true;
        }
    },
    ADD("add", "voxelvoyage.add") {
        @Override
        public boolean executeCommand(Player player, Command command, String s, String[] args) {
            if (!VoxelVoyage.selected.containsKey(player.getName())) {
                player.sendMessage(ChatColor.RED + "Please select a dragon first!");
            } else {

                rightClickSelected(player, 20);
                player.sendMessage(ChatColor.GRAY + "Point added");
            }
            return true;
        }
    },
    KILL("kill", "voxelvoyage.kill") {
        @Override
        public boolean executeCommand(Player player, Command command, String s, String[] args) {
            if (!VoxelVoyage.selected.containsKey(player.getName())) {
                player.sendMessage(ChatColor.RED + "Please select a dragon first!");
            } else {
                rightClickSelected(player, 5);
                rightClickSelected(player, 21);
                player.sendMessage(ChatColor.GRAY + "Entity removed");
            }
            return false;
        }
    },
    EDITPATH("editpath", "voxelvoyage.editpath") {
        @Override
        public boolean executeCommand(Player player, Command command, String s, String[] args) {
            if (!VoxelVoyage.selected.containsKey(player.getName())) {
                player.sendMessage(ChatColor.RED + "Please select a dragon first!");
            } else {
                rightClickSelected(player, 4);
                player.sendMessage(ChatColor.GRAY + "Path ready");
            }
            return true;
        }
    },
    CLEANPATH("cleanpath", "voxelvoyage.cleanpath") {
        @Override
        public boolean executeCommand(Player player, Command command, String s, String[] args) {
            if (!VoxelVoyage.selected.containsKey(player.getName())) {
                player.sendMessage(ChatColor.RED + "Please select a dragon first!");
            } else {
                rightClickSelected(player, 5);
                player.sendMessage(ChatColor.GRAY + "Path ready");
            }
            return true;
        }
    },
    DEMO("demo", "voxelvoyage.demo") {
        @Override
        public boolean executeCommand(Player player, Command command, String s, String[] args) {
            if (!VoxelVoyage.selected.containsKey(player.getName())) {
                player.sendMessage(ChatColor.RED + "Please select a dragon first!");
            } else {
                rightClickSelected(player, 6);
            }
            return true;
        }
    },
    LOADBACKUP("loadbackup", "voxelvoyage.loadbackup") {
        @Override
        public boolean executeCommand(Player player, Command command, String s, String[] args) {
//            if (args.length == 2) {
//                World world = Bukkit.getWorld(args[1]);
//                if (world != null) {
//                    player.sendMessage(ChatColor.LIGHT_PURPLE + "Beginning...");
//                    VoyageData.loadVoyagersFromBackup(((CraftWorld) world).getHandle());
//                    player.sendMessage(ChatColor.DARK_PURPLE + "Finished!");
//                } else {
//                    player.sendMessage(ChatColor.RED + "No world found by the name \"" + args[1] + "\"");
//                }
//            } else {
//                player.sendMessage(ChatColor.LIGHT_PURPLE + "Beginning...");
//                VoyageData.loadVoyagersFromBackup(null);
//                player.sendMessage(ChatColor.DARK_PURPLE + "Finished!");
//            }
            return true;
        }
    }, HELP("help", "") {
        @Override
        public boolean executeCommand(Player player, Command command, String s, String[] args) {
            return false;
        }
    };

    private String name;
    private String permission;

    SubCommand(String enumCommandName, String permission) {
        this.name = enumCommandName;
        this.permission = permission;
    }

    public static void execute(String name, Player player, Command command, String s, String[] args) {
        for (SubCommand enumCommand : values()) {
            if (enumCommand.name.equalsIgnoreCase(name)) {
                if (player.hasPermission(enumCommand.permission)) {
                    enumCommand.executeCommand(player, command, s, args);
                }
            }
        }
    }


    private static void performRightClick(Player player, int action) {
        Entity entity = getNearestEntity(player);

        rightClickEntity(player, action, entity);
    }

    private static void rightClickEntity(Player player, int action, Entity entity) {
        if (entity != null && entity instanceof PrzlabsEntity) {
            ((PrzlabsEntity) entity).rightClick(player, action);
        }
    }

    private static void rightClickSelected(Player player, int action) {
        Entity entity = VoxelVoyage.selected.get(player.getName());

        rightClickEntity(player, action, entity);
    }

    public abstract boolean executeCommand(Player player, Command command, String s, String[] args);
}

