package se.jeremy.minecraft.cuboid.commands;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import se.jeremy.minecraft.cuboid.CuboidAction;
import se.jeremy.minecraft.cuboid.CuboidAreas;
import se.jeremy.minecraft.cuboid.CuboidC;

public class CProtectCommand implements CommandExecutor {

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
			ArrayList<String> ownersList = new ArrayList<String>();
			int paramSize = args.length;
			
			if (paramSize > 1) {
				for (short i = 1; i < paramSize - 1; i++) {
					ownersList.add(args[i]);
				}
				String cuboidName = args[paramSize - 1].trim().toLowerCase();

				// TODO
				CuboidAreas.protectCuboidArea(player, ownersList, cuboidName, false);
				return true;
			} else {
				player.sendMessage(ChatColor.YELLOW + "You need to specify at least one player or group, and a name.");
				return true;
			}
		} else {
			player.sendMessage(ChatColor.RED + "No cuboid has been selected");
			return true;
		}
	}
}
