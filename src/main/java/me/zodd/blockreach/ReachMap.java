package me.zodd.blockreach;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import redempt.redlib.misc.Task;

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.UUID;

public class ReachMap {
    static HashMap<UUID, Double> map = new HashMap<>();

    public void registerTask() {
        Task.asyncRepeating(
                () -> map.keySet().forEach(
                uuid -> {
                    Player p = Bukkit.getPlayer(uuid);
                    if (p == null) {
                        try {
                            map.remove(uuid);
                        } catch (ConcurrentModificationException e) {
                            //TODO
                        }
                        return;
                    }
                    p.sendActionBar(Component.text("Reach Active!"));
                }
        ), 60L, 40L);
    }
}
