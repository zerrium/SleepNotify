package zerrium;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.TimeSkipEvent;
import org.bukkit.event.world.TimeSkipEvent.SkipReason;

public class NightSkipListener implements Listener { //1.15+ only
    @EventHandler
    public void onNightSkipped(TimeSkipEvent event){
        SleepListener.counter = 0;
        SkipReason sr = event.getSkipReason();
        if(sr.equals(SkipReason.NIGHT_SKIP)){ //skipped time by vanilla
            Bukkit.broadcastMessage(ChatColor.GOLD+"[SleepNotify] "+ChatColor.RESET+"Night has been skipped. It's morning!");
        }else if(sr.equals(SkipReason.CUSTOM) && event.getWorld().getTime()==0){ //skipped time by plugin
            Bukkit.broadcastMessage(ChatColor.GOLD+"[SleepNotify] "+ChatColor.RESET+"Night has been skipped. It's morning!");
        }
    }
}
