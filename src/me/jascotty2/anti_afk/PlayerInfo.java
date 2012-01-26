package me.jascotty2.anti_afk;

import me.jascotty2.lib.math.Math2;
import org.bukkit.Location;

public class PlayerInfo {

	float x, y, z, yaw, pitch;
	long lastMoveTime;
	public boolean isAFK = false;
	//String world;

	public PlayerInfo(Location playerLoc) {
		set(playerLoc);
		lastMoveTime = System.currentTimeMillis();
	}

	public final void set(Location playerLoc) {
		x = (float) Math2.round(playerLoc.getX(), 1);
		y = (float) Math2.round(playerLoc.getY(), 1);
		z = (float) Math2.round(playerLoc.getZ(), 1);
		yaw = (float) Math2.round(playerLoc.getYaw(), 1);
		pitch = (float) Math2.round(playerLoc.getPitch(), 1);
		//world = playerLoc.getWorld().getName();
	}

	public void updateTime() {
		lastMoveTime = System.currentTimeMillis();
	}
	
	public boolean hasMoved(Location playerLoc) {
//		float x2, y2, z2, yaw2, pitch2;
//		x2 = (float) Math2.round(playerLoc.getX(), 1);
//		y2 = (float) Math2.round(playerLoc.getY(), 1);
//		z2 = (float) Math2.round(playerLoc.getZ(), 1);
//		yaw2 = (float) Math2.round(playerLoc.getYaw(), 1);
//		pitch2 = (float) Math2.round(playerLoc.getPitch(), 1);
//		return x != x2 || y != y2 || z != z2 || yaw != yaw2 || pitch != pitch2;
//		// slightly faster:
//		return x != (float) Math2.round(playerLoc.getX(), 1)
//				|| y != (float) Math2.round(playerLoc.getY(), 1)
//				|| z != (float) Math2.round(playerLoc.getZ(), 1)
//				|| yaw != (float) Math2.round(playerLoc.getYaw(), 1)
//				|| pitch != (float) Math2.round(playerLoc.getPitch(), 1);
		if (x != (float) Math2.round(playerLoc.getX(), 1)
				|| y != (float) Math2.round(playerLoc.getY(), 1)
				|| z != (float) Math2.round(playerLoc.getZ(), 1)
				|| yaw != (float) Math2.round(playerLoc.getYaw(), 1)
				|| pitch != (float) Math2.round(playerLoc.getPitch(), 1)) {
			set(playerLoc);
			lastMoveTime = System.currentTimeMillis();
			return true;
		}
		return false;
	}
	
	/**
	 * time since last moved
	 * @return time in seconds
	 */
	public long lastMove() {
		return (System.currentTimeMillis() - lastMoveTime) / 1000;
	}
}
