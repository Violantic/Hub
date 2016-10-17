package net.thehorizonmc.hub;

import net.thehorizonmc.hub.dueling.Dueling;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Ethan on 9/4/2016.
 */
public class Hub extends JavaPlugin {

    public Map<String, HubFeature> featureMap;

    private Dueling dueling;

    @Override
    public void onEnable() {
        featureMap = new ConcurrentHashMap<String, HubFeature>();
        dueling = new Dueling(this);

        register(dueling);
        enableAll();
    }

    @Override
    public void onDisable() {
        disableAll();
    }

    public void register(HubFeature feature) {
        featureMap.put(feature.getClass().getSimpleName(), feature);
    }

    public void enable(String feature) {
        featureMap.get(feature).onEnable();
        System.out.println((featureMap.get(feature)));
    }

    public void disable(String feature) {
        featureMap.get(feature).onDisable();
    }

    public void enableAll() {
        for(String feature : featureMap.keySet()){
            enable(feature);
        }
    }

    public void disableAll() {
        for(String feature : featureMap.keySet()) {
            disable(feature);
        }
    }

    public String getPrefix(String name) {
        return featureMap.get(name).getPrefix();
    }

    public Dueling getDueling() {
        return (Dueling) featureMap.get(Dueling.class.getSimpleName());
    }

}
