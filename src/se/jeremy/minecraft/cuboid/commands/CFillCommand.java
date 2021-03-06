package se.jeremy.minecraft.cuboid.commands;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import se.jeremy.minecraft.cuboid.CuboidAction;
import se.jeremy.minecraft.cuboid.CuboidAreas;
import se.jeremy.minecraft.cuboid.CuboidC;

public class CFillCommand implements CommandExecutor {
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

		if (CuboidAction.isReady(playerId, true)) {
			if (args.length > 1) {
				Material blockType = Material.AIR;
				blockType = Material.matchMaterial(args[0]);
				
				if (blockType != null) {
					CuboidAction.fillCuboid(playerId, blockType);
					player.sendMessage(ChatColor.GREEN + "The cuboid has been filled");
				} else {
					player.sendMessage(ChatColor.RED + "" + blockType + " is not a valid block ID.");
				}
			}
		} else {
			player.sendMessage(ChatColor.RED + "No cuboid has been selected");
		}

		return false;
	}

}
