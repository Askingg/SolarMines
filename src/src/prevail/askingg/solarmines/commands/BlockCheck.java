package prevail.askingg.solarmines.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import prevail.askingg.solarmines.main.Core;
import prevail.askingg.solarmines.main.SM;

public class BlockCheck implements CommandExecutor {

	public static List<UUID> waiting = new ArrayList<UUID>();

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String string, String[] args) { // BlockCheck <BlocksMined>
		if (args.length > 0) {
			if (s instanceof Player) {
				Player p = (Player) s;
				if (waiting.contains(p.getUniqueId())) {
					Core.message(SM.prefix + "Please wait for your current request to be processed.", s);
					return true;
				}
				final int b;
				try {
					b = Integer.parseInt(args[0]);
				} catch (Exception ex) {
					Core.message(SM.prefix + "Sorry, but &c" + args[0] + "&f is an invalid integer.", s);
					return true;
				}
				if (b > 0) {
					if (b <= 10000000) {
					waiting.add(p.getUniqueId());
					new BukkitRunnable() {
						public void run() {
							int t = 0;
							for (int x = 1; x < b; x++) {
								for (int interval : Mining.order) {
									if (x % interval == 0) {
										t += Mining.interval.get(interval);
										break;
									}
								}
							}
							Core.message(
									SM.prefix + "By the time you've mined &e" + Core.decimals(0, b)
											+ "&f blocks you'll have received &b" + Core.decimals(0, t) + " Tokens&f.",
									s);
							waiting.remove(p.getUniqueId());
						}
					}.runTaskLater(SM.instance, 1);
					}else {
						Core.message(SM.prefix+"Sorry, but the amount must be less than 10 million.", s);
					}
				} else {
					Core.message(SM.prefix + "Sorry, but you cannot mine a negative amount of blocks.", s);
				}
			}
		} else {
			Core.message(SM.prefix
					+ "View how many tokens you'll have received by the time you reach a certain milestone&3 &l�\n&3 &l�&e /BlockCheck <BlocksBroken>",
					s);
		}
		return true;
	}

}
