package prevail.askingg.solarmines.misc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import prevail.askingg.solarmines.main.Core;
import prevail.askingg.solarmines.main.Files;
import prevail.askingg.solarmines.main.SM;

public class Marry implements Listener, CommandExecutor {

	public static HashMap<UUID, String> gender = new HashMap<UUID, String>();
	public static HashMap<UUID, UUID> marriages = new HashMap<UUID, UUID>();
	public static HashMap<UUID, Long> timeOfMarriage = new HashMap<UUID, Long>();

	public static HashMap<UUID, UUID> proposals = new HashMap<UUID, UUID>(); // Proposed to, Proposer
	public static List<Player> divorceConfirm = new ArrayList<Player>();

	public static void load() {
		ConfigurationSection conf = Files.data.getConfigurationSection("users");
		for (String s : conf.getKeys(false)) {
			UUID u = UUID.fromString(s);
			if (conf.getString(s+".marriage.gender")!=null)
				gender.put(u, conf.getString(s+".marriage.gender"));
			if (conf.getString(s+".marriage.partner")!=null)
				marriages.put(u, UUID.fromString(conf.getString(s+".marriage.partner")));
			if (conf.getString(s+".marriage.time")!=null)
				timeOfMarriage.put(u, conf.getLong(s+".marriage.time"));
			Files.data.set("users."+s+".marriage", null);
		}
		try {
			Files.data.save(Files.dataFile);
		}catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static void save() {
		if (gender.size()>0) {
			for (UUID u : gender.keySet()) {
				Files.data.set("users."+u.toString()+".marriage.gender", gender.get(u));
			}
		}
		if (marriages.size()>0) {
			for (UUID u : marriages.keySet()) {
				Files.data.set("users."+u.toString()+".marriage.partner", marriages.get(u).toString());
			}
		}
		if (timeOfMarriage.size()>0) {
			for (UUID u : timeOfMarriage.keySet()) {
				Files.data.set("users."+u.toString()+".marriage.time", timeOfMarriage.get(u));
			}
		}
		try {
			Files.data.save(Files.dataFile);
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender s, Command cmd, String string, String[] args) {
		if (args.length > 0) {
			if (s instanceof Player) {
				Player p = (Player) s;
				UUID u = p.getUniqueId();
				if (args[0].equalsIgnoreCase("gender")) { // Marry Gender <Gender>
					if (args.length >= 2) {
						String g = args[1];
						if (g.equalsIgnoreCase("male")) {
							gender.put(u, "Male");
							Core.message(SM.prefix + "You set your gender to &bMale&f.", p);
						} else if (g.equalsIgnoreCase("female")) {
							Core.message(SM.prefix + "You set your gender to &dFemale&f.", p);
							gender.put(u, "Female");
						} else {
							Core.message(SM.prefix + "Sorry, but &c" + g + "&f isn't a gender.", p);
						}
					} else {
						Core.message(SM.prefix + "Usage&e /Marry Gender <Gender - Male/Female>", s);
					}
					return true;
				}
				if (args[0].equalsIgnoreCase("propose")) { // Mary Propose <Player>
					if (args.length >= 2) {
						Player pl = Bukkit.getPlayer(args[1]);
						if (pl != null) {
							if (!p.equals(pl)) {
								if (!isMarried(p)) {
									if (!isMarried(pl)) {
										propose(p, pl);
									} else {
										Core.message(
												SM.prefix + "Sorry, but &c" + pl.getName() + "&f is already married to "
														+ getGenderColor(Bukkit.getOfflinePlayer(getPartner(pl)))
														+ Bukkit.getOfflinePlayer(getPartner(pl)).getName() + "&f.",
												s);
									}
								} else {
									Core.message(SM.prefix + "Sorry, but you must be loyal to "
											+ getGenderColor(Bukkit.getOfflinePlayer(getPartner(p)))
											+ Bukkit.getOfflinePlayer(getPartner(p)).getName(), s);
								}
							} else {
								Core.message(SM.prefix + "Did you really try and marry yourself..? How lonely are you?",
										s);
							}
						} else {
							Core.message(SM.prefix + "Sorry, but &c" + args[1] + "&f is an invalid player.", s);
						}
					} else {
						Core.message(SM.prefix + "Usage&e /Marry Propose <Player>", s);
					}
					return true;
				}
				if (args[0].equalsIgnoreCase("accept")) {// Mary Accept <Player>
					if (proposals.containsKey(u)) {
						if (args.length >= 2) {
							Player pl = Bukkit.getPlayer(args[1]);
							if (pl != null) {
								if (proposals.get(u).equals(pl.getUniqueId())) {
									if (!isMarried(p)) {
										if (!isMarried(pl)) {
											marry(p, pl);
										} else {
											Core.message(
													SM.prefix + "Sorry, but &c" + pl.getName()
															+ "&f is already married to "
															+ getGenderColor(Bukkit.getOfflinePlayer(getPartner(pl)))
															+ Bukkit.getOfflinePlayer(getPartner(pl)).getName() + "&f.",
													s);
										}
									} else {
										Core.message(SM.prefix + "Sorry, but you must be loyal to "
												+ getGenderColor(Bukkit.getOfflinePlayer(getPartner(p)))
												+ Bukkit.getOfflinePlayer(getPartner(p)).getName(), s);
									}
								} else {
									Core.message(
											SM.prefix + "Sorry, but &c" + pl.getName() + "&f has not proposed to you.",
											p);
								}
							} else {
								Core.message(SM.prefix + "Sorry, but &c" + args[1] + "&f is an invalid player.", s);
							}
						} else {
							Core.message(SM.prefix + "Usage&e /Marry Accept <Player>", s);
						}
					} else {
						Core.message(SM.prefix + "Sorry, but no one has recently proposed to you.", s);
					}
					return true;
				}
				if (args[0].equalsIgnoreCase("decline")) {// Marry Decline <Player>
					if (proposals.containsKey(u)) {
						if (args.length >= 2) {
							Player pl = Bukkit.getPlayer(args[1]);
							if (pl != null) {
								if (proposals.get(u).equals(pl.getUniqueId())) {
									proposals.remove(u);
									Core.broadcast(SM.prefix + getGenderColor(p) + p.getName() + "&f declined "
											+ getGenderColor(pl) + pl.getName() + "'s&f proposal. Sucks to be "
											+ getGenderColor(pl) + pl.getName());
								} else {
									Core.message(
											SM.prefix + "Sorry, but &c" + pl.getName() + "&f has not proposed to you.",
											p);
								}
							} else {
								Core.message(SM.prefix + "Sorry, but &c" + args[1] + "&f is an invalid player.", s);
							}
						} else {
							Core.message(SM.prefix + "Usage&e /Marry Decline <Player>", s);
						}
					} else {
						Core.message(SM.prefix + "Sorry, but no one has recently proposed to you.", s);
					}
					return true;
				}
				if (args[0].equalsIgnoreCase("divorce")) {
					if (isMarried(p)) {
						if (divorceConfirm.contains(p)) {
							divorceConfirm.remove(p);
							divorce(p);
						} else {
							divorceConfirm.add(p);
							Core.message(SM.prefix + "To confirm your dirorce with "
									+ Bukkit.getOfflinePlayer(getPartner(p)).getName()
									+ "&f, execute the command again.", s);
							new BukkitRunnable() {
								public void run() {
									if (divorceConfirm.contains(p)) {
										divorceConfirm.remove(p);
									}
								}
							}.runTaskLaterAsynchronously(SM.instance, 200);
						}
					} else {
						Core.message(SM.prefix + "Sorry, but you're not married.", p);
					}
					return true;
				}
				if (args[0].equalsIgnoreCase("Status")) { // Marry Status (Player)
					if (args.length >= 2) {
						OfflinePlayer pl = Bukkit.getOfflinePlayer(args[1]);
						if (pl.hasPlayedBefore()) {
							if (isMarried(pl)) {
								OfflinePlayer pa = Bukkit.getOfflinePlayer(getPartner(pl));
								Core.message(SM.prefix + getGenderColor(pl) + pl.getName() + "&f has been married to "
										+ getGenderColor(pa) + pa.getName() + "&f for &a"
										+ Core.time((int) ((System.currentTimeMillis()
												- timeOfMarriage.get(p.getUniqueId())) / 1000))
										+ "&f.", p);
							} else {
								Core.message(SM.prefix + getGenderColor(pl) + pl.getName()+"&f isn't married.", p);
							}
						} else {
							Core.message(SM.prefix + "Sorry, but &c" + args[1] + "&f is an invalid player.", s);
						}
					} else {
						if (isMarried(p)) {
							OfflinePlayer pa = Bukkit.getOfflinePlayer(getPartner(p));
							Core.message(SM.prefix + "You've been married to " + getGenderColor(pa) + pa.getName()
									+ "&f for &a"
									+ Core.time(
											(int) ((System.currentTimeMillis() - timeOfMarriage.get(p.getUniqueId()))
													/ 1000))
									+ "&f.", p);
						} else {
							Core.message(SM.prefix + "You're not married.", p);
						}
					}
					return true;
				}
			}
		}
		Core.message("&3 -&e /Marry&3 &l»&f View the help list.", s);
		Core.message("&3 -&e /Marry Gender&3 &l»&f Set your gender.", s);
		Core.message("&3 -&e /Marry Status &3 &l»&f View someone's marital status.", s);
		Core.message("&3 -&e /Marry Propose &3 &l»&f Propose to someone.", s);
		Core.message("&3 -&e /Marry Accept &3 &l»&f Accept a proposal.", s);
		Core.message("&3 -&e /Marry Decline &3 &l»&f Decline a proposal.", s);
		Core.message("&3 -&e /Marry Divorce &3 &l»&f End your marriage.", s);
		Core.message("&3 &l * &fDeveloped  by &6Askingg&f.", s);
		return true;

	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		UUID u = e.getPlayer().getUniqueId();
		if (!gender.containsKey(u)) {
			gender.put(u, "&f");
		}
	}

	public static void propose(Player p, Player p2) {
		UUID u = p.getUniqueId();
		UUID u2 = p2.getUniqueId();
		proposals.put(u2, u);
		Core.broadcast(SM.prefix + getGenderColor(p) + p.getName() + "&f proposed to " + getGenderColor(p2)
				+ p2.getName() + "&f.");
		new BukkitRunnable() {
			public void run() {
				if (proposals.containsKey(u2)) {
					proposals.remove(u2);
					Core.broadcast(SM.prefix + getGenderColor(p2) + p2.getName() + " ignored " + getGenderColor(p)
							+ p.getName() + "'s&f proposal.");
				}
			}
		}.runTaskLaterAsynchronously(SM.instance, 30 * 20);
	}

	public static void marry(OfflinePlayer p, OfflinePlayer p2) {
		UUID u = p.getUniqueId();
		UUID u2 = p2.getUniqueId();
		marriages.put(u, u2);
		marriages.put(u2, u);
		timeOfMarriage.put(u, System.currentTimeMillis());
		timeOfMarriage.put(u2, System.currentTimeMillis());
		proposals.remove(u2);
		Core.broadcast(SM.prefix + getGenderColor(p) + p.getName() + "&f and " + getGenderColor(p2) + p2.getName()
				+ "&f are now married.");
	}

	public static void divorce(OfflinePlayer p) {
		OfflinePlayer p2 = Bukkit.getOfflinePlayer(marriages.get(p.getUniqueId()));
		marriages.remove(p.getUniqueId());
		marriages.remove(p2.getUniqueId());
		timeOfMarriage.remove(p.getUniqueId());
		timeOfMarriage.remove(p2.getUniqueId());
		Core.broadcast(SM.prefix + getGenderColor(p) + p.getName() + "&f and " + getGenderColor(p2) + p2.getName()
				+ "&f divorced.");
	}

	public static boolean isMarried(OfflinePlayer p) {
		if (marriages.containsKey(p.getUniqueId()))
			return true;
		return false;
	}

	public static UUID getPartner(OfflinePlayer p) {
		return marriages.get(p.getUniqueId());
	}

	public static String getGender(OfflinePlayer p) {
		return gender.get(p.getUniqueId());
	}

	public static String getGenderColor(OfflinePlayer p) {
		String g = gender.get(p.getUniqueId());
		if (g.equalsIgnoreCase("Male"))
			return "&b";
		if (g.equalsIgnoreCase("Female"))
			return "&d";
		return "&f";
	}
}
