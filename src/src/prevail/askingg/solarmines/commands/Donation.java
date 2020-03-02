package prevail.askingg.solarmines.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import prevail.askingg.solarmines.main.Core;
import prevail.askingg.solarmines.main.SM;

public class Donation implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String string, String[] args) {
		if (s instanceof ConsoleCommandSender || s.hasPermission("solar.donator.command")) {
			if (args.length > 1) {
				Player p = Bukkit.getPlayer(args[0]);
				if (p != null) {
					Core.broadcast(
							"&f &l *&7&m---------------&6 &lSolar&e&lMines&7 &m---------------&f&l*\n&f \n&f &f &f &f &a "
									+ p.getName()
									+ "&f purchased "+args[1].replace("_", " ")+"\n&f \n&f &l *&7&m------------------------------------------&f&l*");
				} else {
					Core.message(SM.prefix + "Sorry, but &c" + args[0] + "&f is an invalid player.", s);
				}
			} else {
				Core.message(SM.prefix + "Usage&3 &l»&e /Donation <Player> <Purchased>", s);
			}
		} else {
			Core.message(SM.prefix + "Sorry, but you don't have permission to do that.", s);
		}
		return true;
	}
}
