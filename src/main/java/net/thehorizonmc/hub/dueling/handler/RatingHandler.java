package net.thehorizonmc.hub.dueling.handler;

import net.thehorizonmc.hub.dueling.Dueling;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Ethan on 4/4/2016.
 */
public class RatingHandler {

    public Map<UUID, Integer> ratingMap;

    public Dueling instance;

    public RatingHandler(Dueling instance) {
        this.instance = instance;
        ratingMap = new HashMap<UUID, Integer>();
    }

    public int getRating(UUID uuid) {
        return Bukkit.getPlayer(uuid).getExpToLevel();
    }

    public void setRating(UUID uuid, int rating) {
        ratingMap.put(uuid, rating);
    }

}
