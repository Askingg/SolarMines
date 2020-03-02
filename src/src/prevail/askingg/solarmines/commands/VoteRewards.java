package prevail.askingg.solarmines.commands;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import prevail.askingg.solarmines.crates.Crates;
import prevail.askingg.solarmines.enchanting.Tokens;
import prevail.askingg.solarmines.main.Core;
import prevail.askingg.solarmines.main.SM;

public class VoteRewards implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String string, String[] args) {
		if (s instanceof ConsoleCommandSender || s.hasPermission("solar.vote.rewards")) {
			if (args.length > 0) {
				Player p = Bukkit.getPlayer(args[0]);
				if (p != null) {
					if (args.length > 1) {
						int l = 0;
						try {
							l = Integer.parseInt(args[1]);
						} catch (Exception ex) {
							Core.message(SM.prefix + "Sorry, but &c" + args[1] + "&f is an invalid integer.", s);
							return true;
						}
						if (l < 1)
							l = 1;
						if (l > 5)
							l = 5;
						rewards(p, l);
					} else {
						rewards(p, 0);
					}
				} else {
					Core.message(SM.prefix + "Sorry, but &c" + args[0] + "&f is an invalid player.", s);
				}
			} else {
				Core.message(SM.prefix + "Usage&3 &l»&e /VoteRewards <Player> (luck)", s);
			}
		} else {
			Core.message(SM.prefix + "Sorry, but you don't have permission to do that.", s);
		}
		return true;
	}

	public static void rewards(Player p, int l) {
		Random rand = new Random();
		if (l < 1) {
			double r = rand.nextDouble();
			if (r < 0.05) {
				l = 5;
			} else if (r < 0.01) {
				l = 4;
			} else if (r < 0.25) {
				l = 3;
			} else if (r < 0.5) {
				l = 2;
			} else {
				l = 1;
			}
		}
		int tokens = 0;
		double money = 0;
		int vote = 0;
		int common = 0;
		int rare = 0;
		int epic = 0;
		if (l == 1) {
			tokens = 1;
			money = 1000;
			vote = 1;
		}
		if (l == 2) {
			tokens = 3;
			money = 5000;
			vote = 2;
			common = 1;
		}
		if (l == 3) {
			tokens = 8;
			money = 15000;
			vote = 2;
			common = 1;
		}
		if (l == 4) {
			tokens = 25;
			money = 50000;
			vote = 3;
			common = 2;
			rare = 1;
		}
		if (l == 5) {
			tokens = 25;
			money = 50000;
			vote = 3;
			common = 2;
			rare = 1;
			epic = 1;
		}
		boolean donor = false;
		int d = 0;
		String[] groups = SM.perms.getPlayerGroups(p);
		for (String g : groups) {
			if (g.equalsIgnoreCase("Mercury") || g.equalsIgnoreCase("Venus") || g.equalsIgnoreCase("Earth")
					|| g.equalsIgnoreCase("Mars") || g.equalsIgnoreCase("Jupiter") || g.equalsIgnoreCase("Saturn")
					|| g.equalsIgnoreCase("Uranus") || g.equalsIgnoreCase("Neptune")) {
				donor = true;
				double r = rand.nextDouble();
				if (r < 0.1) {
					d = 5;
					tokens = tokens * 4;
					money = money * 4;
					vote = vote * 4;
					common = common * 3;
					rare = rare * 3;
					epic = epic * 3;
				} else if (r < 0.2) {
					d = 4;
					tokens = tokens * 3;
					money = money * 3;
					vote = vote * 3;
					common = common * 2;
					rare = rare * 2;
					epic = epic * 2;
				} else if (r < 0.3) {
					d = 3;
					tokens = tokens * 2;
					money = money * 2;
					vote = vote * 2;
				} else if (r < 0.3) {
					d = 2;
					tokens += tokens / 2;
					money += money / 2;
				} else {
					d = 1;
				}
				break;
			}
		}
		money = CrateMoney.getCrateMoney(p, money);
		SM.eco.depositPlayer(p, d);
		Tokens.addNoMsg(p, tokens);
		if (p.getInventory().firstEmpty() != -1) {
			p.getInventory().addItem(Crates.key("Vote", vote));
		} else {
			p.getWorld().dropItem(p.getEyeLocation(), Crates.key("Vote", vote));
		}
		if (common > 0) {
			if (p.getInventory().firstEmpty() != -1) {
				p.getInventory().addItem(Crates.key("Common", common));
			} else {
				p.getWorld().dropItem(p.getEyeLocation(), Crates.key("Common", common));
			}
		}
		if (rare > 0) {
			if (p.getInventory().firstEmpty() != -1) {
				p.getInventory().addItem(Crates.key("Rare", rare));
			} else {
				p.getWorld().dropItem(p.getEyeLocation(), Crates.key("Rare", rare));
			}
		}
		if (epic > 0) {
			if (p.getInventory().firstEmpty() != -1) {
				p.getInventory().addItem(Crates.key("Epic", epic));
			} else {
				p.getWorld().dropItem(p.getEyeLocation(), Crates.key("Epic", epic));
			}
		}
		p.updateInventory();
		String m = "&6&lVote&e&lRewards&8 &l>> &6" + p.getName() + "&f voted&8 (&e/Vote&f.&8)&3 &l»";
		m += "\n&3 -&f Luck&3 &l»&f " + luck(l, "&a");
		if (donor)
			m += "\n&3 -&f Donator Luck&3 &l»&f " + luck(d, "&c");
		m += "\n&3 -&f Money&3 &l»&a $" + Core.number(money);
		m += "\n&3 -&f Tokens&3 &l»&b " + Core.decimals(0, tokens);
		m += "\n&3 -&f Vote Keys&3 &l»&d " + vote;
		if (common > 0)
			m += "\n&3 -&f Common Keys&3 &l»&a " + common;
		if (rare > 0)
			m += "\n&3 -&f Rare Keys&3 &l»&c " + rare;
		if (epic > 0)
			m += "\n&3 -&f Epic Keys&3 &l»&b " + epic;
		Core.broadcast(m);
	}

	private static String luck(int l, String c) {
		String s = "";
		for (int x = 0; x < 5; x++) {
			if (x < l) {
				s += c + "&l❘";
			} else {
				s += "&7&l❘";
			}
		}
		return s;
	}
}
