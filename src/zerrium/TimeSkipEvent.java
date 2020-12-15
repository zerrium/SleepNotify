package zerrium;

import com.sun.istack.internal.NotNull;
import org.bukkit.World;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.world.WorldEvent;

public class TimeSkipEvent extends WorldEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private long skipAmount;

    public TimeSkipEvent(@NotNull World world, @NotNull long skipAmount) {
        super(world);
        this.skipAmount = skipAmount;
    }

    public long getSkipAmount() {
        return this.skipAmount;
    }

    public void setSkipAmount(long skipAmount) {
        this.skipAmount = skipAmount;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @NotNull
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
