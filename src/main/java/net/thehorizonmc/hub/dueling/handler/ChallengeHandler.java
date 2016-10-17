package net.thehorizonmc.hub.dueling.handler;

import net.thehorizonmc.hub.dueling.Dueling;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ethan on 4/1/2016.
 */
public class ChallengeHandler {

    /**
     * Map that keeps track of who requests who to a sumo challenge.
     */
    public Map<Player, Player> request = new HashMap<Player, Player>();

    /**
     * Map that keeps track of whoever challenged and their challenge.
     */
    public Map<Player, Player> challengerMap = new HashMap<Player, Player>();

    /**
     * Map that keeps track of whoever was challenged and their challenger.
     */
    public Map<Player, Player> challengedMap = new HashMap<Player, Player>();

    /**
     * Instance of the class
     */
    public Dueling instance;

    /**
     * Constructor
     * @param instance
     */
    public ChallengeHandler(Dueling instance) {
        this.instance = instance;
    }

    /**
     * Finalize a challenge between two players.
     * @param challenger
     * @param target
     */
    public void challenge(Player challenger, Player target) {
        challengerMap.put(challenger, target);
        challengedMap.put(target, challenger);

        instance.getGame().initiated.add(challenger);
        instance.getGame().initiated.add(target);

        // Find open mat. //
        instance.getGame().find(challenger, target);
    }

    /**
     * Find the targeted player for a player.
     * @param challenger
     * @return
     */
    public Player getTarget(Player challenger) {
        if(!challengerMap.containsKey(challenger)) return null;

        return challengerMap.get(challenger);
    }

    /**
     * Get a player's challenger.
     * @param target
     * @return
     */
    public Player getChalleneger(Player target) {
        if(!challengedMap.containsKey(target)) return null;

        return challengedMap.get(target);
    }

}
