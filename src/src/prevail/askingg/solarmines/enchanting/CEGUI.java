package prevail.askingg.solarmines.enchanting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;
import prevail.askingg.solarmines.main.Core;
import prevail.askingg.solarmines.main.SM;

public class CEGUI implements CommandExecutor, Listener {

	public static List<Player> open = new ArrayList<Player>();

	public static Inventory menu(Player p) {
		ItemStack item = p.getInventory().getItemInMainHand();
		Inventory inv = Bukkit.createInventory(null, 45, Core.color("&6&lEnchanting"));
		CE.pickaxeCheck(p, item);
		HashMap<String, Integer> enchants = CE.getEnchants(item);
		for (String s : CE.enchants) {
			if (CE.itemCheck(s, item)) {
				ItemStack i = new ItemStack(CE.guiMat.get(s));
				ItemMeta m = i.getItemMeta();
				List<String> l = new ArrayList<String>();
				m.setDisplayName(Core.color(CE.color.get(s) + s));
				l.add(Core.color("&f"));
				l.add(Core.color("&f&l*&7&m--------------------&f&l*"));
				if (enchants.containsKey(s)) {
					l.add(Core.color("&3 &l »&f Level&3 &l»&6 " + enchants.get(s)));
					if (CE.getMax(s) != Integer.MAX_VALUE)
						l.add(Core.color("&3 &l »&f Max&3 &l»&e " + CE.getMax(s)));
					l.add(Core.color("&f"));
					if (CE.cost.containsKey(s)) {
						l.add(Core.color("&3 &l »&f Cost&3 &l»&b " + CE.getCost(s, enchants.get(s))));
					} else if (CE.prestigeCost.containsKey(s)) {
						l.add(Core.color("&3 &l »&f Cost&3 &l»&c " + CE.getCost(s, enchants.get(s))));
					}
				} else {
					l.add(Core.color("&3 &l »&f Level&3 &l»&6 0"));
					if (CE.getMax(s) != Integer.MAX_VALUE)
						l.add(Core.color("&3 &l »&f Max&3 &l»&e " + CE.getMax(s)));
					l.add(Core.color("&f"));
					if (CE.cost.containsKey(s)) {
						l.add(Core.color("&3 &l »&f Cost&3 &l»&b " + CE.getCost(s, 0)));
					} else if (CE.prestigeCost.containsKey(s)) {
						l.add(Core.color("&3 &l »&f Cost&3 &l»&c " + CE.getCost(s, 0)));
					}
				}
				l.add(Core.color("&f"));
				for (String st : CE.guiDesc.get(s)) {
					l.add(Core.color("&7 &o " + st));
				}
				l.add(Core.color("&f&l*&7&m--------------------&f&l*"));
				m.setLore(l);
				i.setItemMeta(m);
				inv.setItem(CE.guiLocation.get(s), i);
			}
		}
		return inv;
	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		if (open.contains(p)) {
			e.setCancelled(true);
			if (e.getRawSlot() < 45) {
				ItemStack item = p.getInventory().getItemInMainHand();
				HashMap<String, Integer> enchants = CE.getEnchants(item);
				ItemStack ci = e.getCurrentItem();
				if (ci.getType() != Material.AIR) {
					String s = ChatColor.stripColor(ci.getItemMeta().getDisplayName());
					if (CE.enchants.contains(s)) {
						int lvl = 0;
						if (enchants.containsKey(s)) {
							lvl=enchants.get(s);
							if (enchants.get(s) >= CE.getMax(s)) {
								Core.message(
										SM.prefix + "Sorry, but " + CE.color.get(s) + s + "&f is maxed on this item.",
										p);
								return;
							}
						}
						double cost = CE.getCost(s, lvl);
						if (CE.cost.containsKey(s)) {
							if (Tokens.canAfford(p, cost)) {
								Tokens.withdraw(p, cost);
								Tokens.addSpent(p, item, (int) cost);
								CE.enchant(item, s, 1);
								p.getInventory().setItemInMainHand(item);
								p.updateInventory();
								Core.message(SM.prefix + "You purchased a level of " + CE.color.get(s) + s + "&f.", p);
							} else {
								Core.message(SM.prefix + "Sorry, but you cannot afford to purchase" + CE.color.get(s)
										+ s + "&f.", p);
							}
						} else if (CE.prestigeCost.containsKey(s)) {
							if (PrestigePoints.canAfford(p, cost)) {
								PrestigePoints.withdraw(p, cost);
								CE.enchant(item, s, 1);
								p.getInventory().setItemInMainHand(item);
								p.updateInventory();
								Core.message(SM.prefix + "You purchased a level of " + CE.color.get(s) + s + "&f.", p);
							} else {
								Core.message(SM.prefix + "Sorry, but you cannot afford to purchase" + CE.color.get(s)
										+ s + "&f.", p);
							}
						}
						ItemStack i = new ItemStack(CE.guiMat.get(s));
						ItemMeta m = i.getItemMeta();
						List<String> l = new ArrayList<String>();
						m.setDisplayName(Core.color(CE.color.get(s) + s));
						l.add(Core.color("&f"));
						l.add(Core.color("&f&l*&7&m------------------------------&f&l*"));
						if (enchants.containsKey(s)) {
							l.add(Core.color("&3 &l »&f Level&3 &l»&6 " + (enchants.get(s) + 1)));
							if (CE.getMax(s) != Integer.MAX_VALUE)
								l.add(Core.color("&3 &l »&f Max&3 &l»&e " + CE.getMax(s)));
							l.add(Core.color("&f"));
							if (CE.cost.containsKey(s)) {
								l.add(Core.color("&3 &l »&f Cost&3 &l»&b " + CE.getCost(s, lvl)));
							} else if (CE.prestigeCost.containsKey(s)) {
								l.add(Core.color("&3 &l »&f Cost&3 &l»&c " + CE.getCost(s, lvl)));
							}
						} else {
							l.add(Core.color("&3 &l »&f Level&3 &l»&6 0"));
							if (CE.getMax(s) != Integer.MAX_VALUE)
								l.add(Core.color("&3 &l »&f Max&3 &l»&e " + CE.getMax(s)));
							l.add(Core.color("&f"));
							if (CE.cost.containsKey(s)) {
								l.add(Core.color("&3 &l »&f Cost&3 &l»&b " + CE.getCost(s, lvl)));
							} else if (CE.prestigeCost.containsKey(s)) {
								l.add(Core.color("&3 &l »&f Cost&3 &l»&c " + CE.getCost(s, lvl)));
							}
						}
						l.add(Core.color("&f"));
						for (String st : CE.guiDesc.get(s)) {
							l.add(Core.color("&7 &o " + st));
						}
						l.add(Core.color("&f&l*&7&m------------------------------&f&l*"));
						m.setLore(l);
						i.setItemMeta(m);
						e.getInventory().setItem(e.getRawSlot(), i);
					}
				}
			}
		}
	}

	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		Player p = (Player) e.getPlayer();
		if (open.contains(p)) {
			new BukkitRunnable() {
				@Override
				public void run() {
					open.remove(p);
				}
			}.runTaskLaterAsynchronously(SM.instance, 2);
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String string, String[] args) {
		if (s instanceof Player) {
			Player p = (Player) s;
			p.openInventory(menu(p));
			open.add(p);
		}
		return false;
	}

}
