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

public class Tokens implements CommandExecutor {

	public static List<String> lb = new ArrayList<String>();
	public static HashMap<UUID, Double> tokens = new HashMap<UUID, Double>();

	public static void leaderboardUpdater() {
		new BukkitRunnable() {
			@Override
			public void run() {
				HashMap<String, Double> m = new HashMap<String, Double>();
				for (UUID u : tokens.keySet()) {
					m.put(Core.uuidToName(u), tokens.get(u));
				}
				lb = Core.highLowDouble(m);
			}
		}.runTaskTimerAsynchronously(SM.instance, 20, 20);
	}

	public static double balance(OfflinePlayer p) {
		UUID u = p.getUniqueId();
		if (tokens.containsKey(u)) {
			return tokens.get(u);
		}
		return -1;
	}

	@SuppressWarnings("deprecation")
	public static double balance(String p) {
		UUID u = Bukkit.getOfflinePlayer(p).getUniqueId();
		if (tokens.containsKey(u)) {
			return tokens.get(u);
		}
		return -1;
	}

	public static boolean canAfford(Player p, double d) {
		if (tokens.get(p.getUniqueId()) >= d) {
			return true;
		}
		return false;
	}

	public static void add(Player p, double d) {
		UUID u = p.getUniqueId();
		tokens.put(u, tokens.get(u) + d);
		Core.message(SM.prefix + "&b+ " + Core.decimals(0, d) + " Tokens", p);
	}

	public static void addNoMsg(Player p, double d) {
		UUID u = p.getUniqueId();
		tokens.put(u, tokens.get(u) + d);
	}

	public static void withdraw(Player p, double d) {
		UUID u = p.getUniqueId();
		tokens.put(u, tokens.get(u) - d);
		Core.message(SM.prefix + "&b- " + Core.decimals(0, d) + " Tokens", p);
	}

	public static void withdrawNoMsg(Player p, double d) {
		UUID u = p.getUniqueId();
		tokens.put(u, tokens.get(u) - d);
	}

	public static void set(Player p, double d) {
		UUID u = p.getUniqueId();
		tokens.put(u, d);
		Core.message(SM.prefix + "&fYour &bTokens&f balance was set to &b" + Core.decimals(0, d), p);
	}

	public static void setNoPM(Player p, double d) {
		UUID u = p.getUniqueId();
		tokens.put(u, d);
	}

	public static ItemStack tokenitem(int amount) {
		ItemStack i = new ItemStack(Material.SLIME_BALL, amount);
		ItemMeta m = i.getItemMeta();
		m.setDisplayName(Core.color("&bToken"));
		List<String> l = new ArrayList<String>();
		l.add(Core.color("&7"));
		l.add(Core.color("&7 Right-Click to redeem token(s)"));
		l.add(Core.color("&7 &oSneak to claim all tokens in "));
		l.add(Core.color("&7 &oyour inventory."));
		m.setLore(l);
		i.setItemMeta(m);
		return i;
	}

	public static boolean isTokenItem(ItemStack i) {
		if (i != null && i.getType() == Material.SLIME_BALL && i.hasItemMeta()) {
			ItemMeta m = i.getItemMeta();
			if (m.hasDisplayName() && m.hasLore() && m.getDisplayName().equals(Core.color("&bToken"))) {
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
				tokens.put(UUID.fromString(u), conf.getDouble(u + ".tokens"));
				x++;
			}
		}
		Core.console("&f •&f The token balance of " + x + " players was loaded.");
	}

	public static void save() {
		int x = 0;
		if (tokens.size() > 0) {
			for (UUID u : tokens.keySet()) {
				Files.data.set("users." + u.toString() + ".tokens", tokens.get(u));
				x++;
			}
			try {
				Files.data.save(Files.dataFile);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		Core.console("&f •&f The token balance of " + x + " players was saved.");
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender s, Command cmd, String lab, String[] args) {
		if (args.length == 0) {
			if (s instanceof Player) {
				Player p = (Player) s;
				Core.message(SM.prefix + "&fYou have &b" + Core.decimals(0, balance(p)) + " Tokens", p);
				return true;
			}
		} else {
			if (args[0].equalsIgnoreCase("help")) {
				Core.message("&3 -&e /Tokens Help&3 &l»&f View the help list", s);
				Core.message("&3 -&e /Tokens (Player) &3 &l»&f View a user's &bTokens&f balance", s);
				Core.message("&3 -&e /Tokens Give&3 &l»&f Give &bTokens&f to a user's account", s);
				Core.message("&3 -&e /Tokens Take&3 &l»&f Take &bTokens&f from a user's account", s);
				Core.message("&3 -&e /Tokens Withdraw&3 &l»&f Withdraw your &btokens&f to physical items", s);
				Core.message("&3 -&e /Tokens Set&3 &l»&f Set a user's &bTokens&f balance", s);
				Core.message("&3 &l * &fDeveloped  by &6Askingg&f.", s);
				return true;
			}
			if (args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("add")) { // Tokens Give <Player>
																						// <Amount> (Physical)
				if (s instanceof ConsoleCommandSender || s.hasPermission("solar.tokens.give")) {
					if (args.length < 3) {
						Core.message(SM.prefix + "Usage&3 &l»&b /Tokens Give <Player> <Amount> <Physical>", s);
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
						p.getInventory().addItem(Tokens.tokenitem(a));
						p.updateInventory();
						Core.message(SM.prefix + "&fYou gave &b" + Core.decimals(0, a) + " Tokens&f to &b" + args[1]+"&8 (&7Physical.&8)",
								s);
						Core.message(SM.prefix+"You received &b"+Core.decimals(0, a)+" Tokens&8 (&7Physical.&8)", p);
					} else {
						add(p, a);
						Core.message(SM.prefix + "&fYou gave &b" + Core.decimals(0, a) + " Tokens&f to &b" + args[1]+"&f.",
								s);

					}
				} else {
					Core.message(SM.prefix + "Sorry, but you don't have permission to do that.", s);
				}
				return true;
			}
			if (args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("take")) { // Tokens Withdraw <Player>
																							// <Amount>
				if (s instanceof ConsoleCommandSender || s.hasPermission("solar.tokens.withdraw")) {
					if (args.length < 3) {
						Core.message(SM.prefix + "Usage&3 &l»&b /Tokens Withdraw <Player> <Amount>", s);
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
						Core.message(SM.prefix + "You set &b" + args[1] + "'s Tokens&f balance to &b0", s);
					} else {
						withdraw(p, a);
						Core.message(
								SM.prefix + "&fYou withdrew &b" + Core.decimals(0, a) + " Tokens&f from &b" + args[1],
								s);
					}
				} else {
					Core.message(SM.prefix + "Sorry, but you don't have permission to do that.", s);
				}
				return true;
			}
			if (args[0].equalsIgnoreCase("withdraw")) {
				if (s instanceof Player) {
					Player p = (Player) s;
					if (args.length > 1) {
						int x = 0;
						try {
							x = Integer.parseInt(args[1]);
						} catch (Exception ex) {
							Core.message(SM.prefix + "Sorry, but &c" + args[1] + "&f is an invalid integer.", s);
							return true;
						}
						if (x > 0) {
							if (canAfford(p, x)) {
							int m = 0;
							for (ItemStack i : p.getInventory()) {
								if (i == null || i.getType() == Material.AIR) {
									m += 64;
								}
							}
							if (x < m) {
								Tokens.withdrawNoMsg(p, x);
								p.getInventory().addItem(Tokens.tokenitem(x));
								Core.message(SM.prefix + "You withdrew &b" + Core.decimals(0, x) + "&f tokens.", p);
							} else {
								Core.message(SM.prefix + "Sorry, but you don't have enough space in your inventory.",
										s);
							}
							}else {
								Core.message(SM.prefix+"Sorry, but you don't have enough tokens to do that.", s);
							}
						} else {
							Core.message(SM.prefix + "Sorry, but you cannot withdraw less than 1 token.", s);
						}
					} else {
						Core.message(SM.prefix + "Usage&3 &l»&b /Tokens Withdraw <Amount>", s);
					}
				}
				return true;
			}
			if (args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("put")) { // Tokens Set <Player>
																						// <Amount>
				if (s instanceof ConsoleCommandSender || s.hasPermission("solar.tokens.set")) {
					if (args.length < 3) {
						Core.message(SM.prefix + "Usage&3 &l»&b /Tokens Set <Player> <Amount>", s);
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
					Core.message(SM.prefix + "You set &b" + args[1] + "'s Tokens&f balance to &b" + Core.decimals(0, a),
							s);
				} else {
					Core.message(SM.prefix + "Sorry, but you don't have permission to do that.", s);
				}
				return true;
			}
			UUID u = Bukkit.getOfflinePlayer(args[0]).getUniqueId();
			if (tokens.containsKey(u)) {
				Core.message(SM.prefix + "&f" + args[0] + "&f has &b" + Core.decimals(0, tokens.get(u)) + " Tokens", s);
			} else {
				Core.message(SM.prefix + "Sorry, but &c" + args[0] + "&f is an invalid player", s);
			}
			return true;
		}
		return false;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static int getSpent(ItemStack i) {
		List<String> l = i.getItemMeta().getLore();
		for (String s : l) {
			if (s.startsWith(Core.color("&3 &f Spent &8»&b "))) {
				return Integer.valueOf(s.split(Core.color("&8»&b "))[1]);
			}
		}
		return -1;
	}

	public static void addSpent(Player p, ItemStack i, int spent) {
		CE.pickaxeCheck(p, i);
		List<String> l = i.getItemMeta().getLore();
		int y = 0;
		for (String s : l) {
			if (s.startsWith(Core.color("&3 &f Spent &8»&b"))) {
				int x = Integer.valueOf(s.split(Core.color("&8»&b "))[1]);
				l.set(y, Core.color("&3 &f Spent &8»&b " + (x + spent)));
				ItemMeta m = i.getItemMeta();
				m.setLore(l);
				i.setItemMeta(m);
				p.getInventory().setItemInMainHand(i);
				p.updateInventory();
			}
			y++;
		}
	}
}
