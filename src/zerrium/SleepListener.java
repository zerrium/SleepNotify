package zerrium;

import com.earth2me.essentials.IEssentials;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
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
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author willysusilo
 */
public class SleepListener implements Listener{
    protected static int counter = 0;
    
    @EventHandler
    public void onPlayerSleep(PlayerBedEnterEvent event){
        if(SleepNotify.version < 1){
            if(event.isCancelled()) return;
        }else{
            if(!event.getBedEnterResult().equals(PlayerBedEnterEvent.BedEnterResult.OK)) return;
        }
        int required = getRequired();
        counter++;
        Bukkit.broadcastMessage(ChatColor.GOLD+"[SleepNotify] "+ChatColor.RESET+event.getPlayer().getName()+
                " is sleeping! "+ChatColor.AQUA+"["+counter+"/"+required+"]");
        if(SleepNotify.version < 2 && counter == required) NightSkip();
    }
    
    @EventHandler
    public void onPlayerCancelSleep(PlayerBedLeaveEvent event){
        if(event.getPlayer().getWorld().getTime()!=0){
            if(SleepNotify.hasEssentials){
                IEssentials ess = (IEssentials) Bukkit.getPluginManager().getPlugin("Essentials");//see if he is AFK using essentials API
                assert ess != null;
                if(ess.getUser(event.getPlayer()).isAfk()) return;
            }
            counter--;
            Bukkit.broadcastMessage(ChatColor.GOLD+"[SleepNotify] "+ChatColor.RESET+event.getPlayer().getName()+
                    " cancels sleeping! "+ChatColor.AQUA+"["+counter+"/"+getRequired()+"]");
        }
    }
    
    @EventHandler
    public void onPlayerChangeDimension(PlayerChangedWorldEvent event){
        if(counter != 0){
            Player p = event.getPlayer();
            int required = getRequired();
            switch(p.getWorld().getEnvironment()){
                case NORMAL:
                    Bukkit.broadcastMessage(ChatColor.GOLD+"[SleepNotify] "+ChatColor.RESET+p.getName()+
                            " enters overworld "+ChatColor.AQUA+"["+counter+"/"+(required+=1)+"]");
                    break;
                case NETHER:
                    Bukkit.broadcastMessage(ChatColor.GOLD+"[SleepNotify] "+ChatColor.RESET+p.getName()+
                            " enters nether "+ChatColor.AQUA+"["+counter+"/"+(required-=1)+"]");
                    break;
                case THE_END:
                    Bukkit.broadcastMessage(ChatColor.GOLD+"[SleepNotify] "+ChatColor.RESET+p.getName()+
                            " enters the end "+ChatColor.AQUA+"["+counter+"/"+(required-=1)+"]");
                    break;
                default:
                    break;
            }
            if(SleepNotify.version < 2 && counter == required) NightSkip();
        }
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        if(counter != 0){
            Player p = event.getPlayer();
            if(p.getWorld().getEnvironment().equals(Environment.NORMAL)){
                Bukkit.broadcastMessage(ChatColor.GOLD+"[SleepNotify] "+ChatColor.RESET+p.getName()+
                                    " joined the game "+ChatColor.AQUA+"["+counter+"/"+(getRequired()+1)+"]");
            }
        }
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        if(counter != 0){
            Player p = event.getPlayer();
            int required = getRequired();
            if(p.getWorld().getEnvironment().equals(Environment.NORMAL)){
                if(p.isSleeping()) counter--;
                Bukkit.broadcastMessage(ChatColor.GOLD+"[SleepNotify] "+ChatColor.RESET+p.getName()+
                                    " left the game "+ChatColor.AQUA+"["+counter+"/"+(required-=1)+"]");
            }
            if(SleepNotify.version < 2 && counter == required) NightSkip();
        }
    }
    
    @EventHandler
    public void onPlayerKick(PlayerKickEvent event){
        if(counter != 0){
            Player p = event.getPlayer();
            int required = getRequired();
            if(p.getWorld().getEnvironment().equals(Environment.NORMAL)){
                if(p.isSleeping()) counter--;
                Bukkit.broadcastMessage(ChatColor.GOLD+"[SleepNotify] "+ChatColor.RESET+p.getName()+
                                    " is kicked from the game "+ChatColor.AQUA+"["+counter+"/"+(required-=1)+"]");
            }
            if(SleepNotify.version < 2 && counter == required) NightSkip();
        }
    }
    
    protected static int getRequired(){
        AtomicInteger required = new AtomicInteger(0);
        Bukkit.getOnlinePlayers().forEach(i -> {
            if(i.getWorld().getEnvironment().equals(Environment.NORMAL)){ //check if the player is at overworld
                if(SleepNotify.hasEssentials){ //Check if the server installs essentials
                    IEssentials ess = (IEssentials) Bukkit.getPluginManager().getPlugin("Essentials");//see if he is AFK using essentials API
                    assert ess != null;
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

    protected static void NightSkip(){
        BukkitRunnable r = new BukkitRunnable() {
            @Override
            public void run() {
                while(Objects.requireNonNull(Bukkit.getWorld("world")).getTime() !=0 && (Objects.requireNonNull(Bukkit.getWorld("world")).getTime() > 12542 || (Objects.requireNonNull(Bukkit.getWorld("world")).hasStorm() && Objects.requireNonNull(Bukkit.getWorld("world")).isThundering()))){
                    if(Objects.requireNonNull(Bukkit.getWorld("world")).getTime() < 1000 || Objects.requireNonNull(Bukkit.getWorld("world")).getTime() > 23460) break;
                }
                Bukkit.broadcastMessage(ChatColor.GOLD+"[SleepNotify] "+ChatColor.RESET+"Night has been skipped. It's morning!");
            }
        };
        r.runTaskAsynchronously(SleepNotify.getPlugin(SleepNotify.class));
    }
}
