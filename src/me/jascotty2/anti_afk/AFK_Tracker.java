package me.jascotty2.anti_afk;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class AFK_Tracker implements Listener, Runnable {

	final AntiAFK_Plugin plugin;
	final Map<String, PlayerInfo> players = new HashMap<String, PlayerInfo>();

	public AFK_Tracker(AntiAFK_Plugin instance) {
		plugin = instance;
		updatePlayerlist();
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onBlockBreak(BlockBreakEvent event) {
		update(event.getPlayer().getName());
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerChat(PlayerChatEvent event) {
		update(event.getPlayer().getName());
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		update(event.getPlayer().getName());
	}

	protected void update(String pn) {
		if (players.containsKey(pn)) {
			players.get(pn).updateTime();
		} else {
			updatePlayerlist();
		}
	}

	protected final void updatePlayerlist() {
		synchronized (players) {
			Player[] servPlayers = plugin.getServer().getOnlinePlayers();
			Map<String, PlayerInfo> copy = new HashMap<String, PlayerInfo>();
			copy.putAll(players);
			players.clear();
			for (Player p : servPlayers) {
				String pn = p.getName();
				if (copy.containsKey(pn)) {
					players.put(pn, copy.get(pn));
					copy.remove(pn);
				} else {
					players.put(pn, new PlayerInfo(p.getLocation()));
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent event) {
		players.put(event.getPlayer().getName(), new PlayerInfo(event.getPlayer().getLocation()));
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerQuit(PlayerQuitEvent event) {
		players.remove(event.getPlayer().getName());
	}
	
    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
		if(!event.isCancelled() && !plugin.getSettings().isAfkPickup()) {
			PlayerInfo i = players.get(event.getPlayer().getName());
			if(i != null && i.isAFK) {
				event.setCancelled(true);
			}
		}
    }

	@Override
	public void run() {
		Settings conf = plugin.getSettings();
		Player[] servPlayers = plugin.getServer().getOnlinePlayers();
		synchronized (players) {
			for (Player p : servPlayers) {
				if (!conf.kick || (conf.kickOP || !p.isOp())) {
					PlayerInfo i = players.get(p.getName());
					if ((conf.vehicleMove ? !i.hasMoved(p.getLocation())
							: (!p.isInsideVehicle() && !i.hasMoved(p.getLocation())))
							&& i.lastMove() > conf.afkTimeout) {
						i.isAFK = true;
						plugin.playerAFK(p);
					} else if (i.isAFK) {
						// no longer AFK
						i.isAFK = false;
						plugin.playerReturned(p);
					}
					break;
				}
			}
		}
	}
}
