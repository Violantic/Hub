package net.thehorizonmc.hub.dueling;

import net.thehorizonmc.hub.Hub;
import net.thehorizonmc.hub.HubFeature;
import net.thehorizonmc.hub.dueling.game.listener.GameListener;
import net.thehorizonmc.hub.dueling.handler.ChallengeHandler;
import net.thehorizonmc.hub.dueling.handler.GameHandler;
import net.thehorizonmc.hub.dueling.handler.QueHandler;
import net.thehorizonmc.hub.dueling.handler.RatingHandler;
import org.bukkit.ChatColor;

/**
 * Created by Ethan on 4/4/2016.
 */
public class Dueling implements HubFeature {

    private boolean enabled = false;

    private QueHandler queHandler;
    private RatingHandler ratingHandler;
    private ChallengeHandler challengeHandler;
    public GameHandler gameHandler;

    private Hub instance;
    public Dueling(Hub instance) {
        this.instance = instance;
    }

    public void onEnable() {
        enabled = true;

        queHandler = new QueHandler(this);
        ratingHandler = new RatingHandler(this);
        challengeHandler = new ChallengeHandler(this);
        gameHandler = new GameHandler(Hub.getPlugin(Hub.class));

        instance.getServer().getPluginManager().registerEvents(new GameListener(instance), instance);
    }

    public void onDisable() {
        enabled = false;
    }

    public GameHandler getGame() {
        return gameHandler;
    }

    public ChallengeHandler getChallenges() {

        return challengeHandler;
    }

    public String getPrefix() {
        return ChatColor.YELLOW + "Dueling: " + ChatColor.RESET;
    }

    public QueHandler getQue() {
        return queHandler;
    }

    public RatingHandler getRating() {
        return ratingHandler;
    }

}
