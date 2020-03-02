package prevail.askingg.solarmines.crates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;
import prevail.askingg.solarmines.main.Core;
import prevail.askingg.solarmines.main.Files;
import prevail.askingg.solarmines.main.SM;

public class Crates {

	public static List<String> types = new ArrayList<String>();
	public static HashMap<String, String> crateColor = new HashMap<String, String>();
	public static HashMap<String, Double> totalChance = new HashMap<String, Double>();
	public static HashMap<String, List<String>> drops = new HashMap<String, List<String>>();// Crate, list: crate;drop
	public static HashMap<String, Material> dropMaterial = new HashMap<String, Material>();// crate;drop, material
	public static HashMap<String, Integer> dropData = new HashMap<String, Integer>();// crate;drop, data
	public static HashMap<String, Integer> dropAmount = new HashMap<String, Integer>();// crate;drop, amount
	public static HashMap<String, String> dropName = new HashMap<String, String>();// crate;drop, name
	public static HashMap<String, List<String>> dropLore = new HashMap<String, List<String>>();// crate;drop, lore
	public static HashMap<String, List<String>> dropCommands = new HashMap<String, List<String>>();// crate;drop, //
																									// commandList
	public static HashMap<String, Double> dropChance = new HashMap<String, Double>();// crate;drop, chance
	public static HashMap<String, Double> realChance = new HashMap<String, Double>();// Crate;drop, chance
	public static HashMap<String, String> realRarity = new HashMap<String, String>();// crate;drop,rarity

	public static void setup() {
		for (String c : Files.reloadCrates().getConfigurationSection("").getKeys(false)) {
			ConfigurationSection crate = Files.crates.getConfigurationSection(c);
			types.add(c);
			crateColor.put(c, crate.getString("color"));
			ConfigurationSection dropse = crate.getConfigurationSection("drops");
			if (dropse != null) {
				double total = 0.0;
				List<String> dropList = new ArrayList<String>();
				for (String d : dropse.getKeys(false)) {
					dropList.add(d);
					ConfigurationSection drop = dropse.getConfigurationSection(d);
					String dropID = c + ";" + d;
					dropMaterial.put(dropID, Material.getMaterial(drop.getString("display.type")));
					dropData.put(dropID, drop.getInt("display.data"));
					dropAmount.put(dropID, drop.getInt("display.amount"));
					dropName.put(dropID, drop.getString("display.name"));
					dropLore.put(dropID, drop.getStringList("display.lore"));
					dropCommands.put(dropID, drop.getStringList("commands"));
					dropChance.put(dropID, drop.getDouble("chance"));
					total += drop.getDouble("chance");
				}
				for (String d : dropse.getKeys(false)) {
					String dropID = c + ";" + d;
					double real = (dropChance.get(dropID) / total) * 100;
					realChance.put(dropID, real);
					if (real >= 25) {
						realRarity.put(dropID, "&fCommon");
					} else if (real >= 15) {
						realRarity.put(dropID, "&dUncommon");
					} else if (real >= 6) {
						realRarity.put(dropID, "&aRare");
					} else if (real >= 2) {
						realRarity.put(dropID, "&bEpic");
					} else if (real >= 0.4) {
						realRarity.put(dropID, "&cLegendary");
					} else {
						realRarity.put(dropID, "&6&oInf&e&oer&6&ona&e&ol");
					}
//					if (real > 50) {
//						realRarity.put(dropID, "&fCommon");
//					} else if (real > 20) {
//						realRarity.put(dropID, "&dUncommon");
//					} else if (real > 10) {
//						realRarity.put(dropID, "&aRare");
//					} else if (real > 4) {
//						realRarity.put(dropID, "&bEpic");
//					} else if (real > 0.5) {
//						realRarity.put(dropID, "&cLegendary");
//					} else {
//						realRarity.put(dropID, "&c&ki&6&oInf&e&oer&6&ona&e&ol&c&ki&c");
//					}

				}
				totalChance.put(c, total);
				drops.put(c, dropList);
			}
		}
	}

	public static void reload() {
		types.clear();
		crateColor.clear();
		totalChance.clear();
		drops.clear();
		dropMaterial.clear();
		dropData.clear();
		dropAmount.clear();
		dropName.clear();
		dropLore.clear();
		dropCommands.clear();
		dropChance.clear();
		realChance.clear();
		realRarity.clear();
		setup();
	}

	public static ItemStack key(String type) {
		ItemStack i = new ItemStack(Material.TRIPWIRE_HOOK);
		ItemMeta m = i.getItemMeta();
		m.addEnchant(Enchantment.LUCK, 1, true);
		m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		m.setDisplayName(Core.color(crateColor.get(type) + "&l" + type + " Key"));
		List<String> l = new ArrayList<String>();
		l.add(Core.color("&7"));
		l.add(Core.color("&7 Right-Click on a chest while"));
		l.add(Core.color("&7 holding this key to open."));
		m.setLore(l);
		i.setItemMeta(m);
		return i;
	}

	public static ItemStack key(String type, int amount) {
		ItemStack i = new ItemStack(Material.TRIPWIRE_HOOK);
		i.setAmount(amount);
		ItemMeta m = i.getItemMeta();
		m.addEnchant(Enchantment.LUCK, 1, true);
		m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		m.setDisplayName(Core.color(crateColor.get(type) + "&l" + type + " Key"));
		List<String> l = new ArrayList<String>();
		l.add(Core.color("&7"));
		l.add(Core.color("&7 Right-Click on a chest while"));
		l.add(Core.color("&7 holding this key to open."));
		m.setLore(l);
		i.setItemMeta(m);
		return i;
	}

	public static boolean isKey(ItemStack i) {
		if (i != null && i.getType() == Material.TRIPWIRE_HOOK && i.hasItemMeta()) {
			ItemMeta m = i.getItemMeta();
			if (m.hasDisplayName() && m.hasLore()) {
				List<String> l = m.getLore();
				if (l.size() == 3 && l.get(1).equals(Core.color("&7 Right-Click on a chest while"))
						&& l.get(2).equals(Core.color("&7 holding this key to open."))) {
					return true;
				}
			}
		}
		return false;
	}

	public static String getType(ItemStack i) {
		return ChatColor.stripColor(i.getItemMeta().getDisplayName().split(" ")[0]);
	}

	public static void open(Player p, String c) {
		Random r = new Random();
		double w = totalChance.get(c) * r.nextDouble();
		double t = 0.0;

		for (String str : drops.get(c)) {
			t += dropChance.get(c + ";" + str);
			if (t >= w) {
				for (String cmd : dropCommands.get(c + ";" + str)) {
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("%player%", p.getName()));
				}
				break;
			}
		}
	}

	public static void open(Player p, String c, int a) {
		new BukkitRunnable() {
			public void run() {
				for (int x = 0; x < a; x++) {
					Random r = new Random();
					double w = totalChance.get(c) * r.nextDouble();
					double t = 0.0;
					for (String str : drops.get(c)) {
						t += dropChance.get(c + ";" + str);
						if (t >= w) {
							for (String cmd : dropCommands.get(c + ";" + str)) {
								Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("%player%", p.getName()));
							}
							break;
						}
					}
				}
			}
		}.runTaskLaterAsynchronously(SM.instance, 1);
	}

}
