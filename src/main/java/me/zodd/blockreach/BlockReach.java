package me.zodd.blockreach;

import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import redempt.redlib.commandmanager.CommandHook;
import redempt.redlib.commandmanager.CommandParser;

import java.util.Map;
import java.util.UUID;


public final class BlockReach extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new ClickListener(), this);
        new ReachMap().registerTask();
        new CommandParser(this.getResource("command.rdcml")).parse().register("reach", this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @CommandHook("reachtoggle")
    public void toggleCommand(CommandSender sender) {
        Map<UUID,Double> rMap = ReachMap.map;
        UUID id = ((Player) sender).getUniqueId();
        Double defaultValue = 50.0;

        if(rMap.containsKey(id)) {
            rMap.remove(id);
            sender.sendActionBar(Component.text("Reach Disabled!"));
        } else {
            rMap.put(id,defaultValue);
            sender.sendActionBar(Component.text("Reach Enabled!"));
        }
    }

    @CommandHook("reachdist")
    public void changeDist(CommandSender sender, double dist) {
        Map<UUID,Double> rMap = ReachMap.map;
        UUID id = ((Player) sender).getUniqueId();

        if (dist < 0) {
            sender.sendMessage("Distance cannot be less than 0!");
            return;
        }

        if(rMap.containsKey(id)) {
            rMap.put(id,dist);
            sender.sendMessage("Changed your distance to " + dist);
        } else {
            sender.sendMessage("You do not have Reach enabled!");
        }
    }
}
