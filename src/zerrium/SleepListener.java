package zerrium;

import com.earth2me.essentials.IEssentials;
import java.util.concurrent.atomic.AtomicInteger;
import net.ess3.api.events.AfkStatusChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.TimeSkipEvent;
import org.bukkit.event.world.TimeSkipEvent.SkipReason;

/**
 *
 * @author willysusilo
 */
public class SleepListener implements Listener{
    private static int counter = 0;
    
    @EventHandler
    public void onPlayerSleep(PlayerBedEnterEvent event){
        if(event.getBedEnterResult().equals(PlayerBedEnterEvent.BedEnterResult.OK)){
            counter++;
            Bukkit.broadcastMessage(ChatColor.GOLD+"[SleepNotify] "+ChatColor.RESET+event.getPlayer().getName()+
                    " is sleeping! "+ChatColor.AQUA+"["+counter+"/"+getRequired()+"]");
        }
    }
    
    @EventHandler
    public void onPlayerCancelSleep(PlayerBedLeaveEvent event){
        if(event.getPlayer().getWorld().getTime()!=0){
            counter--;
            Bukkit.broadcastMessage(ChatColor.GOLD+"[SleepNotify] "+ChatColor.RESET+event.getPlayer().getName()+
                    " cancels sleeping! "+ChatColor.AQUA+"["+counter+"/"+getRequired()+"]");
        }
    }
    
    @EventHandler
    public void onNightSkipped(TimeSkipEvent event){
        counter = 0;
        SkipReason sr = event.getSkipReason();
        if(sr.equals(SkipReason.NIGHT_SKIP)){ //skipped time by vanilla
            Bukkit.broadcastMessage(ChatColor.GOLD+"[SleepNotify] "+ChatColor.RESET+"Night has been skipped. It's morning!");
        }else if(sr.equals(SkipReason.CUSTOM) && event.getWorld().getTime()==0){ //skipped time by plugin
            Bukkit.broadcastMessage(ChatColor.GOLD+"[SleepNotify] "+ChatColor.RESET+"Night has been skipped. It's morning!");
        }
    }
    
    @EventHandler
    public void onPlayerChangeDimension(PlayerChangedWorldEvent event){
        if(counter != 0){
            Player p = event.getPlayer();
            switch(p.getWorld().getEnvironment()){
                case NORMAL:
                    Bukkit.broadcastMessage(ChatColor.GOLD+"[SleepNotify] "+ChatColor.RESET+p.getName()+
                            " enters overworld! "+ChatColor.AQUA+"["+counter+"/"+getRequired()+"]");
                    break;
                case NETHER:
                    Bukkit.broadcastMessage(ChatColor.GOLD+"[SleepNotify] "+ChatColor.RESET+p.getName()+
                            " enters nether! "+ChatColor.AQUA+"["+counter+"/"+getRequired()+"]");
                    break;
                case THE_END:
                    Bukkit.broadcastMessage(ChatColor.GOLD+"[SleepNotify] "+ChatColor.RESET+p.getName()+
                            " enters the end! "+ChatColor.AQUA+"["+counter+"/"+getRequired()+"]");
                    break;
            }
        }
    }
    
    @EventHandler
    public void onPlayerAfkToggle(AfkStatusChangeEvent event){
        if(counter != 0){
            Player p = event.getAffected().getBase();
            if(p.getWorld().getEnvironment().equals(Environment.NORMAL)){
                if(event.getValue()){ //goes AFK
                    if(p.isSleeping()) counter--;
                    Bukkit.broadcastMessage(ChatColor.GOLD+"[SleepNotify] "+ChatColor.RESET+p.getName()+
                                    " is AFK! "+ChatColor.AQUA+"["+counter+"/"+getRequired()+"]");
                }else{
                    if(p.isSleeping()) counter++;
                    Bukkit.broadcastMessage(ChatColor.GOLD+"[SleepNotify] "+ChatColor.RESET+p.getName()+
                                    " is no longer AFK! "+ChatColor.AQUA+"["+counter+"/"+getRequired()+"]");
                }
            }
        }
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        if(counter != 0){
            Player p = event.getPlayer();
            if(p.getWorld().getEnvironment().equals(Environment.NORMAL)){
                Bukkit.broadcastMessage(ChatColor.GOLD+"[SleepNotify] "+ChatColor.RESET+p.getName()+
                                    " joined the game! "+ChatColor.AQUA+"["+counter+"/"+getRequired()+"]");
            }
        }
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        if(counter != 0){
            Player p = event.getPlayer();
            if(p.getWorld().getEnvironment().equals(Environment.NORMAL)){
                if(p.isSleeping()) counter--;
                Bukkit.broadcastMessage(ChatColor.GOLD+"[SleepNotify] "+ChatColor.RESET+p.getName()+
                                    " left the game! "+ChatColor.AQUA+"["+counter+"/"+getRequired()+"]");
            }
        }
    }
    
    @EventHandler
    public void onPlayerKick(PlayerKickEvent event){
        if(counter != 0){
            Player p = event.getPlayer();
            if(p.getWorld().getEnvironment().equals(Environment.NORMAL)){
                if(p.isSleeping()) counter--;
                Bukkit.broadcastMessage(ChatColor.GOLD+"[SleepNotify] "+ChatColor.RESET+p.getName()+
                                    " is kicked from the game! "+ChatColor.AQUA+"["+counter+"/"+getRequired()+"]");
            }
        }
    }
    
    private static int getRequired(){
        AtomicInteger required = new AtomicInteger(0);
        Bukkit.getOnlinePlayers().forEach(i -> {
            if(i.getWorld().getEnvironment().equals(Environment.NORMAL)){ //check if the player is at overworld
                if(SleepNotify.hasEssentials){ //Check if the server installs essentials
                    IEssentials ess = (IEssentials) Bukkit.getPluginManager().getPlugin("Essentials");//see if he is AFK using essentials API
                    if(!ess.getUser(i).isAfk()){
                        required.getAndIncrement();
                    }
                }else{
                    required.getAndIncrement();
                }
            }
        });
        return required.intValue();
    }
}
