package zerrium;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

/**
 *
 * @author willysusilo
 */
public class SleepNotify extends JavaPlugin{
    public static boolean hasEssentials = false;
    public static int version;
    
    @Override
    public void onEnable() {
        System.out.println(ChatColor.YELLOW+"[SleepNotify] v1.0 by zerrium");
        version = getVersion();
        getServer().getPluginManager().registerEvents(new SleepListener(), this);
        if(version > 1) getServer().getPluginManager().registerEvents(new NightSkipListener(), this);
        Objects.requireNonNull(this.getCommand("t")).setExecutor(new WhatTime());
        if(Bukkit.getPluginManager().getPlugin("Essentials") != null || Bukkit.getPluginManager().getPlugin("EssentialsX") != null){
            System.out.println(ChatColor.YELLOW+"[SleepNotify] Essentials plugin detected. AFK detection for sleep notification is hooked.");
            getServer().getPluginManager().registerEvents(new AfkListener(), this);
            hasEssentials = true;
        }else{
            System.out.println(ChatColor.YELLOW+"[SleepNotify] No Essentials plugin detected. Disabled AFK detection for sleep notification");
        }
    }

    @Override
    public void onDisable() {
        System.out.println(ChatColor.YELLOW+"[SleepNotify] v0.4 disabling plugin");
    }

    protected static int getVersion(){
        String v = Bukkit.getServer().getVersion();
        if (v.contains("1.8")) return 0;
        else if (v.contains("1.9")) return 0;
        else if (v.contains("1.10")) return 0;
        else if (v.contains("1.11")) return 0;
        else if (v.contains("1.12")) return 0;

        //BedEnterResult
        else if (v.contains("1.13")) return 1;
        else if (v.contains("1.14")) return 1;

        //TimeSkipEvent
        else if (v.contains("1.15")) return 2;
        else if (v.contains("1.16")) return 2;
        else return 2;
    }
}
