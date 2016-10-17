package net.thehorizonmc.hub.dueling.handler;

import net.thehorizonmc.hub.dueling.Dueling;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Ethan on 4/4/2016.
 */
public class QueHandler {

    Map<UUID, UUID> playerQue;

    private Dueling instance;
    public QueHandler(Dueling instance) {
        this.instance = instance;
        this.playerQue = new ConcurrentHashMap<UUID, UUID>();
    }



}
