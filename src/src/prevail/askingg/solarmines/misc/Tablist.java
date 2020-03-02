package prevail.askingg.solarmines.misc;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import prevail.askingg.solarmines.main.Config;
import prevail.askingg.solarmines.main.Core;
import prevail.askingg.solarmines.main.SM;

public class Tablist {

	public static void setFormats() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			p.setPlayerListName(getFormat(p));
		}
	}

	public static void timer() {
		new BukkitRunnable() {
			@Override
			public void run() {

				if (Bukkit.getOnlinePlayers().size() < 1) {
					return;
				}
				for (Player p : Bukkit.getOnlinePlayers()) {
					p.setPlayerListName(Tablist.getFormat(p));
				}

			}
		}.runTaskTimerAsynchronously(SM.instance, 50, 50);
	}

	public static String getFormat(Player p) {
		if (Config.player2.contains(p.getName())) {
			// return Core.color(Core.papi(p, AFK.prefix(p)
			// + Config.playerTab.get(p.getName()).replace("%player%", p.getName()) +
			// AFK.suffix(p)));
			return Core.color(Core.papi(p, Config.playerTab.get(p.getName()).replace("%player%", p.getName())));
		}
		if (Config.perm2.size() > 0) {
			for (String s : Config.perm2) {
				if (p.hasPermission("sacrifice.format.tab." + s)) {
					// return Core.color(Core.papi(p,
					// AFK.prefix(p) + Config.permTab.get(s).replace("%player%", p.getName()) +
					// AFK.suffix(p)));
					return Core.color(Core.papi(p, Config.permTab.get(s).replace("%player%", p.getName())));
				}
			}
		}
		// return Core.color(Core.papi(p,
		// AFK.prefix(p)
		// + Config.permTab.get(Config.perm2.get(Config.perm2.size() -
		// 1)).replace("%player%", p.getName())
		// + AFK.suffix(p)));
		return Core.color(Core.papi(p,
				Config.permTab.get(Config.perm2.get(Config.perm2.size() - 1)).replace("%player%", p.getName())));
	}
}
