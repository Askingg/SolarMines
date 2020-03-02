package prevail.askingg.solarmines.events;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import prevail.askingg.solarmines.crates.Crates;
import prevail.askingg.solarmines.crates.DropsGUI;
import prevail.askingg.solarmines.enchanting.CE;
import prevail.askingg.solarmines.enchanting.CEGUI;
import prevail.askingg.solarmines.enchanting.Tokens;
import prevail.askingg.solarmines.enchanting.enchants.LuckyBlocks;
import prevail.askingg.solarmines.main.Core;
import prevail.askingg.solarmines.main.SM;
import prevail.askingg.solarmines.sell.Booster;
import prevail.askingg.solarmines.sell.Sell;
import prevail.askingg.solarmines.sell.TempBoosters;

public class Interact implements Listener {

	public static HashMap<Player, Long> quicksell = new HashMap<Player, Long>();
	public static HashMap<Player, Long> enchant = new HashMap<Player, Long>();

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if (e.getHand().equals(EquipmentSlot.OFF_HAND))
			return;
		Player p = e.getPlayer();
		ItemStack i = p.getInventory().getItemInMainHand();
		HashMap<String, Integer> enchants = CE.getEnchants(i);
		if (i != null && i.getType().toString().contains("_PICKAXE")) {
			if (e.getAction().toString().contains("RIGHT_CLICK")) {
				Block b = e.getClickedBlock();
				if (b != null && b.getType() == Material.SPONGE) {
					int lb = 0;
					if (enchants.containsKey("LuckyBlocks"))
						lb = enchants.get("LuckyBlocks");
					if (lb == 0) {
						Core.message(SM.prefix + "Sorry, but you don't have &6Lucky&eBlocks&f.", p);
					} else {
						LuckyBlocks.open(p, lb);
						e.getClickedBlock().setType(Material.AIR);
					}
					return;
				}
				if (!p.isSneaking()) {
					int qs = 0;
					if (enchants.containsKey("QuickSell"))
						qs = enchants.get("QuickSell");
					if (qs == 0) {
						Core.message(SM.prefix + "Sorry, but you don't have &eQuickSell&f.", p);
					} else {
						long delay = 10000 - (qs * 2000);
						if (quicksell.containsKey(p)) {
							if (System.currentTimeMillis() > quicksell.get(p)) {
								new Sell(p).sellInventory();
								quicksell.put(p, System.currentTimeMillis() + delay);
							} else {
								Core.message(SM.prefix + "Sorry, but you cannot use &eQuickSell&f for another &e"
										+ Core.time((int) ((quicksell.get(p) - System.currentTimeMillis()) / 1000) + 1),
										p);
							}
						} else {
							new Sell(p).sellInventory();
							quicksell.put(p, System.currentTimeMillis() + delay);
						}
					}
				} else {
					if (quicksell.containsKey(p)) {
						if (System.currentTimeMillis() > quicksell.get(p)) {
							p.openInventory(CEGUI.menu(p));
							CEGUI.open.add(p);
							enchant.put(p, System.currentTimeMillis() + 2000);
						}
					} else {
						p.openInventory(CEGUI.menu(p));
						CEGUI.open.add(p);
						enchant.put(p, System.currentTimeMillis() + 2000);
					}
				}
			}
			if (e.getAction().toString().contains("LEFT")) {
				Block b = e.getClickedBlock();
				if (b != null && b.getType() == Material.SPONGE) {
					int lb = 0;
					if (enchants.containsKey("LuckyBlocks"))
						lb = enchants.get("LuckyBlocks");
					if (lb == 0) {
						Core.message(SM.prefix + "Sorry, but you don't have &6Lucky&eBlocks&f.", p);
					} else {
						LuckyBlocks.open(p, lb);
						e.getClickedBlock().setType(Material.AIR);
					}
					return;
				}
			}
		}
		if (TempBoosters.isBooster(i)) {
			UUID u = p.getUniqueId();
			double b = TempBoosters.getBoost(i);
			int d = TempBoosters.getDuration(i);
			if (TempBoosters.getType(i).equals("Personal")) {
				if (Booster.moneyBoost.containsKey(u)) {
					if (Booster.moneyBoost.get(u) == b) {
						Booster.moneyExpire.put(u, Booster.moneyExpire.get(u) + ((long) d * 1000L));
						Core.message(SM.prefix + "You added &b" + Core.time(d) + "&f to your &a" + Core.decimals(2, b)
								+ "&f booster.", p);
						if (i.getAmount() == 1) {
							p.setItemInHand(new ItemStack(Material.AIR));
						} else {
							i.setAmount(i.getAmount() - 1);
						}
						p.updateInventory();
					} else {
						Core.message(SM.prefix
								+ "Sorry, but but your current personal booster has a different boost to the one you're trying to use.",
								p);
					}
				} else {
					Booster.moneyBoost.put(u, b);
					Booster.moneyExpire.put(u, System.currentTimeMillis() + ((long) d * 1000L));
					Core.message(SM.prefix + "You applied a &a" + Core.decimals(2, b) + "&8 (&a" + Core.time(d)
							+ "&8)&f Booster.", p);
					if (i.getAmount() == 1) {
						p.setItemInHand(new ItemStack(Material.AIR));
					} else {
						i.setAmount(i.getAmount() - 1);
					}
					p.updateInventory();
				}
			} else {
				if (Booster.globalMoneyBoost > 0) {
					if (Booster.globalMoneyBoost == b) {
						Booster.globalMoneyExpire = Booster.globalMoneyExpire + ((long) d * 1000L);
						Core.broadcast("&f &l *&7&m----------------&a Booster&7 &m----------------&f&l*\n"
								+ "&3 &3 &3 &3 &l»&a " + Core.time(d) + "&f was added to the global &a"
								+ Core.decimals(2, b) + "x\n&l &l &l &f &f &f booster by &b" + p.getName() + "\n"
								+ "&f &l *&7&m----------------------------------------&f&l*");
						if (i.getAmount() == 1) {
							p.setItemInHand(new ItemStack(Material.AIR));
						} else {
							i.setAmount(i.getAmount() - 1);
						}
						p.updateInventory();
					} else {
						Core.message(SM.prefix
								+ "Sorry, but the global booster has a different boost to the one you're trying to use.",
								p);
					}
				} else {
					Booster.globalMoneyBoost = b;
					Booster.globalMoneyExpire = System.currentTimeMillis() + ((long) d * 1000L);
					Core.broadcast("&f &l *&7&m----------------&a Booster&7 &m----------------&f&l*\n"
							+ "&3 &3 &3 &3 &l»&f A global &a" + Core.decimals(2, b) + "x&8(&a" + Core.time(d)
							+ "&8)&f booster was" + "\n&l &l &l &f &f &f started by &b" + p.getName() + "\n"
							+ "&f &l *&7&m----------------------------------------&f&l*");
					if (i.getAmount() == 1) {
						p.setItemInHand(new ItemStack(Material.AIR));
					} else {
						i.setAmount(i.getAmount() - 1);
					}
					p.updateInventory();
				}
			}
		}
		if (e.getClickedBlock() != null && e.getClickedBlock().getType() == Material.CHEST) {
			if (Crates.isKey(i)) {
				if (e.getAction().toString().contains("RIGHT")) {
					String c = Crates.getType(i);
					int a = i.getAmount();
					if (p.isSneaking()) {
						Crates.open(p, c, a);
						i = new ItemStack(Material.AIR);
						p.setItemInHand(i);
					} else {
						Crates.open(p, c);
						if (a == 1) {
							i = new ItemStack(Material.AIR);
							p.setItemInHand(i);
						} else {
							i.setAmount(a - 1);
							p.setItemInHand(i);
						}
					}
					p.updateInventory();
					e.setCancelled(true);
				} else if (e.getAction().toString().contains("LEFT")) {
					p.openInventory(DropsGUI.menu(p, Crates.getType(i)));
				}
			}
		}
		if (Tokens.isTokenItem(i)) {
			if (p.isSneaking()) {
				int a = 0;
				for (int x = 0; x < 36; x++) {
					ItemStack is = p.getInventory().getItem(x);
					if (Tokens.isTokenItem(is)) {
						a += is.getAmount();
						p.getInventory().setItem(x, new ItemStack(Material.AIR));
					}
				}
				Tokens.addNoMsg(p, a);
				Core.message(SM.prefix + "You redeemed &b" + a + " Tokens&f.", p);
			} else {
				int a = i.getAmount();
				Tokens.addNoMsg(p, a);
				i = new ItemStack(Material.AIR);
				p.setItemInHand(i);
				Core.message(SM.prefix + "You redeemed &b" + a + " Tokens&f.", p);
			}
			p.updateInventory();
		}
	}
}
