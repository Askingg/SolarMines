package prevail.askingg.solarmines.misc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

public class Blocks implements CommandExecutor {

	public static HashMap<UUID, Integer> broken = new HashMap<UUID, Integer>();

	public static void load(Player p) {
		UUID u = p.getUniqueId();
		ConfigurationSection conf = Files.data.getConfigurationSection("users");
		if (conf != null) {
			if (conf.getString(u.toString() + ".blocks") != null) {
				broken.put(u, conf.getInt(u.toString() + ".blocks"));
			}
		}
	}

	public static void save(Player p) {
		UUID u = p.getUniqueId();
		if (broken.containsKey(u)) {
			Files.data.set("users." + u.toString() + ".blocks", broken.get(u));
			try {
				Files.data.save(Files.dataFile);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			broken.remove(u);
		}
	}

	///////////////////////////////////////////////////////////////////////////////////////

	public static List<String> lb = new ArrayList<String>();
	public static HashMap<String, Integer> lbBroken = new HashMap<String, Integer>();
	public static int lbPages = 0;

	public static void leaderboard() {
		new BukkitRunnable() {
			@Override
			public void run() {
				lbBroken.clear();
				if (Files.data.getConfigurationSection("users") != null) {
					for (String s : Files.data.getConfigurationSection("users").getKeys(false)) {
						lbBroken.put(Core.uuidToName(UUID.fromString(s)), Files.data.getInt("users." + s + ".blocks"));
					}
				}
				if (Bukkit.getOnlinePlayers().size() > 0) {
					for (Player p : Bukkit.getOnlinePlayers()) {
						lbBroken.put(p.getName(), broken.get(p.getUniqueId()));
					}
				}
				lb = Core.highLowInt(lbBroken);
				lbPages = (lb.size() / 10) + 1;
			}
		}.runTaskTimerAsynchronously(SM.instance, 10, 60 * 20);
	}

	public static List<String> getPage(int pg) {
		List<String> l = new ArrayList<String>();
		for (int x = (10 * pg) - 10; x < pg * 10; x++) {
			if (lb.size() > x) {
				l.add(lb.get(x));
			} else {
				break;
			}
		}
		return l;
	}

	///////////////////////////////////////////////////////////////////////////////////////

	public static Integer getBlocks(Player p) {
		UUID u = p.getUniqueId();
		if (broken.containsKey(u)) {
			return broken.get(u);
		}
		return 0;
	}

	///////////////////////////////////////////////////////////////////////////////////////

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String lab, String[] args) {
		if (cmd.getName().equalsIgnoreCase("blocks")) {
			if (args.length == 0) {
				if (s instanceof Player) {
					Player p = (Player) s;
					UUID u = p.getUniqueId();
					Core.message(SM.prefix + "You have mined &b" + Core.decimals(0, broken.get(u)) + "&f blocks.", p);
				}
			} else {
				if (args[0].equalsIgnoreCase("set")) { // Blocks Set <Player> <Amount>
					if (s instanceof ConsoleCommandSender || s.hasPermission("solar.blocks.set")) {
						if (args.length > 2) {
							Player p = Bukkit.getPlayer(args[1]);
							if (p != null) {
								int x = 0;
								try {
									x = Integer.parseInt(args[2]);
								} catch (Exception ex) {
									Core.message(SM.prefix + "Sorry, but &c" + args[2] + "&f is an invalid integer.",
											s);
									return true;
								}
								if (x < 0) {
									x = 0;
								}
								broken.put(p.getUniqueId(), x);
								Core.message(SM.prefix + "You set &6" + p.getName() + "'s&f blocks to &e"
										+ Core.decimals(0, x), s);
								Core.message(SM.prefix + "Your blocks were set to &e" + Core.decimals(0, x), p);
							} else {
								Core.message(SM.prefix + "Sorry, but &c" + args[1] + "&f is an invalid player.", s);
							}
						} else {
							Core.message(SM.prefix + "Usage &e/Blocks Set <Player> <Blocks>", s);
						}
					} else {
						Core.message(SM.prefix + "Sorry, but you don't have permission to do that.", s);
					}
					return true;
				}
				Player p = Bukkit.getPlayer(args[0]);
				if (p == null) {
					Core.message(SM.prefix + "Sorry, but &c" + args[0] + "&f is an invalid player", s);
					return true;
				}
				Core.message(SM.prefix + "&b" + p.getName() + "&f has mined &b"
						+ Core.decimals(0, broken.get(p.getUniqueId())) + " blocks.", s);
			}
		}
		if (cmd.getName().equalsIgnoreCase("blockstop")) {
			if (args.length == 0) {
				if (lb.size() >= 10) {
					String m = "&f &l *&7&m---------&l----&f &6BlocksBroken&7 &m---------&l----&f&l*";
					for (int x = 0; x < 10; x++) {
						String pl = lb.get(x);
						m += "\n&3 &3 &3 &3 &l»&f #" + (x + 1) + "&3 &l»&a " + pl + "&8 (&b"
								+ Core.decimals(0, lbBroken.get(pl)) + " blocks&8)";
					}
					m += "\n&f &l *&7&m----------------------&e 1&7/&6y&7 &m------------&f&l*";
					Core.message(m, s);
				} else {
					String m = "&f &l *&7&m---------&l----&f &6BlocksBroken&7 &m---------&l----&f&l*";
					for (int x = 0; x < lb.size(); x++) {
						String pl = lb.get(x);
						m += "\n&3 &3 &3 &3 &l»&f #" + (x + 1) + "&3 &l»&a " + pl + "&8 (&b"
								+ Core.decimals(0, lbBroken.get(pl)) + " blocks&8)";
					}
					m += "\n&f &l *&7&m----------------------&e 1&7/&6"+lbPages+"&7 &m------------&f&l*";
					Core.message(m, s);
				}
			} else {
				Core.message(SM.prefix + "Leaderboard pages coming soon..", s);
			}
		}
		return false;
	}

}
