package net.thehorizonmc.hub;

/**
 * Created by Ethan on 9/5/2016.
 */
public interface HubFeature {
    void onEnable();
    void onDisable();
    String getPrefix();
}
