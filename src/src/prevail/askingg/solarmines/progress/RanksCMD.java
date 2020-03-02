package prevail.askingg.solarmines.progress;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import prevail.askingg.solarmines.main.Core;
import prevail.askingg.solarmines.main.SM;

public class RanksCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String string, String[] args) {
		if (s instanceof Player) {
			Player p = (Player) s;
			Core.message(SM.prefix + "Ranks to progress through via &e/RankUp&8 &l»", p);
			for (String r : Ranks.ranks) {
				Core.message("&3 &l-&f " + r + "&3 &l»&a $" + Core.number(Ranks.getCost(p, r)), p);
			}
		}
		return false;
	}

}
