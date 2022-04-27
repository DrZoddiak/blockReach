package me.zodd.blockreach;

import com.destroystokyo.paper.MaterialTags;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.RayTraceResult;

import java.util.*;

public class ClickListener implements Listener {
    private final List<Material> swords = new ArrayList<>(MaterialTags.SWORDS.getValues().stream().toList());

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        UUID id = p.getUniqueId();

        if (!p.hasPermission("blockreach.base")) return;

        Map<UUID,Double> map = ReachMap.map;

        if(!map.containsKey(id)) return;

        Double maxDistance = map.get(id);

        RayTraceResult result = p.rayTraceBlocks(maxDistance);
        if (result == null) return;

        //Break
        if (e.getAction().isLeftClick()) {
            if(e.hasItem() && swords.contains(Objects.requireNonNull(e.getItem()).getType())) return;
            Block block = result.getHitBlock();
            if (block == null) return;
            p.breakBlock(block);
        }

        //Build
        if (e.getAction().isRightClick()) {
            if (!e.isBlockInHand()) return;
            Location loc = result.getHitPosition().toLocation(p.getWorld());
            BlockFace face = result.getHitBlockFace();

            switch (Objects.requireNonNull(face)) {
                case WEST -> loc.add(-1.0, 0.0, 0.0);
                case DOWN -> loc.add(0.0, -1.0, 0.0);
                case NORTH -> loc.add(0.0, 0.0, -1.0);
            }

            Material mat = p.getInventory().getItemInMainHand().getType();
            loc.getBlock().setType(mat);

            if (p.getGameMode() != GameMode.CREATIVE) {
                p.getInventory().getItemInMainHand().subtract(1);
                p.updateInventory();
            }
        }
    }
}