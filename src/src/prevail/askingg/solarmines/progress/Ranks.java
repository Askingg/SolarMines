package prevail.askingg.solarmines.progress;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import prevail.askingg.solarmines.enchanting.PrestigePoints;
import prevail.askingg.solarmines.main.Core;
import prevail.askingg.solarmines.main.Files;
import prevail.askingg.solarmines.main.SM;

public class Ranks {

	public static List<String> ranks = new ArrayList<String>();
	public static HashMap<String, Double> costBase = new HashMap<String, Double>();

	public static String getFirstRank() {
		return ranks.get(0);
	}

	public static String getFinalRank() {
		return ranks.get(ranks.size() - 1);
	}

	public static boolean isFinalRank(String rank) {
		if (rank.equals(getFinalRank())) {
			return true;
		}
		return false;
	}

	public static String rankupTo(String from) {
		for (int x = 0; x < ranks.size(); x++) {
			if (ranks.get(x).equals(from)) {
				return ranks.get(x + 1);
			}
		}
		return "";
	}

	public static double getCost(Player p) {
		double asc = getAscension(p) + 1;
		double pres = getPrestige(p) + 1;
		return ((costBase.get(rankupTo(SM.perms.getPrimaryGroup(p)))) * pres) * asc;
	}

	public static double getCost(Player p, String rank) {
		double asc = getAscension(p) + 1;
		double pres = getPrestige(p) + 1;
		return ((costBase.get(rank)) * pres) * asc;
	}

	public static void rankup(Player p, String rank) {
		String to = rankupTo(rank);
		SM.perms.playerRemoveGroup(null, p, rank);
		SM.perms.playerAddGroup(null, p, to);
		Core.broadcast(SM.prefix + "&a" + p.getName() + "&f ranked up from&b " + rank + "&f to &b" + to + "&f.");
	}

	public static void rum(Player p, String from, String to, int ranks) {
		SM.perms.playerRemoveGroup(null, p, from);
		SM.perms.playerAddGroup(null, p, to);
		Core.broadcast(SM.prefix + "&a" + p.getName() + "&f ranked up from&b " + from + "&f to &b" + to + " &8(&b"
				+ ranks + " Ranks&8)");
	}

	public static void rum(Player p, String from, String to) {
		SM.perms.playerRemoveGroup(null, p, from);
		SM.perms.playerAddGroup(null, p, to);
		Core.broadcast(SM.prefix + "&a" + p.getName() + "&f ranked up from&b " + from + "&f to &b" + to);
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static HashMap<UUID, Integer> ascensions = new HashMap<UUID, Integer>();
	public static HashMap<UUID, Integer> prestiges = new HashMap<UUID, Integer>();
	public static double prestigeCost;

	public static Integer getAscension(Player p) {
		UUID u = p.getUniqueId();
		if (ascensions.containsKey(u)) {
			return ascensions.get(u);
		}
		return 0;
	}

	public static void ascend(Player p) {
		SM.perms.playerRemoveGroup(null, p, getFinalRank());
		SM.perms.playerAddGroup(null, p, getFirstRank());
		SM.eco.withdrawPlayer(p, SM.eco.getBalance(p));
		prestiges.put(p.getUniqueId(), 0);
		ascensions.put(p.getUniqueId(), getAscension(p) + 1);
		Core.broadcast(SM.prefix + "&a" + p.getName() + "&f ASCENDED TO &b" + ascensions.get(p.getUniqueId()));
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "spawn " + p.getName());
	}

	// Returns true/false - if the value was changed.
	public static boolean setAscension(Player p, int x) {
		UUID u = p.getUniqueId();
		if (x == getAscension(p)) {
			return false;
		} else {
			if (x == 0) {
				ascensions.remove(u);
			} else {
				ascensions.put(u, x);
			}
			return true;
		}
	}

	public static double getAscensionCost(Player p) {
		double asc = getAscension(p) + 1;
		return 5000000000000.0* asc;
	}

	public static Integer getPrestige(Player p) {
		UUID u = p.getUniqueId();
		if (prestiges.containsKey(u)) {
			return prestiges.get(u);
		}
		return 0;
	}

	// Returns true/false - if the value was changed.
	public static boolean setPrestige(Player p, int x) {
		UUID u = p.getUniqueId();
		if (x == getPrestige(p)) {
			return false;
		} else {
			if (x == 0) {
				prestiges.remove(u);
			} else {
				prestiges.put(u, x);
			}
			return true;
		}
	}

	public static void prestige(Player p) {
		SM.perms.playerRemoveGroup(null, p, getFinalRank());
		SM.perms.playerAddGroup(null, p, getFirstRank());
		int pres = getPrestige(p) + 1;
		prestiges.put(p.getUniqueId(), pres);
		Core.broadcast(SM.prefix + "&a" + p.getName() + "&f prestiged to &b" + prestiges.get(p.getUniqueId()));
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "eco set " + p.getName()+" 0");
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "spawn " + p.getName());
		double points = PrestigePoints.points.get(p.getUniqueId())+1;
		PrestigePoints.points.put(p.getUniqueId(), points);
		if (points%10==0) {
			Core.message(SM.prefix+"You now have &c"+Core.decimals(0, points)+" Points&f.", p);
		}
	}

	public static double getPrestigeCost(Player p) {
		double asc = getAscension(p) + 1;
		double pres = getPrestige(p) + 1;
		return (1000000000.0 * pres) * asc;
	}

	public static void load() {
		ConfigurationSection conf = Files.data.getConfigurationSection("users");
		if (conf != null) {
			for (String s : conf.getKeys(false)) {
				UUID u = UUID.fromString(s);
				if (conf.getString(s + ".prestiges") != null) {
					prestiges.put(u, conf.getInt(s + ".prestiges"));
				}
				if (conf.getString(s + ".ascensions") != null) {
					prestiges.put(u, conf.getInt(s + ".ascensions"));
				}
			}
		}
	}

	public static void save() {
		if (prestiges.size() > 0) {
			for (UUID u : prestiges.keySet()) {
				Files.data.set("users." + u.toString() + ".prestiges", prestiges.get(u));
			}
		}

		if (ascensions.size() > 0) {
			for (UUID u : ascensions.keySet()) {
				Files.data.set("users." + u.toString() + ".ascensions", ascensions.get(u));
			}
		}
		try {
			Files.data.save(Files.dataFile);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
