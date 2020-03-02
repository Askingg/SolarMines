package prevail.askingg.solarmines.enchanting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import prevail.askingg.solarmines.main.Core;
import prevail.askingg.solarmines.main.Files;
import prevail.askingg.solarmines.main.SM;

public class PrestigePoints implements CommandExecutor {

	public static List<String> lb = new ArrayList<String>();
	public static HashMap<UUID, Double> points = new HashMap<UUID, Double>();

	public static void leaderboardUpdater() {
		new BukkitRunnable() {
			@Override
			public void run() {
				HashMap<String, Double> m = new HashMap<String, Double>();
				for (UUID u : points.keySet()) {
					m.put(Core.uuidToName(u), points.get(u));
				}
				lb = Core.highLowDouble(m);
			}
		}.runTaskTimerAsynchronously(SM.instance, 20, 20);
	}

	public static double balance(OfflinePlayer p) {
		UUID u = p.getUniqueId();
		if (points.containsKey(u)) {
			return points.get(u);
		}
		return -1;
	}

	@SuppressWarnings("deprecation")
	public static double balance(String p) {
		UUID u = Bukkit.getOfflinePlayer(p).getUniqueId();
		if (points.containsKey(u)) {
			return points.get(u);
		}
		return -1;
	}

	public static boolean canAfford(Player p, double d) {
		if (points.get(p.getUniqueId()) >= d) {
			return true;
		}
		return false;
	}

	public static void add(Player p, double d) {
		UUID u = p.getUniqueId();
		points.put(u, points.get(u) + d);
		Core.message(SM.prefix + "&c+ " + Core.decimals(0, d) + " PrestigePoints", p);
	}

	public static void addNoMsg(Player p, double d) {
		UUID u = p.getUniqueId();
		points.put(u, points.get(u) + d);
	}

	public static void withdraw(Player p, double d) {
		UUID u = p.getUniqueId();
		points.put(u, points.get(u) - d);
		Core.message(SM.prefix + "&c- " + Core.decimals(0, d) + " PrestigePoints", p);
	}

	public static void withdrawNoMsg(Player p, double d) {
		UUID u = p.getUniqueId();
		points.put(u, points.get(u) - d);
	}

	public static void set(Player p, double d) {
		UUID u = p.getUniqueId();
		points.put(u, d);
		Core.message(SM.prefix + "&fYour &cpoints&f balance was set to &c" + Core.decimals(0, d), p);
	}

	public static void setNoPM(Player p, double d) {
		UUID u = p.getUniqueId();
		points.put(u, d);
	}

	public static ItemStack tokenitem(int amount) {
		ItemStack i = new ItemStack(Material.SLIME_BALL, amount);
		ItemMeta m = i.getItemMeta();
		m.setDisplayName(Core.color("&cToken"));
		List<String> l = new ArrayList<String>();
		l.add(Core.color("&7"));
		l.add(Core.color("&7 Right-Click to redeem token(s)"));
		l.add(Core.color("&7 &oSneak to claim all points in "));
		l.add(Core.color("&7 &oyour inventory."));
		m.setLore(l);
		i.setItemMeta(m);
		return i;
	}

	public static boolean isTokenItem(ItemStack i) {
		if (i != null && i.getType() == Material.SLIME_BALL && i.hasItemMeta()) {
			ItemMeta m = i.getItemMeta();
			if (m.hasDisplayName() && m.hasLore() && m.getDisplayName().equals(Core.color("&cToken"))) {
				List<String> l = m.getLore();
				if (l.size() > 3 && l.get(1).equals(Core.color("&7 Right-Click to redeem token(s)"))) {
					return true;
				}
			}
		}
		return false;
	}

	public static void load() {
		ConfigurationSection conf = Files.data.getConfigurationSection("users");
		int x = 0;
		if (conf != null) {
			for (String u : conf.getKeys(false)) {
				points.put(UUID.fromString(u), conf.getDouble(u + ".points"));
				x++;
			}
		}
		Core.console("&f •&f The point balance of " + x + " players was loaded.");
	}

	public static void save() {
		int x = 0;
		if (points.size() > 0) {
			for (UUID u : points.keySet()) {
				Files.data.set("users." + u.toString() + ".points", points.get(u));
				x++;
			}
			try {
				Files.data.save(Files.dataFile);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		Core.console("&f •&f The point balance of " + x + " players was saved.");
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender s, Command cmd, String lab, String[] args) {
		if (args.length == 0) {
			if (s instanceof Player) {
				Player p = (Player) s;
				Core.message(SM.prefix + "&fYou have &c" + Core.decimals(0, balance(p)) + " points", p);
				return true;
			}
		} else {
			if (args[0].equalsIgnoreCase("help")) {
				Core.message("&3 -&e /Points Help&3 &l»&f View the help list", s);
				Core.message("&3 -&e /Points (Player) &3 &l»&f View a user's &cpoints&f balance", s);
				Core.message("&3 -&e /Points Give&3 &l»&f Give &cpoints&f to a user's account", s);
				Core.message("&3 -&e /Points Take&3 &l»&f Take &cpoints&f from a user's account", s);
				Core.message("&3 -&e /Points Set&3 &l»&f Set a user's &cpoints&f balance", s);
				Core.message("&3 &l * &fDeveloped  by &6Askingg&f.", s);
				return true;
			}
			if (args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("add")) { // points Give <Player>
																						// <Amount> (Physical)
				if (s instanceof ConsoleCommandSender || s.hasPermission("solar.points.give")) {
					if (args.length < 3) {
						Core.message(SM.prefix + "Usage&3 &l»&c /points Give <Player> <Amount> <Physical>", s);
						return true;
					}
					Player p = Bukkit.getPlayer(args[1]);
					if (p == null) {
						Core.message(SM.prefix + "Sorry, but &c" + args[1] + "&f is an invalid player", s);
						return true;
					}
					int a = 0;
					try {
						a = Integer.parseInt(args[2]);
					} catch (Exception ex) {
						Core.message(SM.prefix + "Sorry, but &c" + args[2] + "&f is an invalid integer", s);
						return true;
					}
					if (a < 1) {
						Core.message(SM.prefix + "Sorry, but the amount must be greater than 0", s);
						return true;
					}
					if (args.length > 3 && args[3].equalsIgnoreCase("physical")) {
						p.getInventory().addItem(PrestigePoints.tokenitem(a));
						p.updateInventory();
						Core.message(SM.prefix + "&fYou gave &c" + Core.decimals(0, a) + " points&f to &c" + args[1]+"&8 (&7Physical.&8)",
								s);
						Core.message(SM.prefix+"You received &c"+Core.decimals(0, a)+" points&8 (&7Physical.&8)", p);
					} else {
						add(p, a);
						Core.message(SM.prefix + "&fYou gave &c" + Core.decimals(0, a) + " points&f to &c" + args[1]+"&f.",
								s);

					}
				} else {
					Core.message(SM.prefix + "Sorry, but you don't have permission to do that.", s);
				}
				return true;
			}
			if (args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("take")) { // points Withdraw <Player>
																							// <Amount>
				if (s instanceof ConsoleCommandSender || s.hasPermission("solar.points.withdraw")) {
					if (args.length < 3) {
						Core.message(SM.prefix + "Usage&3 &l»&c /points Withdraw <Player> <Amount>", s);
						return true;
					}
					Player p = Bukkit.getPlayer(args[1]);
					if (p == null) {
						Core.message(SM.prefix + "Sorry, but &c" + args[1] + "&f is an invalid player", s);
						return true;
					}
					int a = 0;
					try {
						a = Integer.parseInt(args[2]);
					} catch (Exception ex) {
						Core.message(SM.prefix + "Sorry, but &c" + args[2] + "&f is an invalid integer", s);
						return true;
					}
					if (a < 1) {
						Core.message(SM.prefix + "Sorry, but the amount must be greater than 0", s);
						return true;
					}
					if (balance(p) - a < 0) {
						set(p, 0);
						Core.message(SM.prefix + "You set &c" + args[1] + "'s points&f balance to &c0", s);
					} else {
						withdraw(p, a);
						Core.message(
								SM.prefix + "&fYou withdrew &c" + Core.decimals(0, a) + " points&f from &c" + args[1],
								s);
					}
				} else {
					Core.message(SM.prefix + "Sorry, but you don't have permission to do that.", s);
				}
				return true;
			}
			if (args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("put")) { // points Set <Player>
																						// <Amount>
				if (s instanceof ConsoleCommandSender || s.hasPermission("solar.points.set")) {
					if (args.length < 3) {
						Core.message(SM.prefix + "Usage&3 &l»&c /Points Set <Player> <Amount>", s);
						return true;
					}
					Player p = Bukkit.getPlayer(args[1]);
					if (p == null) {
						Core.message(SM.prefix + "Sorry, but &c" + args[1] + "&f is an invalid player", s);
						return true;
					}
					int a = 0;
					try {
						a = Integer.parseInt(args[2]);
					} catch (Exception ex) {
						Core.message(SM.prefix + "Sorry, but &c" + args[2] + "&f is an invalid integer", s);
						return true;
					}
					if (a < 1) {
						Core.message(SM.prefix + "Sorry, but the amount must be greater than 0", s);
						return true;
					}
					set(p, a);
					Core.message(SM.prefix + "You set &c" + args[1] + "'s Points&f balance to &c" + Core.decimals(0, a),
							s);
				} else {
					Core.message(SM.prefix + "Sorry, but you don't have permission to do that.", s);
				}
				return true;
			}
			UUID u = Bukkit.getOfflinePlayer(args[0]).getUniqueId();
			if (points.containsKey(u)) {
				Core.message(SM.prefix + "&f" + args[0] + "&f has &c" + Core.decimals(0, points.get(u)) + " points", s);
			} else {
				Core.message(SM.prefix + "Sorry, but &c" + args[0] + "&f is an invalid player", s);
			}
			return true;
		}
		return false;
	}
}
