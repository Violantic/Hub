package net.thehorizonmc.hub.dueling.game.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by Ethan on 9/4/2016.
 */
public class DuelWinEvent extends Event {

    private Player winner;
    private Player loser;

    private static final HandlerList handlers = new HandlerList();

    private boolean cancelled = false;

    public DuelWinEvent(Player winner, Player loser) {
        this.winner = winner;
        this.loser = loser;
    }

    public Player getLoser() {
        return loser;
    }

    public Player getWinner() {
        return winner;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
