package zerrium;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author willysusilo
 */
public class SleepNotify extends JavaPlugin{
    public static boolean hasEssentials = false;
    
    @Override
    public void onEnable() {
        System.out.println(ChatColor.YELLOW+"[SleepNotify] v0.2 by zerrium");
        getServer().getPluginManager().registerEvents(new SleepListener(), this);
        this.getCommand("t").setExecutor(new WhatTime());
        if(Bukkit.getPluginManager().getPlugin("Essentials") != null || Bukkit.getPluginManager().getPlugin("EssentialsX") != null){
            System.out.println(ChatColor.YELLOW+"[SleepNotify] Essentials plugin detected. AFK detection for sleep notification is hooked.");
            hasEssentials = true;
        }else{
            System.out.println(ChatColor.YELLOW+"[SleepNotify] No Essentials plugin detected. Disabled AFK detection for sleep notification");
        }
    }

    @Override
    public void onDisable() {
        System.out.println(ChatColor.YELLOW+"[SleepNotify] v0.2 disabling plugin");
    }
}
