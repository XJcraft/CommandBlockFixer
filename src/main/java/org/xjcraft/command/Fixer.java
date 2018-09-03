package org.xjcraft.command;

import org.bukkit.block.Block;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Fixer extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof BlockCommandSender) {
            CommandBlock block = (CommandBlock) ((BlockCommandSender) sender).getBlock();
            String s = block.getCommand();
            if (!s.contains("minecraft:")) {
                String[] split = s.split(" ");
                if (split.length >= 5 && split[0].equalsIgnoreCase("tp")) {
                    if (split[1].length()<=8) {
                        String x = split[2];
                        String y = split[3];
                        String z = split[4];
                        block.setCommand(String.format("minecraft:tp @p[r=..5] %s %s %s",x,y,z));
                    }
                }
            }
        }
        return super.onCommand(sender, command, label, args);
    }

}
