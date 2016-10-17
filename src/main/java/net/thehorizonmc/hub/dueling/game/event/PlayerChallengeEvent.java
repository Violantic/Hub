package net.thehorizonmc.hub.dueling.game.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by Ethan on 9/4/2016.
 */
public class PlayerChallengeEvent extends Event implements Cancellable {

    private Player player;
    private Player target;

    private boolean cancelled = false;

    public boolean isCancelled() {
        return cancelled;
    }

    public Player getPlayer() {
        return player;
    }

    public Player getTarget() {
        return target;
    }

    public PlayerChallengeEvent(Player player, Player target) {

        this.player = player;
        this.target = target;
    }

    public void setCancelled(boolean b) {
        cancelled = b;
    }

    @Override
    public HandlerList getHandlers() {
        return null;
    }
}
