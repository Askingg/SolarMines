package prevail.askingg.solarmines.sell;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import prevail.askingg.solarmines.main.Core;
import prevail.askingg.solarmines.main.Files;
import prevail.askingg.solarmines.main.SM;

public class Booster implements CommandExecutor {

	public static double globalMoneyBoost = 0.0;
	public static long globalMoneyExpire = 0;
	public static HashMap<UUID, Double> moneyBoost = new HashMap<UUID, Double>();
	public static HashMap<UUID, Long> moneyExpire = new HashMap<UUID, Long>();

	public static void load() {
		ConfigurationSection conf = Files.data.getConfigurationSection("globalBooster");
		globalMoneyBoost = conf.getDouble("boost");
		if (conf.getLong("time") > 0) {
			globalMoneyExpire = System.currentTimeMillis() + conf.getLong("time");

		} else {
			globalMoneyExpire = 0;
		}
		conf = Files.data.getConfigurationSection("users");
		if (conf != null) {
			for (String s : conf.getKeys(false)) {
				if (conf.getConfigurationSection(s + ".booster") != null) {
					moneyBoost.put(UUID.fromString(s), conf.getDouble(s + ".booster.boost"));
					moneyExpire.put(UUID.fromString(s), System.currentTimeMillis() + conf.getLong(s + ".booster.time"));
					Files.data.set("users." + s + ".booster", null);
				}
			}
		}
		try {
			Files.data.save(Files.dataFile);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void save() {
		Files.data.set("globalBooster.boost", globalMoneyBoost);
		if (globalMoneyExpire > 0) {
			Files.data.set("globalBooster.time", globalMoneyExpire - System.currentTimeMillis());
		} else {
			Files.data.set("globalBooster.time", 0L);
		}
		for (UUID u : moneyBoost.keySet()) {
			Files.data.set("users." + u.toString() + ".booster.boost", moneyBoost.get(u));
			Files.data.set("users." + u.toString() + ".booster.time", moneyExpire.get(u) - System.currentTimeMillis());
		}
		try {
			Files.data.save(Files.dataFile);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void timer() {
		new BukkitRunnable() {
			@Override
			public void run() {
				if (globalMoneyExpire != 0 && globalMoneyBoost != 0.0) {
					if (System.currentTimeMillis() > globalMoneyExpire) {
						Core.broadcast("&f &l *&7&m----------------&a Booster&7 &m----------------&f&l*\n"
								+ "&3 &3 &3 &3 &l»&f The global &a" + Core.decimals(1, globalMoneyBoost)
								+ "x&f multiplier has ended.\n"
								+ "&f &l *&7&m----------------------------------------&f&l*");
						globalMoneyBoost = 0.0;
						globalMoneyExpire = 0L;
					}
				}
				if (moneyBoost.size() > 0) {
					for (UUID u : moneyBoost.keySet()) {
						if (System.currentTimeMillis() > moneyExpire.get(u)) {
							Player p = Bukkit.getPlayer(u);
							if (p != null) {
								Core.message("&f &l *&7&m----------------&a Booster&7 &m----------------&f&l*\n"
										+ "&3 &3 &3 &3 &l»&f Your personal &a" + Core.decimals(1, moneyBoost.get(u))
										+ "x&f multiplier has ended.\n"
										+ "&f &l *&7&m----------------------------------------&f&l*", p);
							}
							moneyBoost.remove(u);
							moneyExpire.remove(u);
						}
					}
				}
			}
		}.runTaskTimerAsynchronously(SM.instance, 20, 20);
	}

	public static void setGlobalBooster(double boost, int seconds) {
		Core.broadcast("&f &l *&7&m----------------&a Booster&7 &m----------------&f&l*\n"
				+ "&3 &3 &3 &3 &l»&f A global &a" + Core.decimals(1, boost) + "x&f has started.&8 (&b"
				+ Core.time(seconds) + "&8)" + "\n" + "&f &l *&7&m----------------------------------------&f&l*");
		globalMoneyBoost = boost;
		globalMoneyExpire = System.currentTimeMillis() + (seconds * 1000);
	}

	public static void setPersonalBooster(Player p, double boost, int seconds) {
		Core.message("&f &l *&7&m----------------&a Booster&7 &m----------------&f&l*\n"
				+ "&3 &3 &3 &3 &l»&f You now have a personal booster of &a" + Core.decimals(1, boost) + "x\n"
				+ "&l &l &l &f &f &f lasting &b" + Core.time(seconds) + "\n"
				+ "&f &l *&7&m----------------------------------------&f&l*", p);
		moneyBoost.put(p.getUniqueId(), boost);
		moneyExpire.put(p.getUniqueId(), System.currentTimeMillis() + ((long) seconds * 1000L));
	}

	public static double permissionMoney(Player p) {
		HashMap<String, Double> m = new HashMap<String, Double>();
		m.put("Mercury", 0.1);
		m.put("Venus", 0.2);
		m.put("Earth", 0.4);
		m.put("Mars", 0.6);
		m.put("Jupiter", 0.8);
		m.put("Saturn", 1.0);
		m.put("Uranus", 1.25);
		m.put("Neptune", 1.5);

		String[] g = SM.perms.getPlayerGroups(p);
		double d = 0.0;
		for (String s : g) {
			if (m.containsKey(s) && m.get(s) > d) {
				d = m.get(s);
			}
		}
		return d;
	}

	public static double getMoneyBooster(Player p) {
		double d = globalMoneyBoost + permissionMoney(p);
		if (moneyBoost.containsKey(p.getUniqueId())) {
			d += moneyBoost.get(p.getUniqueId());
		}
		if (d < 1.0) {
			return 0.0;
		}
		return d;
	}

	//////////////////////////////////////////////////////////////////////////////////////

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String string, String[] args) {
		if (args.length == 0) {
			if (s instanceof Player) {
				Player p = (Player) s;
				UUID u = p.getUniqueId();
				String msg = "&f &l *&7&m----------------&a Booster&7 &m----------------&f&l*\n";
				msg += "&3 &3 &3 &3 &l»&f Overall&3 &l»&a " + Core.decimals(1, getMoneyBooster(p)) + "x\n";
				msg += "&7 \n";
				if (moneyBoost.containsKey(u)) {
					msg += "&3 &3 &3 &3 &l»&f Personal&3 &l»&a " + Core.decimals(1, moneyBoost.get(u)) + "x&8 (&b"
							+ Core.time2((int) ((moneyExpire.get(u) - System.currentTimeMillis()) / 1000)) + "&8)\n";
				} else {
					msg += "&3 &3 &3 &3 &l»&f Personal&3 &l»&a 0.0x\n";
				}
				if ((int) ((globalMoneyExpire - System.currentTimeMillis()) / 1000) > 0) {
					msg += "&3 &3 &3 &3 &l»&f Global&3 &l»&a " + Core.decimals(1, globalMoneyBoost) + "x&8 (&b"
							+ Core.time2((int) ((globalMoneyExpire - System.currentTimeMillis()) / 1000)) + "&8)\n";
				} else {
					msg += "&3 &3 &3 &3 &l»&f Global&3 &l»&a " + Core.decimals(1, globalMoneyBoost) + "x\n";
				}
				msg += "&3 &3 &3 &3 &l»&f Rank&3 &l»&a " + Core.decimals(1, permissionMoney(p)) + "x\n";
				msg += "&f &l *&7&m----------------------------------------&f&l*";
				Core.message(msg, s);
			}
		} else {
			if (args[0].equalsIgnoreCase("give")) { // Booster Give <Player> <Boost> <Duration(Seconds)>
													// (Type(Global/Personal)>
				if (s instanceof ConsoleCommandSender || s.hasPermission("solar.booster.give")) {
					if (args.length > 3) {
						Player p = Bukkit.getPlayer(args[1]);
						if (p != null) {
							double boost = 0.0;
							try {
								boost = Double.parseDouble(args[2]);
							} catch (Exception ex) {
								Core.message(SM.prefix + "Sorry, but &c" + args[2] + "&f is an invalid double.", s);
								return true;
							}
							int duration = 0;
							try {
								duration = Integer.parseInt(args[3]);
							} catch (Exception ex) {
								Core.message(SM.prefix + "Sorry, but &c" + args[3] + "&f is an invaid integer.", s);
								return true;
							}
							String type = "Personal";
							if (args.length > 4) {
								if (args[4].equalsIgnoreCase("personal")) {

								} else if (args[4].equalsIgnoreCase("global")) {
									type = "Global";
								} else {
									Core.message(SM.prefix + "Sorry, but &c" + args[4]
											+ "&f is an invalid boost type.&8 (&bPersonal&8/Global&8)", p);
									return true;
								}
							}
							if (boost < 0.1)
								boost = 0.1;
							if (duration < 1)
								duration = 1;
							p.getInventory().addItem(TempBoosters.boosterItem(type, boost, duration));
							p.updateInventory();
							Core.message(SM.prefix + "You gave a &a" + Core.decimals(2, boost) + "&8 (&a"
									+ Core.time(duration) + "&8)&f booster to &b" + p.getName() + "&f.", s);
							Core.message(SM.prefix + "You received a &a" + Core.decimals(2, boost) + "&8 (&a"
									+ Core.time(duration) + "&8)&f booster.", p);
						} else {
							Core.message(SM.prefix + "Sorry, but &c" + args[1] + "&f is an invalid player.", s);
						}
					} else {
						Core.message(SM.prefix
								+ "Usage &3&l»&e /Booster Give <Player> <Boost> <Duration> (Type(Global/Personal))", s);
					}
				} else {
					Core.message(SM.prefix + "Sorry, but you don't have permission to do that.", s);
				}
				return true;
			}
			if (args[0].equalsIgnoreCase("global")) { // Booster Global <Boost> <Duration>
				if (s instanceof ConsoleCommandSender || s.hasPermission("solar.booster.global")) {
					if (args.length > 2) {
						double d = 0.0;
						try {
							d = Double.valueOf(args[1]);
						} catch (Exception ex) {
							Core.message(SM.prefix + "Sorry, but &c" + args[1] + "&f is an invalid double.", s);
							return true;
						}
						if (d < 1.0) {
							Core.message(SM.prefix + "Sorry, but &c" + Core.decimals(1, d) + "&f is too low.", s);
							return true;
						}
						int secs = 0;
						try {
							secs = Integer.valueOf(args[2]);
						} catch (Exception ex) {
							Core.message(SM.prefix + "Sorry, but &c" + args[2] + "&f is an invalid integer.", s);
							return true;
						}
						setGlobalBooster(d, secs);
					} else {
						Core.message(SM.prefix + "Usage &3&l»&e /Booster Global <Boost> <Duration>", s);
					}
				} else {
					Core.message(SM.prefix + "Sorry, but you don't have permission to do that.", s);
				}
				return true;
			}

			if (args[0].equalsIgnoreCase("personal")) { // Booster Pesronal <Player> <Boost> <Duration>
				if (s instanceof ConsoleCommandSender || s.hasPermission("solar.booster.personal")) {
					if (args.length > 3) {
						Player p = Bukkit.getPlayer(args[1]);
						if (p == null) {
							Core.message(SM.prefix + "Sorry, but &c" + args[1] + "&f is an invalid player.", p);
							return true;
						}
						double d = 0.0;
						try {
							d = Double.valueOf(args[2]);
						} catch (Exception ex) {
							Core.message(SM.prefix + "Sorry, but &c" + args[2] + "&f is an invalid double.", s);
							return true;
						}
						if (d < 1.0) {
							Core.message(SM.prefix + "Sorry, but &c" + Core.decimals(1, d) + "&f is too low.", s);
							return true;
						}
						int secs = 0;
						try {
							secs = Integer.valueOf(args[3]);
						} catch (Exception ex) {
							Core.message(SM.prefix + "Sorry, but &c" + args[3] + "&f is an invalid integer.", s);
							return true;
						}
						setPersonalBooster(p, d, secs);
					} else {
						Core.message(SM.prefix + "Usage &3&l»&e /Booster Personal <Player> <Boost> <Duration>", s);
					}
				} else {
					Core.message(SM.prefix + "Sorry, but you don't have permission to do that.", s);
				}
				return true;
			}
			Core.message("&3 -&e /Booster&3 &l»&f View your current booster", s);
			Core.message("&3 -&e /Booster Give&3 &l»&f Give a player a claimable temp booster", s);
			Core.message("&3 -&e /Booster Global&3 &l»&f Set a global booster", s);
			Core.message("&3 -&e /Booster Personal&3 &l»&f Set a player-specific booster", s);
			Core.message("&3 &l * &fDeveloped  by &bAskingg&f.", s);

		}
		return false;
	}

}
