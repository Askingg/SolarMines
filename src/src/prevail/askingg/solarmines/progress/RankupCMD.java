package prevail.askingg.solarmines.progress;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import prevail.askingg.solarmines.main.Core;
import prevail.askingg.solarmines.main.SM;

public class RankupCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String lab, String[] args) {
		if (s instanceof Player) {
			if (cmd.getName().equals("rankup")) {
				Player p = (Player) s;
				String rank = SM.perms.getPrimaryGroup(p);
				if (!Ranks.ranks.contains(rank)) {
					Core.message(SM.prefix + "Sorry, but you cannot rankup from &c" + rank, p);
					return true;
				}
				if (Ranks.isFinalRank(rank)) {
					Core.message(SM.prefix + "Sorry, but you're the final rank, &e/Prestige&f.", p);
					return true;
				}
				double cost = Ranks.getCost(p);
				if (SM.eco.getBalance(p) >= cost) {
					SM.eco.withdrawPlayer(p, cost);
					Ranks.rankup(p, rank);
				} else {
					Core.message(
							SM.prefix + "Sorry, but you don't have the &c$" + Core.decimals(0, cost) + "&f to rankup.",
							s);
				}
			}
			if (cmd.getName().equals("rankupmax")) {
				if (s instanceof Player) {
					Player p = (Player) s;
					String rank = SM.perms.getPrimaryGroup(p);
					if (!Ranks.ranks.contains(rank)) {
						Core.message(SM.prefix + "Sorry, but you cannot rankup from &c" + rank, p);
						return true;
					}
					if (Ranks.isFinalRank(rank)) {
						Core.message(SM.prefix + "Sorry, but you're the final rank, &e/Prestige&f.", p);
						return true;
					}
					String to = "";
					double total = 0.0;
					for (int c = 0; c < Ranks.ranks.size(); c++) {
						if (Ranks.ranks.get(c).equals(rank)) {
							for (int x = c + 1; x < Ranks.ranks.size(); x++) {
								String r = Ranks.ranks.get(x);
								double cost = Ranks.getCost(p, r);
								if (SM.eco.getBalance(p) >= total + cost) {
									total += cost;
									to = r;
									continue;
								}
								if (to.equals("")) {
									Core.message(SM.prefix + "Sorry, but you cannot afford to rank up.", s);
									return true;
								}
								SM.eco.withdrawPlayer(p, total);
								Ranks.rum(p, rank, to, x - c - 1);
								return true;
							}
						}
					}
					if (to.equals("")) {
						Core.message(SM.prefix + "Sorry, but you cannot afford to rank up.", s);
						return true;
					}
					SM.eco.withdrawPlayer(p, total);
					Ranks.rum(p, rank, to);
					return true;

				}
				return false;

			}
		}
		return false;
	}

}
