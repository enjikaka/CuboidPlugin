package se.jeremy.minecraft.cuboid;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.channels.FileChannel;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Material;

/*
 * Serialization of selected cuboid (NOT a cuboid area)
 */

@SuppressWarnings("serial")
public class CuboidContent implements Serializable {

	public UUID owner = null;
	private String name = "";
	private Material[][][] cuboidData;
	public byte loadReturnCode;
	private Cuboid plugin;

	CuboidContent(Cuboid instance, UUID owner, String name, Material[][][] tableau) {
		this.plugin = instance;
		this.owner = owner;
		this.name = name;
		this.cuboidData = tableau;
	}

	CuboidContent(UUID owner, String name) {
		this.owner = owner;
		this.name = name;
		this.loadReturnCode = this.load();
	}

	public Material[][][] getData() {
		return this.cuboidData;
	}

	public int save() {
		
		File cuboidFolder = Bukkit.getPluginManager().getPlugin("Cuboid").getDataFolder();
		try {
			if (!cuboidFolder.exists()) {
				cuboidFolder.mkdir();
			}
			File ownerFolder = new File(cuboidFolder + File.separator + owner);
			try {
				if (!ownerFolder.exists()) {
					ownerFolder.mkdir();
				}
			} catch (Exception e) {
				e.printStackTrace();
				return 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 1;
		}

		try {
			File cuboidFile = new File(cuboidFolder + File.separator + owner + File.separator, this.name + ".cuboid");
			ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(cuboidFile)));
			oos.writeObject(this.cuboidData);
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
			return 2;
		}
		
		Bukkit.getLogger().log(Level.INFO, "New saved cuboid : " + this.name);
		return 0;
	}

	@SuppressWarnings("resource")
	private byte load() {
		try {
			ObjectInputStream ois = new ObjectInputStream(
					new BufferedInputStream(new FileInputStream(new File(
							plugin.getDataFolder() + File.separator + owner,
							this.name + ".cuboid"))));
			try {
				this.cuboidData = (Material[][][]) (ois.readObject());
			} catch (Exception e) {
				e.printStackTrace();
				return 3;
			}
			ois.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return 1;
		} catch (IOException e) {
			e.printStackTrace();
			return 2;
		}
		plugin.getLogger().log(Level.INFO, "Loaded cuboid : " + this.name);
		return 0;
	}

	@SuppressWarnings("resource")
	public static boolean copyFile(File sourceFile, File destFile) {
		boolean returnStatus = true;
		if (!destFile.exists()) {
			try {
				destFile.createNewFile();
			} catch (Exception e) {
				returnStatus = false;
			}
		}

		FileChannel source = null;
		FileChannel destination = null;
		try {
			source = new FileInputStream(sourceFile).getChannel();
			destination = new FileOutputStream(destFile).getChannel();
			destination.transferFrom(source, 0, source.size());
		} catch (Exception e) {
			returnStatus = false;
		} finally {
			if (source != null) {
				try {
					source.close();
				} catch (Exception e) {
					returnStatus = false;
				}
			}
			if (destination != null) {
				try {
					destination.close();
				} catch (Exception e) {
					returnStatus = false;
				}
			}
		}
		return returnStatus;
	}
}
