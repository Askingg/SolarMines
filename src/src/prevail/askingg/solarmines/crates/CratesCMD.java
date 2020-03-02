package prevail.askingg.solarmines.crates;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import prevail.askingg.solarmines.main.Core;
import prevail.askingg.solarmines.main.SM;

public class CratesCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String string, String[] args) {
		if (args.length > 0) {
			if (args[0].equalsIgnoreCase("give")) {// Crates Give <Player> <Type> <Amount>
				if (s instanceof ConsoleCommandSender || s.hasPermission("solar.crates.give")) {
					if (args.length > 2) {
						Player p = Bukkit.getPlayer(args[1]);
						if (p != null) {
							String c = args[2];
							boolean b = false;
							for (String cr : Crates.types) {
								if (cr.equalsIgnoreCase(c)) {
									c = cr;
									b = true;
								}
							}
							if (b) {
								int a = 1;
								if (args.length > 3) {
									try {
										a = Integer.parseInt(args[3]);
									} catch (Exception ex) {
										Core.message(
												SM.prefix + "Sorry, but &c" + args[3] + "&f is an invalid integer.", s);
										return true;
									}
									if (a < 1)
										a = 1;
								}
								p.getInventory().addItem(Crates.key(c, a));
								p.updateInventory();
								Core.message(SM.prefix + "You gave " + Crates.crateColor.get(c) + a + " " + c
										+ " Keys&f to &a" + p.getName() + "&f.", s);
								Core.message(SM.prefix + Crates.crateColor.get(c) + "+ " + a + " " + c + " Keys.", p);
							} else {
								Core.message(SM.prefix + "Sorry, but &c" + args[2] + "&f is an invalid crate type.", s);
							}
						} else {
							Core.message(SM.prefix + "Sorry, but &c" + args[1] + "&f is an invalid player.", s);
						}
					} else {
						Core.message(SM.prefix + "Usage&3 &l»&e /Crates Give <Player> <Type> (Amount)", s);
					}
				} else {
					Core.message(SM.prefix + "Sorry, but you don't have permission to do that.", s);
				}
				return true;
			}
			if (args[0].equalsIgnoreCase("list")) {
				if (s instanceof ConsoleCommandSender || s.hasPermission("solar.crates.list")) {
					Core.message(SM.prefix + "Crate list&3 &l", s);
					for (String c : Crates.types) {
						Core.message("&3 - " + Crates.crateColor.get(c) + c, s);
					}
				} else {
					Core.message(SM.prefix + "Sorry, but you don't have permission to do that.", s);
				}
				return true;
			}
			if (args[0].equalsIgnoreCase("drops") || args[0].equalsIgnoreCase("preview")) {// Crates Drops <Crate>
				if (s instanceof Player) {
					Player p = (Player) s;
					if (args.length > 1) {
						String c = args[1];
						for (String cr : Crates.types) {
							if (c.equalsIgnoreCase(cr)) {
								p.openInventory(DropsGUI.menu(p, cr));
								return true;
							}
						}
						Core.message(SM.prefix + "Sorry, but &c" + args[1] + "&f is an invalid create type.", s);
					} else {
						Core.message(SM.prefix + "Usage&3 &l»&e /Crates Drops <Crate>", s);
					}
				}
				return true;
			}
			if (args[0].equalsIgnoreCase("reload")) {
				if (s instanceof ConsoleCommandSender || s.hasPermission("solar.crates.reload")) {
					Crates.reload();
					Core.message(SM.prefix+"Crates have been reloaded.", s);
				}else {
					Core.message(SM.prefix+"Sorry, but you don't have permission to do that.", s);
				}
				return true;
			}
		}
		Core.message(SM.prefix + "Commands for &6Crates&3 &l»", s);
		Core.message("&3 - &e/Crates&3 &l»&f View the help list", s);
		Core.message("&3 - &e/Crates List&3 &l»&f List all crate types", s);
		Core.message("&3 - &e/Crates Give&3 &l»&f Give users crate keys", s);
		Core.message("&3 - &e/Crates Drops&3 &l»&f View a crate's drops", s);
		Core.message("&3 - &e/Crates Reload&3 &l»&f Reload the crates file", s);
		return true;
	}

}
