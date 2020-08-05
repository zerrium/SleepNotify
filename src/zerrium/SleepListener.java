package zerrium;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.world.TimeSkipEvent;
import org.bukkit.event.world.TimeSkipEvent.SkipReason;

/**
 *
 * @author willysusilo
 */
public class SleepListener implements Listener{
    
    @EventHandler
    public void onPlayerSleep(PlayerBedEnterEvent event){
        if(event.getBedEnterResult().equals(PlayerBedEnterEvent.BedEnterResult.OK)){
            Bukkit.broadcastMessage(ChatColor.GOLD+"[SleepNotify] "+ChatColor.RESET+event.getPlayer().getName()+" is sleeping!");
        }
    }
    
    @EventHandler
    public void onNightSkipped(TimeSkipEvent event){
        SkipReason sr = event.getSkipReason();
        if(sr.equals(SkipReason.NIGHT_SKIP)){ //skipped time by vanilla
            Bukkit.broadcastMessage(ChatColor.GOLD+"[SleepNotify] "+ChatColor.RESET+"Night has been skipped. It's morning!");
        }else if(sr.equals(SkipReason.CUSTOM) && event.getWorld().getTime()==0){ //skipped time by plugin
            Bukkit.broadcastMessage(ChatColor.GOLD+"[SleepNotify] "+ChatColor.RESET+"Night has been skipped. It's morning!");
        }
    }
}
