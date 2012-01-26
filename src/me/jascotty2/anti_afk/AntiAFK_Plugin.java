
package me.jascotty2.anti_afk;

import java.util.logging.Level;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public class AntiAFK_Plugin extends JavaPlugin {

	public static final String name = "AntiAFK";
	protected AFK_Tracker tracker;
	protected Settings conf;

	@Override
	public void onEnable() {
		tracker = new AFK_Tracker(this);
		conf = new Settings(this);
		
		PluginManager pm = getServer().getPluginManager();
		
		pm.registerEvents(tracker, this);
		getServer().getScheduler().scheduleSyncRepeatingTask(this, tracker, 100, 100); // check every 5 seconds

		getLogger().log(Level.INFO, "Enabled");
	}

	@Override
	public void onDisable() {
		getLogger().info("Disabled");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return false;
    }

	public Settings getSettings() {
		return conf;
	}
	
	public void playerAFK(Player p) {
		if(conf.kick) {
			if(!conf.kickOP && p.isOp()) {
				return;
			}
			getLogger().info(String.format("kicking %s for idling" , p.getName()));
			p.kickPlayer(conf.getKickMsg());
			if(conf.kickBroadcast != null && !conf.kickBroadcast.trim().isEmpty()) {
				getServer().broadcastMessage(ChatColor.YELLOW + p.getName() + " " + conf.kickBroadcast);
			}
		} else {
			getServer().broadcastMessage(ChatColor.YELLOW + p.getName() + " " + conf.afkMessage);
		}
	}
	
	public void playerReturned(Player p) {
		getServer().broadcastMessage(ChatColor.YELLOW + p.getName() + " " + conf.afkBackMessage);
	}
}
