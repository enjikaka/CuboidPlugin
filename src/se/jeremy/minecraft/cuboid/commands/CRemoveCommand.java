package se.jeremy.minecraft.cuboid.commands;

import java.io.File;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import se.jeremy.minecraft.cuboid.Cuboid;
import se.jeremy.minecraft.cuboid.CuboidAreas;
import se.jeremy.minecraft.cuboid.CuboidC;

public class CRemoveCommand implements CommandExecutor {
	private Cuboid plugin;

	public CRemoveCommand(Cuboid instance) {
		this.plugin = instance;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			return true;
		}
		
		Player player = (Player) sender;
		UUID playerId = player.getUniqueId();
		
		CuboidC playersArea = CuboidAreas.findCuboidArea(player.getLocation());
		
		if (playersArea != null && !playersArea.isAllowed(cmd) && !playersArea.isOwner(player) && !player.hasPermission("cuboidplugin.ignoreownership")) {
			player.sendMessage(ChatColor.RED + "This command is disallowed in this area");
			return true;
		}

		if (args.length > 1) {
			String cuboidName = args[1].toLowerCase();
			if (plugin.cuboidExists(playerId, cuboidName)) {
				File toDelete = new File("cuboids/" + playerId + "/"
						+ cuboidName + ".cuboid");
				if (toDelete.delete()) {
					player.sendMessage(ChatColor.GREEN + "Cuboid sucessfuly deleted");
				} else {
					player.sendMessage(ChatColor.RED + "Error while deleting the cuboid file");
				}
			} else {
				player.sendMessage(ChatColor.RED + "This cuboid does not exist.");
			}
		}

		return false;
	}

}
