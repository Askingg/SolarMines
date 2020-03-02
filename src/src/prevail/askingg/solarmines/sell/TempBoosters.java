package prevail.askingg.solarmines.sell;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import prevail.askingg.solarmines.main.Core;
import prevail.askingg.solarmines.main.SM;

public class TempBoosters {

	public static ItemStack boosterItem(String type, Double boost, int seconds) {
		ItemStack i = new ItemStack(Material.MAGMA_CREAM);
		ItemMeta m = i.getItemMeta();
		m.setDisplayName(Core.color("&aMoney Booster"));
		List<String> l = new ArrayList<String>();
		l.add(Core.color("&7"));
		l.add(Core.color("&f Type&3 &l»&b " + type));
		l.add(Core.color("&f Boost&3 &l»&a " + Core.decimals(2, boost)));
		l.add(Core.color("&f Duration&3 &l»&a " + Core.time(seconds)));
		l.add(Core.color("&7"));
		l.add(Core.color("&7 &o RightClick"));
		m.setLore(l);
		i.setItemMeta(m);
		return i;
	}

	public static boolean isBooster(ItemStack i) {
		if (i != null && i.getType() == Material.MAGMA_CREAM && i.hasItemMeta()) {
			ItemMeta m = i.getItemMeta();
			if (m.hasDisplayName() && m.hasLore()) {
				List<String> l = m.getLore();
				if (m.getDisplayName().equals(Core.color("&aMoney Booster")) && l.size() == 6
						&& l.get(5).equals(Core.color("&7 &o RightClick"))) {
					return true;
				}
			}
		}
		return false;
	}

	public static String getType(ItemStack i) {
		return i.getItemMeta().getLore().get(1).split(Core.color("»&b "))[1];
	}
	
	public static double getBoost(ItemStack i) {
		return Double.valueOf(i.getItemMeta().getLore().get(2).split(Core.color("»&a "))[1]);
	}

	public static int getDuration(ItemStack i) {
		String[] str = i.getItemMeta().getLore().get(3).split(Core.color("»&a "))[1].split(" ");
		int x = 0;
		for (String s : str) {
			if (s.endsWith("s")) {
				x += Integer.valueOf(s.split("s")[0]);
			} else if (s.endsWith("m")) {
				x += Integer.valueOf(s.split("m")[0]) * 60;
			} else if (s.endsWith("h")) {
				x += Integer.valueOf(s.split("h")[0]) * 3600;
			} else if (s.endsWith("d")) {
				x += Integer.valueOf(s.split("d")[0]) * 86400;
			} else {
				Core.broadcast(
						SM.prefix + "&cInvalid timeframe &8(&4" + s + "&8)&c found. Please send this to Askingg.");
			}
		}
		return x;
	}
}