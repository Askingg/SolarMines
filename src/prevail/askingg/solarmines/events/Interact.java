package prevail.askingg.solarmines.events;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import prevail.askingg.solarmines.enchanting.CE;
import prevail.askingg.solarmines.enchanting.CEGUI;
import prevail.askingg.solarmines.main.Core;
import prevail.askingg.solarmines.main.SM;
import prevail.askingg.solarmines.sell.Shops;

public class Interact implements Listener {

	public static HashMap<Player, Long> quicksell = new HashMap<Player, Long>();
	public static HashMap<Player, Long> enchant = new HashMap<Player, Long>();

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		ItemStack i = p.getItemInHand();
		if (i != null && i.getType().toString().contains("_PICKAXE")) {
			if (e.getAction().toString().contains("RIGHT_CLICK")) {
				if (!p.isSneaking()) {
					int qs = CE.getLevel(i, "QuickSell");
					if (qs == 0) {
						Core.message(SM.prefix + "Sorry, but you don't have the &eQuickSell&f enchantment.", p);
					} else {
						long delay = 10000 - (qs * 1000);
						if (quicksell.containsKey(p)) {
							if (System.currentTimeMillis() > quicksell.get(p)) {
								new Shops(p).sellall(p);
								quicksell.put(p, System.currentTimeMillis() + delay);
							} else {
								Core.message(SM.prefix + "Sorry, but you cannot use &eQuickSell&f for another &e"
										+ Core.time((int) ((quicksell.get(p) - System.currentTimeMillis()) / 1000) + 1),
										p);
							}
						} else {
							new Shops(p).sellall(p);
							quicksell.put(p, System.currentTimeMillis() + delay);
						}
					}
				} else {
					if (quicksell.containsKey(p)) {
						if (System.currentTimeMillis() > quicksell.get(p)) {
							p.openInventory(CEGUI.menu(p));
							CEGUI.open.add(p);
							enchant.put(p, System.currentTimeMillis() + 1000);
						}
					} else {
						p.openInventory(CEGUI.menu(p));
						CEGUI.open.add(p);
						enchant.put(p, System.currentTimeMillis() + 1000);
					}
				}
			}
		}
	}
}
