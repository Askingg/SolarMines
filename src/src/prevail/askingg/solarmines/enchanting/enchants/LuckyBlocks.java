package prevail.askingg.solarmines.enchanting.enchants;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

import prevail.askingg.solarmines.crates.Crates;
import prevail.askingg.solarmines.enchanting.Tokens;
import prevail.askingg.solarmines.main.Core;
import prevail.askingg.solarmines.main.SM;

public class LuckyBlocks implements Listener, CommandExecutor {

	private static String prefix = "&6&lLucky&e&lBlock&8 &l>>&f ";

	public static void open(Player p, int l) {
		int x = new Random().nextInt(100) + 1;
		boolean full = p.getInventory().firstEmpty() == -1;
		if (l == 1) {
			if (x > 25) {
				Core.message(prefix + "Sorry, but that &6Lucky&eBlock&f was &cEmpty&f.", p);
			} else if (x > 15) {
				Tokens.addNoMsg(p, 1);
				Core.message(prefix + "You received &b1 Token&f.", p);
			} else if (x > 5) {
				if (full) {
					Core.message(prefix + "Your inventory is full!", p);
				} else {
					p.getInventory().addItem(Crates.key("Common"));
					Core.message(prefix + "You received a Common Key.", p);
				}
			} else {
				Tokens.addNoMsg(p, 2);
				Core.message(prefix + "You received &b2 Tokens&f.", p);
			}
			p.updateInventory();
			return;
		} else if (l == 2) {
			if (x > 30) {
				Core.message(prefix + "Sorry, but that &6Lucky&eBlock&f was &cEmpty&f.", p);
			} else if (x > 18) {
				Tokens.addNoMsg(p, 1);
				Core.message(prefix + "You received &b1 Token&f.", p);
			} else if (x > 6) {
				if (full) {
					Core.message(prefix + "Your inventory is full!", p);
				} else {
					p.getInventory().addItem(Crates.key("Common"));
					Core.message(prefix + "You received a Common Key.", p);
				}
			} else {
				Tokens.addNoMsg(p, 2);
				Core.message(prefix + "You received &b2 Tokens&f.", p);
			}
			p.updateInventory();
			return;
		} else if (l == 3) {
			if (x > 35) {
				Core.message(prefix + "Sorry, but that &6Lucky&eBlock&f was &cEmpty&f.", p);
			} else if (x > 21) {
				Tokens.addNoMsg(p, 1);
				Core.message(prefix + "You received &b1 Token&f.", p);
			} else if (x > 7) {
				if (full) {
					Core.message(prefix + "Your inventory is full!", p);
				} else {
					p.getInventory().addItem(Crates.key("Common"));
					Core.message(prefix + "You received a Common Key.", p);
				}
			} else {
				Tokens.addNoMsg(p, 2);
				Core.message(prefix + "You received &b2 Tokens&f.", p);
			}
			p.updateInventory();
			return;
		} else if (l == 4) {
			if (x > 40) {
				Core.message(prefix + "Sorry, but that &6Lucky&eBlock&f was &cEmpty&f.", p);
			} else if (x > 25) {
				Tokens.addNoMsg(p, 1);
				Core.message(prefix + "You received &b1 Token&f.", p);
			} else if (x > 10) {
				if (full) {
					Core.message(prefix + "Your inventory is full!", p);
				} else {
					p.getInventory().addItem(Crates.key("Common"));
					Core.message(prefix + "You received a Common Key.", p);
				}
			} else if (x > 2) {
				Tokens.addNoMsg(p, 2);
				Core.message(prefix + "You received &b2 Tokens&f.", p);
			} else {
				Tokens.addNoMsg(p, 4);
				Core.message(prefix + "You received &b4 Tokens&f.", p);
			}
			p.updateInventory();
			return;
		} else if (l == 5) {
			if (x > 45) {
				Core.message(prefix + "Sorry, but that &6Lucky&eBlock&f was &cEmpty&f.", p);
			} else if (x > 29) {
				Tokens.addNoMsg(p, 1);
				Core.message(prefix + "You received &b1 Token&f.", p);
			} else if (x > 13) {
				if (full) {
					Core.message(prefix + "Your inventory is full!", p);
				} else {
					p.getInventory().addItem(Crates.key("Common"));
					Core.message(prefix + "You received a Common Key.", p);
				}
			} else if (x > 3) {
				Tokens.addNoMsg(p, 2);
				Core.message(prefix + "You received &b2 Tokens&f.", p);
			} else {
				Tokens.addNoMsg(p, 4);
				Core.message(prefix + "You received &b4 Tokens&f.", p);
			}
			p.updateInventory();
			return;
		} else if (l == 6) {
			if (x > 50) {
				Core.message(prefix + "Sorry, but that &6Lucky&eBlock&f was &cEmpty&f.", p);
			} else if (x > 33) {
				Tokens.addNoMsg(p, 1);
				Core.message(prefix + "You received &b1 Token&f.", p);
			} else if (x > 16) {
				if (full) {
					Core.message(prefix + "Your inventory is full!", p);
				} else {
					p.getInventory().addItem(Crates.key("Common"));
					Core.message(prefix + "You received a Common Key.", p);
				}
			} else if (x > 4) {
				Tokens.addNoMsg(p, 2);
				Core.message(prefix + "You received &b2 Tokens&f.", p);
			} else {
				Tokens.addNoMsg(p, 4);
				Core.message(prefix + "You received &b4 Tokens&f.", p);
			}
			p.updateInventory();
			return;
		} else if (l == 7) {
			if (x > 50) {
				Core.message(prefix + "Sorry, but that &6Lucky&eBlock&f was &cEmpty&f.", p);
			} else if (x > 28) {
				if (full) {
					Core.message(prefix + "Your inventory is full!", p);
				} else {
					p.getInventory().addItem(Crates.key("Common"));
					Core.message(prefix + "You received a Common Key.", p);
				}
			} else if (x > 14) {
				Tokens.addNoMsg(p, 2);
				Core.message(prefix + "You received &b2 Tokens&f.", p);
			} else {
				Tokens.addNoMsg(p, 4);
				Core.message(prefix + "You received &b4 Tokens&f.", p);
			}
			p.updateInventory();
			return;
		} else if (l == 8) {
			if (x > 50) {
				Core.message(prefix + "Sorry, but that &6Lucky&eBlock&f was &cEmpty&f.", p);
			} else if (x > 20) {
				if (full) {
					Core.message(prefix + "Your inventory is full!", p);
				} else {
					p.getInventory().addItem(Crates.key("Common"));
					Core.message(prefix + "You received a Common Key.", p);
				}
			} else if (x > 2) {
				Tokens.addNoMsg(p, 4);
				Core.message(prefix + "You received &b4 Tokens&f.", p);
			} else {
				if (full) {
					Core.message(prefix + "Your inventory is full!", p);
				} else {
					p.getInventory().addItem(Crates.key("Rare"));
					Core.message(prefix + "You received a &aRare Key&f.", p);
				}
			}
			p.updateInventory();
			return;
		} else if (l == 9) {
			if (x > 50) {
				Core.message(prefix + "Sorry, but that &6Lucky&eBlock&f was &cEmpty&f.", p);
			} else if (x > 10) {
				if (full) {
					Core.message(prefix + "Your inventory is full!", p);
				} else {
					p.getInventory().addItem(Crates.key("Common"));
					Core.message(prefix + "You received a Common Key.", p);
				}
			} else if (x > 6) {
				if (full) {
					Core.message(prefix + "Your inventory is full!", p);
				} else {
					p.getInventory().addItem(Crates.key("Rare"));
					Core.message(prefix + "You received a &aRare Key&f.", p);
				}
			} else if (x > 2) {
				Tokens.addNoMsg(p, 6);
				Core.message(prefix + "You received &b6 Tokens&f.", p);
			} else {
				Tokens.addNoMsg(p, 10);
				Core.message(prefix + "You received &b10 Tokens&f.", p);
			}
			p.updateInventory();
			return;
		} else if (l == 10) {
			if (x > 50) {
				Core.message(prefix + "Sorry, but that &6Lucky&eBlock&f was &cEmpty&f.", p);
			} else if (x > 10) {
				if (full) {
					Core.message(prefix + "Your inventory is full!", p);
				} else {
					p.getInventory().addItem(Crates.key("Common"));
					Core.message(prefix + "You received a Common Key.", p);
				}
			} else if (x > 3) {
				if (full) {
					Core.message(prefix + "Your inventory is full!", p);
				} else {
					p.getInventory().addItem(Crates.key("Rare"));
					Core.message(prefix + "You received a &aRare Key&f.", p);
				}
			} else {
				Tokens.addNoMsg(p, 10);
				Core.message(prefix + "You received &b10 Tokens&f.", p);
			}
			p.updateInventory();
			return;
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////////

	public static List<Player> open = new ArrayList<Player>();

	public static Inventory menu() {
		Inventory inv = Bukkit.createInventory(null, 36, Core.color("&6&lLucky&e&lBlock&8 &l>>&a Levels"));
		ItemStack i = new ItemStack(Material.SPONGE);
		ItemMeta m = i.getItemMeta();
		List<String> l = new ArrayList<String>();
		m.setDisplayName(Core.color("&aLevel 1"));
		l.add(Core.color("&7"));
		l.add(Core.color("&3 &l»&c Nothing&8 (&a75%&8)"));
		l.add(Core.color("&3 &l»&b 1 Token&8 (&a10%&8)"));
		l.add(Core.color("&3 &l»&f Common Key&8 (&a10%&8)"));
		l.add(Core.color("&3 &l»&b 2 Tokens&8 (&a5%&8)"));
		m.setLore(l);
		i.setItemMeta(m);
		inv.setItem(11, i);
		i = new ItemStack(Material.SPONGE);
		m = i.getItemMeta();
		l.clear();
		m.setDisplayName(Core.color("&aLevel 2"));
		l.add(Core.color("&7"));
		l.add(Core.color("&3 &l»&c Nothing&8 (&a70%&8)"));
		l.add(Core.color("&3 &l»&b 1 Token&8 (&a12%&8)"));
		l.add(Core.color("&3 &l»&f Common Key&8 (&a12%&8)"));
		l.add(Core.color("&3 &l»&b 2 Tokens&8 (&a6%&8)"));
		m.setLore(l);
		i.setItemMeta(m);
		inv.setItem(12, i);
		i = new ItemStack(Material.SPONGE);
		m = i.getItemMeta();
		l.clear();
		m.setDisplayName(Core.color("&aLevel 3"));
		l.add(Core.color("&7"));
		l.add(Core.color("&3 &l»&c Nothing&8 (&a65%&8)"));
		l.add(Core.color("&3 &l»&b 1 Token&8 (&a14%&8)"));
		l.add(Core.color("&3 &l»&f Common Key&8 (&a14%&8)"));
		l.add(Core.color("&3 &l»&b 2 Tokens&8 (&a7%&8)"));
		m.setLore(l);
		i.setItemMeta(m);
		inv.setItem(13, i);
		i = new ItemStack(Material.SPONGE);
		m = i.getItemMeta();
		l.clear();
		m.setDisplayName(Core.color("&aLevel 4"));
		l.add(Core.color("&7"));
		l.add(Core.color("&3 &l»&c Nothing&8 (&a60%&8)"));
		l.add(Core.color("&3 &l»&b 1 Token&8 (&a15%&8)"));
		l.add(Core.color("&3 &l»&f Common Key&8 (&a15%&8)"));
		l.add(Core.color("&3 &l»&b 2 Tokens&8 (&a8%&8)"));
		l.add(Core.color("&3 &l»&b 4 Tokens&8 (&a2%&8)"));
		m.setLore(l);
		i.setItemMeta(m);
		inv.setItem(14, i);
		i = new ItemStack(Material.SPONGE);
		m = i.getItemMeta();
		l.clear();
		m.setDisplayName(Core.color("&aLevel 5"));
		l.add(Core.color("&7"));
		l.add(Core.color("&3 &l»&c Nothing&8 (&a55%&8)"));
		l.add(Core.color("&3 &l»&b 1 Token&8 (&a16%&8)"));
		l.add(Core.color("&3 &l»&f Common Key&8 (&a16%&8)"));
		l.add(Core.color("&3 &l»&b 2 Tokens&8 (&a10%&8)"));
		l.add(Core.color("&3 &l»&b 4 Tokens&8 (&a3%&8)"));
		m.setLore(l);
		i.setItemMeta(m);
		inv.setItem(15, i);
		i = new ItemStack(Material.SPONGE);
		m = i.getItemMeta();
		l.clear();
		m.setDisplayName(Core.color("&aLevel 6"));
		l.add(Core.color("&7"));
		l.add(Core.color("&3 &l»&c Nothing&8 (&a50%&8)"));
		l.add(Core.color("&3 &l»&b 1 Token&8 (&a17%&8)"));
		l.add(Core.color("&3 &l»&f Common Key&8 (&a17%&8)"));
		l.add(Core.color("&3 &l»&b 2 Tokens&8 (&a12%&8)"));
		l.add(Core.color("&3 &l»&b 4 Tokens&8 (&a4%&8)"));
		m.setLore(l);
		i.setItemMeta(m);
		inv.setItem(20, i);
		i = new ItemStack(Material.SPONGE);
		m = i.getItemMeta();
		l.clear();
		m.setDisplayName(Core.color("&aLevel 7"));
		l.add(Core.color("&7"));
		l.add(Core.color("&3 &l»&c Nothing&8 (&a50%&8)"));
		l.add(Core.color("&3 &l»&f Common Key&8 (&a22%&8)"));
		l.add(Core.color("&3 &l»&b 4 Tokens&8 (&a14%&8)"));
		l.add(Core.color("&3 &l»&c Rare Key&8 (&a14%&8)"));
		m.setLore(l);
		i.setItemMeta(m);
		inv.setItem(21,i );
		i = new ItemStack(Material.SPONGE);
		m = i.getItemMeta();
		l.clear();
		m.setDisplayName(Core.color("&aLevel 8"));
		l.add(Core.color("&7"));
		l.add(Core.color("&3 &l»&c Nothing&8 (&a50%&8)"));
		l.add(Core.color("&3 &l»&f Common Key&8 (&a30%&8)"));
		l.add(Core.color("&3 &l»&b 4 Tokens&8 (&a18%&8)"));
		l.add(Core.color("&3 &l»&c Rare Key&8 (&a2%&8)"));
		m.setLore(l);
		i.setItemMeta(m);
		inv.setItem(22, i);
		i = new ItemStack(Material.SPONGE);
		m = i.getItemMeta();
		l.clear();
		m.setDisplayName(Core.color("&aLevel 9"));
		l.add(Core.color("&7"));
		l.add(Core.color("&3 &l»&c Nothing&8 (&a50%&8)"));
		l.add(Core.color("&3 &l»&f Common Key&8 (&a40%&8)"));
		l.add(Core.color("&3 &l»&c Rare Key&8 (&a4%&8)"));
		l.add(Core.color("&3 &l»&b 6 Tokens&8 (&a4%&8)"));
		l.add(Core.color("&3 &l»&b 10 Tokens&8 (&a2%&8)"));
		m.setLore(l);
		i.setItemMeta(m);
		inv.setItem(23, i);
		i = new ItemStack(Material.SPONGE);
		m = i.getItemMeta();
		l.clear();
		m.setDisplayName(Core.color("&aLevel 10"));
		l.add(Core.color("&7"));
		l.add(Core.color("&3 &l»&c Nothing&8 (&a50%&8)"));
		l.add(Core.color("&3 &l»&f Common Key&8 (&a40%&8)"));
		l.add(Core.color("&3 &l»&c Rare Key&8 (&a7%&8)"));
		l.add(Core.color("&3 &l»&b 10 Tokens&8 (&a3%&8)"));
		m.setLore(l);
		i.setItemMeta(m);
		inv.setItem(24, i);
		return inv;
	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		if (open.contains(p)) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		Player p = (Player) e.getPlayer();
		if (open.contains(p)) {
			new BukkitRunnable() {
				public void run() {
					open.remove(p);
				}
			}.runTaskLaterAsynchronously(SM.instance, 3);
		}
	}

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String string, String[] args) {
		if (s instanceof Player) {
			Player p = (Player)s;
			p.openInventory(menu());
			open.add(p);
		}
		return false;
	}
}
