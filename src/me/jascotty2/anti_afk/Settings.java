package me.jascotty2.anti_afk;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import org.bukkit.plugin.messaging.Messenger;

public class Settings {

	final AntiAFK_Plugin plugin;
	
	public long afkTimeout = 30;
	public boolean kick = true, 
			kickOP = false,
			vehicleMove = true, // if movements while in a vehicle are counted 
			afkPickup = false, // if an AFK can pickup Items
			allowBed = true, // if in bed, allow for 2x more time before kicking
			afkAsleep = true; // if afk, count them as asleep?
	protected String kickMsg = "Kicked for idling.";
	public String kickBroadcast = "was kicked for idling.",
			afkMessage = "is now AFK",
			afkBackMessage = "is no longer AFK";

	public String getKickMsg() {
		return kickMsg.replace("<sec>", String.valueOf(afkTimeout));
	}

	public boolean isAfkPickup() {
		return !kick && afkPickup;
	}
	
	public Settings(AntiAFK_Plugin instance) {
		plugin = instance;
	}

	protected static void extractResource(String jarPath, String destFolder) throws IOException {
		extractResource(jarPath, new File(destFolder));
	}

	protected static void extractResource(String jarPath, File destFolder) throws IOException {
		File j = new File(jarPath);
		File wr = new File(destFolder, j.getName());
//		wr.createNewFile();
//		InputStream res = Messenger.class.getResourceAsStream(jarPath);
//		FileWriter tx = new FileWriter(wr);
//		try {
//			for (int i = 0; (i = res.read()) > 0;) {
//				tx.write(i);
//			}
//		} finally {
//			tx.flush();
//			tx.close();
//			res.close();
//		}
		byte buf[] = new byte[1024];
		int l;
		URL res = Messenger.class.getResource(jarPath);
		if (res == null) {
			throw new IOException("cannot find " + jarPath + " in jar");
		}
		URLConnection resConn = res.openConnection();
		resConn.setUseCaches(false);
		InputStream in = resConn.getInputStream();
		if (!wr.exists()) {
			// first check if need to create the directory structure
			if (destFolder.exists() && !destFolder.isDirectory()) {
				throw new IOException("cannot use '" + destFolder.getAbsolutePath() + "': is a directory");
			} else if (!destFolder.exists() && !destFolder.mkdirs()) {
				throw new IOException("cannot use '" + destFolder.getAbsolutePath() + "': cannot create the directory");
			}
			if (!wr.createNewFile()) {
				throw new IOException("cannot write to " + wr.getAbsolutePath());
			}
		}
		FileOutputStream out = new FileOutputStream(wr);

		while ((l = in.read(buf)) > 0) {
			out.write(buf, 0, l);
		}
		in.close();
		out.close();
	}
}
