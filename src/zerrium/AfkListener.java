package zerrium;

import net.ess3.api.events.AfkStatusChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class AfkListener implements Listener {
    @EventHandler
    public void onPlayerAfkToggle(AfkStatusChangeEvent event){
        if(SleepListener.counter != 0){
            Player p = event.getAffected().getBase();
            if(p.getWorld().getEnvironment().equals(World.Environment.NORMAL)){
                int required = SleepListener.getRequired();
                if(event.getValue()){ //goes AFK
                    if(p.isSleeping()) SleepListener.counter--;
                    Bukkit.broadcastMessage(ChatColor.GOLD+"[SleepNotify] "+ChatColor.RESET+p.getName()+
                            " is AFK "+ChatColor.AQUA+"["+SleepListener.counter+"/"+(required-=1)+"]");
                }else{
                    if(p.isSleeping()) SleepListener.counter++;
                    Bukkit.broadcastMessage(ChatColor.GOLD+"[SleepNotify] "+ChatColor.RESET+p.getName()+
                            " is no longer AFK "+ChatColor.AQUA+"["+SleepListener.counter+"/"+(required+=1)+"]");
                }
                if(SleepNotify.version < 2 && SleepListener.counter == required) SleepListener.NightSkip();
            }
        }
    }
}
