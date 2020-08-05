package zerrium;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author willysusilo
 */
public class SleepNotify extends JavaPlugin{
    @Override
    public void onEnable() {
        System.out.println(ChatColor.YELLOW+"[SleepNotify] v0.1 by zerrium");
        getServer().getPluginManager().registerEvents(new SleepListener(), this);
        this.getCommand("t").setExecutor(new WhatTime());
    }

    @Override
    public void onDisable() {
        System.out.println(ChatColor.YELLOW+"[SleepNotify] v0.1 disabling plugin");
    }
}
