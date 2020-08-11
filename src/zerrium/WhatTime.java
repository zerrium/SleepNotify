package zerrium;

import org.bukkit.ChatColor;
import org.bukkit.World.Environment;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author willysusilo
 */
public class WhatTime implements CommandExecutor{

    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
        if(cs instanceof Player){
            Player player = (Player) cs;
            String dim = "";
            Environment en = player.getWorld().getEnvironment();
            double time = player.getWorld().getTime();
            switch(en){
                case NETHER:
                    dim = "the nether";
                    break;
                
                case NORMAL:
                    dim = "the overworld";
                    break;
                    
                case THE_END:
                    dim = "the end";
                    break;
                default:
                    break;
            }
            int hours = (int) ((Math.floor(time / 1000.0) + 6) % 24);
            int minutes = (int) Math.floor((time % 1000) / 1000.0 * 60);
            String tif = String.format("%02d:%02d", hours, minutes);
            player.sendMessage(ChatColor.GOLD+"[Zerrium Server]"+ChatColor.RESET+" It's "+tif+" in "+dim);
            return true;
        }else{
            System.out.println(ChatColor.GOLD+"[SleepNotify] "+ChatColor.RED+"Error: you must be a player to execute the command!");
        }
        return false;
    }
}
