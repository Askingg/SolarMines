package prevail.askingg.solarmines.sell;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import prevail.askingg.solarmines.main.Config;
import prevail.askingg.solarmines.main.Core;
import prevail.askingg.solarmines.main.SM;

public class Sell {

	private Player player;
	private double modifier;
	private int highestSelllable;

	public Sell(Player p) {
		String[] g = SM.perms.getPlayerGroups(p);
		double modifier = 0;
		int highestSelllable = 0;
		for (int x = 0; x < g.length; x++) {
			String s = g[x];
			if (Config.modifier.containsKey(s) && Config.modifier.get(s) > modifier) {
				modifier = Config.modifier.get(s);
			}
			if (Config.highestSell.containsKey(s)
					&& Config.sellValue.get(Config.highestSell.get(s)) > highestSelllable) {
				highestSelllable = Config.sellValue.get(Config.highestSell.get(s));
			}
		}
		this.player = p;
		this.modifier = modifier;
		this.highestSelllable = highestSelllable;
	}

	public boolean canSell(String id) {
		if (Config.sellValue.containsKey(id) && Config.sellValue.get(id) <= this.highestSelllable) {
			return true;
		}
		return false;
	}

	public double getWorth(ItemStack i) {
		String s = i.getType().toString() + ";" + i.getDurability();
		if (Config.worth.containsKey(s)) {
			return Math.round((Config.worth.get(s) * this.modifier) * i.getAmount());
		}
		return -1;
	}

	public void sellInventory() {
		int items = 0;
		double sell = 0;
		PlayerInventory pi = player.getInventory();
		for (ItemStack i : pi.getContents()) {
			if (i != null) {
				double d = getWorth(i);
				if (d > 0 && canSell(i.getType().toString() + ";" + i.getDurability())) {
					items += i.getAmount();
					sell += d;
					pi.removeItem(i);
				}
			}
		}
		player.updateInventory();
		if (sell > 0) {
			double b = Booster.getMoneyBooster(player);
			if (b > 1.0) {
				sell = sell * b;
				SM.eco.depositPlayer(player, sell);
				Core.message(SM.prefix + "You sold &6" + Core.decimals(0, items) + "&f items, earning you &a$"
						+ Core.decimals(1, sell) + "&f.&8 (&b" + Core.decimals(2, b) + "x&8)", player);
			} else {
				SM.eco.depositPlayer(player, sell);
				Core.message(SM.prefix + "You sold &6" + Core.decimals(0, items) + "&f items, earning you &a$"
						+ Core.decimals(1, sell) + "&f.", player);
			}
		} else {
			Core.message(SM.prefix + "Sorry, but you don't have any items to sell.", player);
		}
	}

	public void sell(ItemStack i) {
		if (canSell(i.getType().toString() + ";" + i.getDurability())) {
			double d = getWorth(i);
			double b = Booster.getMoneyBooster(player);
			if (b > 1.0) {
				d = d * b;
			}
			SM.eco.depositPlayer(player, d);
		}
	}
}
