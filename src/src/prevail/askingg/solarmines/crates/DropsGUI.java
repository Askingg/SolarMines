package prevail.askingg.solarmines.crates;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import prevail.askingg.solarmines.main.Core;
import prevail.askingg.solarmines.main.SM;

public class DropsGUI implements Listener {

	public static List<Player> open = new ArrayList<Player>();

	public static Inventory menu(Player p, String c) {
		Inventory inv = Bukkit.createInventory(null, ((Crates.drops.get(c).size() / 9) * 9) + 18,
				Core.color(Crates.crateColor.get(c) + c + " Drops"));
		ItemStack i = new ItemStack(Material.STAINED_GLASS_PANE);
		i.setDurability((byte) 7);
		ItemMeta m = i.getItemMeta();
		m.setDisplayName(Core.color("&7"));
		i.setItemMeta(m);
		inv.setItem(0, i);
		inv.setItem(1, i);
		inv.setItem(2, i);
		inv.setItem(3, i);
		inv.setItem(5, i);
		inv.setItem(6, i);
		inv.setItem(7, i);
		inv.setItem(8, i);
		i = new ItemStack(Material.SIGN);
		m = i.getItemMeta();
		m.setDisplayName(Core.color("&aInformation"));
		List<String> l = new ArrayList<String>();
		l.add(Core.color("&7"));
		l.add(Core.color("&a Rarities&3 &l»"));
		l.add(Core.color("&3&l »&f Common&3 &l»&b chance >= 25"));
		l.add(Core.color("&3&l »&d Uncommon&3 &l»&b 25 > chance >= 15"));
		l.add(Core.color("&3&l »&a Rare&3 &l»&b 15 > chance >= 6"));
		l.add(Core.color("&3&l »&b Epic&3 &l»&b 6 > chance >= 2"));
		l.add(Core.color("&3&l »&c Legendary&3 &l»&b 2 > chance >= 0.4"));
		l.add(Core.color("&3&l »&6 &oInf&e&oer&6&ona&e&ol&3 &l»&b 0.4 > chance"));
		l.add(Core.color("&7"));
		l.add(Core.color("&a Percentages&3 &l»"));
		l.add(Core.color("&3&l »&f All percentages shown are the real"));
		l.add(Core.color("&f &f &f &f chance (Out of 100) of that reward."));
		m.setLore(l);
		i.setItemMeta(m);
		inv.setItem(4, i);

		for (String s : Crates.drops.get(c)) {
			String id = c + ";" + s;
			i = new ItemStack(Crates.dropMaterial.get(id), Crates.dropAmount.get(id));
			i.setDurability((byte) (int) Crates.dropData.get(id));
			m = i.getItemMeta();
			m.setDisplayName(Core.color(Core.papi(p, Crates.dropName.get(id))));
			l.clear();
			for (String st : Crates.dropLore.get(id)) {
				l.add(Core.color(Core.papi(p, st.replace("%rarity%", Crates.realRarity.get(id)).replace("%percent%",
						Core.decimals(2, Crates.realChance.get(id))))));
			}
			m.setLore(l);
			i.setItemMeta(m);
			inv.addItem(i);
		}
		open.add(p);
		return inv;
	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (open.contains((Player) e.getWhoClicked()))
			e.setCancelled(true);
	}

	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		Player p = (Player) e.getPlayer();
		if (open.contains(p)) {
			new BukkitRunnable() {
				public void run() {
					open.remove(p);
				}
			}.runTaskLaterAsynchronously(SM.instance, 2);
		}
	}
}
