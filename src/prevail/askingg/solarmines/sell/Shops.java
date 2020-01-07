package prevail.askingg.solarmines.sell;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import prevail.askingg.solarmines.main.Config;
import prevail.askingg.solarmines.main.Core;
import prevail.askingg.solarmines.main.SM;

public class Shops {

	private String shop;

	public Shops(String shop) {
		this.shop = shop;
	}

	public Shops(Player p) {
		for (String s : Config.shops) {
			if (p.hasPermission("solar.shops." + s)) {
				this.shop = s;
				break;
			}
		}
	}

	public String getHighestShop(Player p) {
		for (String s : Config.shops) {
			if (p.hasPermission("solar.shops." + s)) {
				this.shop = s;
				return s;
			}
		}
		return null;
	}

	public double getWorth(ItemStack i) {
		String s = i.getType().toString() + ";" + i.getDurability();
		if (Config.worth.containsKey(shop + ";" + s)) {
			return Config.worth.get(shop + ";" + s) * i.getAmount();
		}
		return -1;
	}

	public void sellall(Player p) {
		int items = 0;
		double sell = 0;
		PlayerInventory pi = p.getInventory();
		for (ItemStack i : pi.getContents()) {
			if (i != null) {
				double d = getWorth(i);
				if (d > 0) {
					items += i.getAmount();
					sell += d;
					pi.removeItem(i);
				}
			}
		}
		p.updateInventory();
		if (sell > 0) {
			SM.eco.depositPlayer(p, sell);
			Core.message(SM.prefix + "You sold &6" + Core.decimals(0, items) + "&f items, earning you &a$"
					+ Core.decimals(1, sell), p);
		} else {
			Core.message(SM.prefix + "Sorry, but you don't have any items to sell.", p);
		}
	}
}
