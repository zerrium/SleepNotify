package zerrium;

import com.sun.istack.internal.NotNull;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CounterFix implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(strings[0].equals("fix")){
            SleepListener.counter = 0;
            commandSender.sendMessage(ChatColor.GOLD+"[SleepNotify] "+ChatColor.RESET+"Reset the sleep counter back to 0");
            return true;
        }else{
            commandSender.sendMessage(ChatColor.GOLD+"[SleepNotify] "+ChatColor.RESET+"Usage: /sleepnotify fix");
        }
        return false;
    }
}
