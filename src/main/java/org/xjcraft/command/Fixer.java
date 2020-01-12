package org.xjcraft.command;


import net.minecraft.server.v1_15_R1.BlockPosition;
import net.minecraft.server.v1_15_R1.TileEntity;
import net.minecraft.server.v1_15_R1.TileEntityCommand;
import net.minecraft.server.v1_15_R1.WorldServer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_15_R1.block.CraftCommandBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.player.PlayerInteractEvent;
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
            modifyCommand(block);
        }
        return super.onCommand(sender, command, label, args);
    }

    private String modifyCommand(CommandBlock block) {
        String s = block.getCommand();
        if (!s.contains("minecraft:")) {
            String[] split = s.split(" ");
            if (split.length >= 5 && (split[0].contains("tp"))) {
                if (split[1].length() <= 8) {
                    String x = split[2];
                    String y = split[3];
                    String z = split[4];
                    World world = getServer().getWorld("MainLand");
                    try {
                        Block at = getSafeBlock(world.getBlockAt(Integer.parseInt(x), Integer.parseInt(y), Integer.parseInt(z)));
//                        world.getHighestBlockAt(Integer.parseInt(x), Integer.parseInt(z));
                        y = String.valueOf(at.getY());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    s = String.format("minecraft:tp @p[distance=..5] %s %s %s", x, y, z);
                }
            }
        }
        return s;
    }

    private Block getSafeBlock(Block block) {
        if (block.getRelative(0, 1, 0).isPassable() && block.getRelative(0, 2, 0).isPassable())
            return block;
        return getSafeBlock(block.getRelative(0, 1, 0));
    }

    @EventHandler
    public void press(PlayerInteractEvent event) {
        Block clickedBlock = event.getClickedBlock();
    }

    @EventHandler
    public void press(BlockRedstoneEvent event) {
        if (event.getOldCurrent() >= event.getNewCurrent()) return;
        Block clickedBlock = event.getBlock();
        if (clickedBlock.getType().data == org.bukkit.block.data.type.CommandBlock.class) {
            CraftCommandBlock block = (CraftCommandBlock) clickedBlock.getState();
            String s = modifyCommand(block);
            block.setCommand(s);
            CraftWorld world = (CraftWorld) clickedBlock.getWorld();
            WorldServer handle = world.getHandle();
            TileEntity tileEntity = handle.getTileEntity(new BlockPosition(block.getX(), block.getY(), block.getZ()));
            ((TileEntityCommand) tileEntity).getCommandBlock().setCommand(s);

        }
    }

}
