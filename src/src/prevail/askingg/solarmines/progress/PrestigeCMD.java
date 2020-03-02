package prevail.askingg.solarmines.progress;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import prevail.askingg.solarmines.main.Core;
import prevail.askingg.solarmines.main.SM;

public class PrestigeCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String string, String[] args) {
		if (args.length == 0) {
			if (s instanceof Player) {
				Player p = (Player) s;
				String r = SM.perms.getPrimaryGroup(p);
				if (Ranks.isFinalRank(r)) {
					if (SM.eco.getBalance(p) >= Ranks.getPrestigeCost(p)) {
						Ranks.prestige(p);
					} else {
						Core.message(SM.prefix + "Sorry, but you cannot afford the &c"
								+ Core.number(Ranks.getPrestigeCost(p)) + "&f to prestige.", s);
					}
				} else {
					Core.message(SM.prefix + "Sorry, but you cannot prestige from &c" + r + "&f.", s);
				}
			}
		} else {
			if (args[0].equalsIgnoreCase("set")) { // Prestige Set <Player> <Prestige>
				if (s instanceof ConsoleCommandSender || s.hasPermission("solar.prestige.set")) {
					if (args.length >= 3) {
						Player p = Bukkit.getPlayer(args[1]);
						if (p != null) {
							int x = 0;
							try {
								x = Integer.parseInt(args[2]);
							} catch (Exception ex) {
								Core.message(SM.prefix + "Sorry, but &c" + args[2] + "&f is an invalid integer.", s);
								return true;
							}
							if (x < 0)
								x = 0;
							if (x > 100)
								x = 100;
							if (Ranks.setPrestige(p, x)) {
								Core.message(SM.prefix + "You set &b" + p.getName() + "'s&f prestige to &a" + x, s);
								Core.message(SM.prefix + "Your prestige was set to &b" + x + "&f by &a" + s.getName(),
										p);
							} else {
								Core.message(SM.prefix + "&b" + p.getName() + "'s &fprestige was not changed.", s);
							}
						} else {
							Core.message(SM.prefix + "Sorry, but &c" + args[1] + "&f is an invalid player.", s);
						}
					} else {
						Core.message(SM.prefix + "&fUsage&8 &l>>&b /Prestige Set <Player> <Prestige>", s);
					}
				} else {
					Core.message(SM.prefix + "Sorry, but you don't have permission to do that.", s);
				}
				return true;
			}
			Core.message("&3 -&e /Prestige&3 &l»&f Reset your rank, and start over.", s);
			Core.message("&3 -&e /Prestige Help&3 &l»&f View the help list", s);
			Core.message("&3 -&e /Prestige Set&3 &l»&f Set a user's prestige.", s);
			Core.message("&3 &l * &fDeveloped  by &bAskingg.", s);

		}
		return false;
	}

}
