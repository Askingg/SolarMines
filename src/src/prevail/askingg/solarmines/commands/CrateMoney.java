package prevail.askingg.solarmines.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import prevail.askingg.solarmines.main.Core;
import prevail.askingg.solarmines.main.SM;
import prevail.askingg.solarmines.progress.Ranks;

public class CrateMoney implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String string, String[] args) {// CrateMoney <Player>
																							// <Amount> // <Amount>
		if (s instanceof ConsoleCommandSender || s.hasPermission("solar.cratemoney.use")) {
			if (args.length >= 2) {
				Player p = Bukkit.getPlayer(args[0]);
				if (p != null) {
					double b = 0;
					try {
						b = Double.parseDouble(args[1]);
					} catch (Exception ex) {
						Core.message(SM.prefix + "Sorry, but &c" + args[1] + "&f is an invalid double.", s);
						return true;
					}
					if (b < 0) {
						Core.message(SM.prefix + "Sorry, but the amount must be greater than 0", s);
						return true;
					}
					double d = getCrateMoney(p, b);
					SM.eco.depositPlayer(p, d);
					Core.message(SM.prefix + "You gave &b" + p.getName() + "&a $" + Core.number(d) + "&f.&8 (&7$"
							+ Core.number(b) + "&8)", s);
					Core.message(SM.prefix + "You received &a$" + Core.number(d) + "&f.&8 (&7$" + Core.number(b)
							+ "&8)", p);
					return true;
				} else {
					Core.message(SM.prefix + "Sorry, but &c" + args[0] + "&f is an invalid player.", s);
				}
			} else {
				Core.message(SM.prefix + "Usage&3 &l»&e /CrateMoney <Player> <Amount>", s);
			}
		} else {
			Core.message(SM.prefix + "Sorry, but you don't have permission to do that.", s);
		}
		return true;
	}

	public static double getCrateMoney(Player p, double base) {
		return (base * (Ranks.getPrestige(p) + 1)) * (Ranks.getAscension(p) + 1);
	}
	
}
