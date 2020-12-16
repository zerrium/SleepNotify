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

/**
 *
 * @author willysusilo
 */
public class SleepListener implements Listener{
    private static int counter = 0;
    
    @EventHandler
    public void onPlayerSleep(PlayerBedEnterEvent event){
        int required = getRequired();
        long time = event.getPlayer().getWorld().getTime();
        if(event.isCancelled()) return;
        counter++;
        Bukkit.broadcastMessage(ChatColor.GOLD+"[SleepNotify] "+ChatColor.RESET+event.getPlayer().getName()+
                " is sleeping! "+ChatColor.AQUA+"["+counter+"/"+required+"]");
        if(counter == required){
            counter = 0;
            Bukkit.broadcastMessage(ChatColor.GOLD + "[SleepNotify] " + ChatColor.RESET + "Night has been skipped. It's morning!");
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
    public void onPlayerChangeDimension(PlayerChangedWorldEvent event){
        if(counter != 0){
            int required = getRequired();
            Player p = event.getPlayer();
            switch(p.getWorld().getEnvironment()){
                case NORMAL:
                    Bukkit.broadcastMessage(ChatColor.GOLD+"[SleepNotify] "+ChatColor.RESET+p.getName()+
                            " enters overworld "+ChatColor.AQUA+"["+counter+"/"+required+"]");
                    break;
                case NETHER:
                    Bukkit.broadcastMessage(ChatColor.GOLD+"[SleepNotify] "+ChatColor.RESET+p.getName()+
                            " enters nether "+ChatColor.AQUA+"["+counter+"/"+required+"]");
                    break;
                case THE_END:
                    Bukkit.broadcastMessage(ChatColor.GOLD+"[SleepNotify] "+ChatColor.RESET+p.getName()+
                            " enters the end "+ChatColor.AQUA+"["+counter+"/"+required+"]");
                    break;
                default:
                    break;
            }
            if(counter == required){
                counter = 0;
                Bukkit.broadcastMessage(ChatColor.GOLD + "[SleepNotify] " + ChatColor.RESET + "Night has been skipped. It's morning!");
            }
        }
    }
    
    @EventHandler
    public void onPlayerAfkToggle(AfkStatusChangeEvent event){
        if(counter != 0){
            int required = getRequired();
            Player p = event.getAffected().getBase();
            if(p.getWorld().getEnvironment().equals(Environment.NORMAL)){
                if(event.getValue()){ //goes AFK
                    if(p.isSleeping()) counter--;
                    Bukkit.broadcastMessage(ChatColor.GOLD+"[SleepNotify] "+ChatColor.RESET+p.getName()+
                                    " is AFK "+ChatColor.AQUA+"["+counter+"/"+(required-1)+"]");
                    if(counter == (required-1)){
                        counter = 0;
                        Bukkit.broadcastMessage(ChatColor.GOLD + "[SleepNotify] " + ChatColor.RESET + "Night has been skipped. It's morning!");
                    }
                }else{
                    if(p.isSleeping()) counter++;
                    Bukkit.broadcastMessage(ChatColor.GOLD+"[SleepNotify] "+ChatColor.RESET+p.getName()+
                                    " is no longer AFK "+ChatColor.AQUA+"["+counter+"/"+(required+1)+"]");
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
                                    " joined the game "+ChatColor.AQUA+"["+counter+"/"+getRequired()+"]");
            }
        }
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        if(counter != 0){
            int required = getRequired()-1;
            Player p = event.getPlayer();
            if(p.getWorld().getEnvironment().equals(Environment.NORMAL)){
                if(p.isSleeping()) counter--;
                Bukkit.broadcastMessage(ChatColor.GOLD+"[SleepNotify] "+ChatColor.RESET+p.getName()+
                                    " left the game "+ChatColor.AQUA+"["+counter+"/"+required+"]");
            }
            if(counter == required){
                counter = 0;
                Bukkit.broadcastMessage(ChatColor.GOLD + "[SleepNotify] " + ChatColor.RESET + "Night has been skipped. It's morning!");
            }
        }
    }
    
    @EventHandler
    public void onPlayerKick(PlayerKickEvent event){
        if(counter != 0){
            int required = getRequired()-1;
            Player p = event.getPlayer();
            if(p.getWorld().getEnvironment().equals(Environment.NORMAL)){
                if(p.isSleeping()) counter--;
                Bukkit.broadcastMessage(ChatColor.GOLD+"[SleepNotify] "+ChatColor.RESET+p.getName()+
                                    " is kicked from the game "+ChatColor.AQUA+"["+counter+"/"+required+"]");
            }
            if(counter == required){
                counter = 0;
                Bukkit.broadcastMessage(ChatColor.GOLD + "[SleepNotify] " + ChatColor.RESET + "Night has been skipped. It's morning!");
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
