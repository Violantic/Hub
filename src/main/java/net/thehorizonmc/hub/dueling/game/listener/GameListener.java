package net.thehorizonmc.hub.dueling.game.listener;

import net.thehorizonmc.hub.Hub;
import net.thehorizonmc.hub.dueling.game.event.DuelWinEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.Iterator;

/**
 * Created by Ethan on 4/4/2016.
 */
public class GameListener implements Listener {
    
    private Hub instance;
    public GameListener(Hub instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        if(!player.hasPlayedBefore()) {
            event.getPlayer().setLevel(1);
        }
    }

    @EventHandler
    public void onUpdate(SignChangeEvent event) {
        String[] og = event.getLines();
        for(String string : og) {
            if (string.contains("{queSign}")) {
                event.setLine(1, instance.getDueling().getPrefix());
                event.setLine(2, "Que");
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
            if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if(
                        event.getClickedBlock().getType() == Material.SIGN ||
                        event.getClickedBlock().getType() == Material.WALL_SIGN
                   ) {
                    Sign sign = (Sign) event.getClickedBlock().getState();
                    for(String s : sign.getLines()) {
                        if(s.contains("Que")) {
                            // Enter the que for the player. //
                            event.getPlayer().sendMessage(instance.getDueling().getPrefix() + "Finding a match for you...");
                        }
                    }
                }
            }
    }

    /**
     * Listens for PvP'ing
     * @param event
     */
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if(!(event.getDamager() instanceof Player && event.getEntity() instanceof Player)) return;
        if(!instance.getDueling().getGame().canPvP((Player) event.getDamager())){ event.setCancelled(true); return;}
        if(
            instance.getDueling().getGame().getOpponent((Player) event.getDamager()) != event.getEntity()
                || instance.getDueling().getGame().getOpponent((Player) event.getDamager()) == null
          ) event.setCancelled(true);
    }

    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent event) {
        if(event.getRightClicked() instanceof Player) {
            if (event.getPlayer().getItemInHand().getType() == Material.IRON_SWORD) {
                Player target = (Player) event.getRightClicked();
                if (
                        instance.getDueling().getGame().canPvP(target) ||
                                instance.getDueling().getGame().canPvP(event.getPlayer())
                        ) return;
                if (target.getItemInHand().getType() != Material.IRON_SWORD) return;

                // Is player dueling someone, or accepting a duel? //
                for (Iterator<Player> iterator = instance.getDueling().getChallenges().request.keySet().iterator(); iterator.hasNext(); ) {
                    Player player = iterator.next();
                    if (instance.getDueling().getChallenges().request.get(player).equals(event.getPlayer())) {
                        // Player is accepting a duel. //

                        if(
                             instance.getDueling().getGame().initiated.contains(player) ||
                             instance.getDueling().getGame().initiated.contains(event.getPlayer())
                           ) {
                            return;
                        }
                        instance.getDueling().getChallenges().challenge(player, event.getPlayer());
                        break;
                    }
                }

                if(instance.getDueling().getChallenges().request.containsKey(event.getPlayer())) {
                    if(instance.getDueling().getChallenges().request.get(event.getPlayer()) == target) {
                        return;
                    }
                }

                instance.getDueling().getChallenges().request.put(event.getPlayer(), target);
                event.getPlayer().sendMessage(instance.getDueling().getPrefix() + "You have sent a duel request to " + ChatColor.YELLOW + target.getName());
                target.sendMessage(instance.getDueling().getPrefix() + "You have received a duel request from " + ChatColor.YELLOW + event.getPlayer().getName());
            }
        }
    }

    /**
     * Listens for when a player wins/loses
     * @param event
     */
    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if(instance.getDueling().getGame().isPlaying(event.getEntity())) {
            event.setKeepLevel(true);
            event.setDeathMessage("");
            Player loser = event.getEntity();
            Player winner = instance.getDueling().getGame().getOpponent(loser);

            event.setKeepInventory(true);
            event.setKeepLevel(true);

            DuelWinEvent duelWinEvent = new DuelWinEvent(winner, loser);
            instance.getServer().getPluginManager().callEvent(duelWinEvent);

            for(Player player : Bukkit.getOnlinePlayers()) {
                winner.showPlayer(player);
                loser.showPlayer(player);
            }

            if(instance.getDueling().getChallenges().challengerMap.containsKey(event.getEntity())) {
                // Player initially challenged someone else. //
                instance.getDueling().getChallenges().challengerMap.remove(event.getEntity());
                instance.getDueling().getChallenges().challengedMap.remove(instance.getDueling().getGame().getOpponent(event.getEntity()));
            } else if (instance.getDueling().getChallenges().challengedMap.containsKey(event.getEntity())) {
                instance.getDueling().getChallenges().challengedMap.remove(instance.getDueling().getGame().getOpponent(event.getEntity()));
                instance.getDueling().getChallenges().challengerMap.remove(instance.getDueling().getGame().getOpponent(event.getEntity()));
            }
        }
    }

    @EventHandler
    public void onWin(DuelWinEvent event) {
        for(Player player : Bukkit.getOnlinePlayers()) {
            event.getWinner().showPlayer(player);
            event.getLoser().showPlayer(player);
        }

        instance.getDueling().getGame().pvp.remove(event.getLoser());
        instance.getDueling().getGame().pvp.remove(event.getWinner());
        instance.getDueling().getGame().initiated.remove(event.getLoser());
        instance.getDueling().getGame().initiated.remove(event.getWinner());

        if(instance.getDueling().getChallenges().challengerMap.containsKey(event.getLoser())) {
            // Player initially challenged someone else. //
            instance.getDueling().getChallenges().challengerMap.remove(event.getLoser());
            instance.getDueling().getChallenges().challengedMap.remove(instance.getDueling().getGame().getOpponent(event.getLoser()));
        } else if (instance.getDueling().getChallenges().challengedMap.containsKey(event.getLoser())) {
            instance.getDueling().getChallenges().challengedMap.remove(instance.getDueling().getGame().getOpponent(event.getLoser()));
            instance.getDueling().getChallenges().challengerMap.remove(instance.getDueling().getGame().getOpponent(event.getLoser()));
        }

        Bukkit.broadcastMessage(instance.getDueling().getPrefix() + ChatColor.YELLOW + event.getWinner().getName() + ChatColor.RESET + " has beat " + ChatColor.YELLOW + event.getLoser().getName() + ChatColor.RESET + " in a duel");
    }

}
