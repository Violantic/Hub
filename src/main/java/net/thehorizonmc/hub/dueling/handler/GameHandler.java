package net.thehorizonmc.hub.dueling.handler;

import net.thehorizonmc.hub.Hub;
import net.thehorizonmc.hub.dueling.Dueling;
import net.thehorizonmc.hub.ColorEnum;
import net.thehorizonmc.hub.dueling.game.Titles;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Ethan on 4/1/2016.
 */
public class GameHandler {

    /**
     * List that keeps track of who is currently in a match.
     */
    public List<Player> pvp = new ArrayList<Player>();

    /**
     * Players who are either dueling and pvp hasn't turned on yet, or players already in pvp.
     */
    public List<Player> initiated = new ArrayList<Player>();

    /**
     * Map that keeps track if players would like to only view duelers.
     */
    public Map<UUID, Boolean> visibleToggle = new ConcurrentHashMap<UUID, Boolean>();

    /**
     * Instance of plugin's main class.
     */
    public Hub instance;

    /**
     * Constructor.
     * @param instance
     */
    public GameHandler(Hub instance) {
        this.instance = instance;
    }

    /**
     * Can a specific player pvp?
     * @param player
     * @return
     */
    public boolean canPvP(Player player) {
        return (pvp.contains(player));
    }

    /**
     * Is a specific player playing?
     * @param player
     * @return
     */
    public boolean isPlaying(Player player) {
        return (pvp.contains(player));
    }

    /**
     * Find an open mat for the players.
     * @param challenger
     * @param challenged
     */
    public void find(final Player challenger, final Player challenged) {
        final List<Player> players = new ArrayList<Player>();
        players.add(challenger);
        players.add(challenged);

        challenged.sendMessage(instance.getPrefix(Dueling.class.getSimpleName()) + "Duel initiating");
        challenger.sendMessage(instance.getPrefix(Dueling.class.getSimpleName()) + "Duel initiating");

        for(Player player : Bukkit.getOnlinePlayers()) {
            if(!players.contains(player)) {
                for(Player s : players) {
                    s.hidePlayer(player);
                }
            }
        }

        new BukkitRunnable() {
            private int i = 0;
            public void run() {
                if(i >= 3) {
                    challenger.sendMessage(instance.getPrefix(Dueling.class.getSimpleName()) + "GO!");
                    pvp.add(challenger);
                    challenged.sendMessage(instance.getPrefix(Dueling.class.getSimpleName()) + "GO!");
                    pvp.add(challenged);
                    cancel();
                }
                i++;

                    challenged.sendMessage(instance.getPrefix(Dueling.class.getSimpleName()) + ColorEnum.RANDOM.getChatColor() + i);
                    Titles.sendTitle(challenged, 0, 1, 0, instance.getPrefix(Dueling.class.getSimpleName()) + "Duel initiating in " + ColorEnum.RANDOM.getChatColor() + i);
                    challenger.sendMessage(instance.getPrefix(Dueling.class.getSimpleName()) + ColorEnum.RANDOM.getChatColor() + i);
                    Titles.sendTitle(challenged, 0, 1, 0, instance.getPrefix(Dueling.class.getSimpleName()) + "Duel initiating in " + ColorEnum.RANDOM.getChatColor() + i);
            }
        }.runTaskTimer(instance, 20, 20);
    }

    /**
     * Find the opponent of a player.
     * @param player
     * @return
     */
    public Player getOpponent(Player player) {
        // Find opposite player. //
        if(instance.getDueling().getChallenges().challengerMap.containsKey(player)) {
            // Player initially challenged someone else. //
            return instance.getDueling().getChallenges().challengerMap.get(player);
        } else if (instance.getDueling().getChallenges().challengedMap.containsKey(player)) {
            // Player was challenged by someone else. //
            return instance.getDueling().getChallenges().challengedMap.get(player);
        }
        return null;
    }



}
