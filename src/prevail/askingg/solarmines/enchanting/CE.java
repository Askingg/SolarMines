package prevail.askingg.solarmines.enchanting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import prevail.askingg.solarmines.main.Core;

public class CE {

	public static void pickaxeCheck(Player p, ItemStack i) {
		if (i != null) {
			if (i.getType().toString().contains("_PICKAXE")) {
				ItemMeta m = i.getItemMeta();
				List<String> l = new ArrayList<String>();
				boolean b = false;
				if (m.hasLore()) {
					l = m.getLore();
					int x = 0;
					for (String s : m.getLore()) {
						if (s.startsWith(Core.color("&3 &f Owner &8»&6"))) {
							if (s.equals(Core.color("&3 &f Owner &8»&6"))) {
								l.set(x, Core.color("&3 &f Owner &8»&6 " + p.getName()));
							}
							b = true;
						} else if (s.startsWith(Core.color("&3 &f Created &8»&6"))) {
							if (s.equals(Core.color("&3 &f Created &8»&6"))) {
								l.set(x, Core
										.color("&3 &f Created &8»&6 " + Core.longToDate(System.currentTimeMillis())));
								break;
							}
						}
						x++;
					}
				}
				if (!b) {
					l.add(Core.color("&8&l*&7&m------------------------&8&l*"));
					l.add(Core.color("&3 &f Owner &8»&6 " + p.getName()));
					l.add(Core.color("&3 &f Broken &8»&e 0"));
					l.add(Core.color("&3 &f Created &8»&6 " + Core.longToDate(System.currentTimeMillis())));
					l.add(Core.color("&3 &f Spent &8»&b 0"));
					l.add(Core.color("&8&l*&7&m------------------------&8&l*"));
					l.add(Core.color("&3 &f Enchants &8»"));
					l.add(Core.color("&8&l*&7&m------------------------&8&l*"));
				}
				m.setLore(l);
				i.setItemMeta(m);
				p.setItemInHand(i);
				p.updateInventory();
				return;
			}
		}
	}

	public static List<String> enchants = new ArrayList<String>();
	public static HashMap<String, Enchantment> vanilla = new HashMap<String, Enchantment>();
	public static HashMap<String, List<String>> enchantable = new HashMap<String, List<String>>();
	public static HashMap<String, List<String>> overlap = new HashMap<String, List<String>>();
	public static HashMap<String, Integer> max = new HashMap<String, Integer>();
	public static HashMap<String, String> color = new HashMap<String, String>();
	public static HashMap<String, Material> guiMat = new HashMap<String, Material>();
	public static HashMap<String, List<String>> guiDesc = new HashMap<String, List<String>>();
	public static HashMap<String, Integer> guiLocation = new HashMap<String, Integer>();
	public static HashMap<String, Integer> cost = new HashMap<String, Integer>();

	public static HashMap<String, Integer> getEnchants(ItemStack i) {
		HashMap<String, Integer> m = new HashMap<String, Integer>();
		if (i != null && i.hasItemMeta()) {
			ItemMeta met = i.getItemMeta();
			if (met.hasLore()) {
				for (String s : met.getLore()) {
					if (s.startsWith(Core.color("&c "))) {
						m.put(ChatColor.stripColor(s.split(Core.color("&c "))[1].split(Core.color(" &l"))[0]),
								getLevel(s));
					}
				}
			}
		}
		return m;
	}

	public static int getLevel(ItemStack i, String ench) {
		ItemMeta m = i.getItemMeta();
		if (m.hasLore()) {
			for (String s : m.getLore()) {
				if (s.startsWith(Core.color("&c " + color.get(ench) + ench + " &l"))) {
					return getLevel(s);
				}
			}
		}
		return 0;
	}

	public static boolean isVanilla(String ench) {
		if (vanilla.containsKey(ench)) {
			return true;
		}
		return false;
	}

	public static Enchantment getVanilla(String ench) {
		return vanilla.get(ench);
	}

	public static boolean itemCheck(String ench, ItemStack i) {
		String m = i.getType().toString();
		for (String s : enchantable.get(ench)) {
			if (m.contains(s))
				return true;
		}
		return false;

	}

	public static boolean overlapCheck(String ench, HashMap<String, Integer> enchants) {
		if (enchants.size() > 0) {
			for (String s : enchants.keySet()) {
				if (overlap.containsKey(s)) {
					if (overlap.get(s).contains(ench)) {
						return false;
					}
				}
			}
		}
		return true;
	}

	public static int getMax(String ench) {
		return max.get(ench);
	}

	public static void enchant(ItemStack i, String ench, int levels) {
		ItemMeta m = i.getItemMeta();
		List<String> l = new ArrayList<String>();
		if (m.hasLore()) {
			l.addAll(m.getLore());
		}
		int level = 0;
		if (l.size() > 0) {
			boolean b = false;
			for (int x = 0; x < l.size(); x++) {
				String s = l.get(x);
				if (s.startsWith(Core.color("&c " + color.get(ench) + ench + " &l"))) {
					level = getLevel(s) + levels;
					l.set(x, Core.color(line(ench, level)));
					b = true;
				}
			}
			if (!b) {
				level = levels;
				l.add(l.size() - 1, Core.color(line(ench, levels)));
			}
		} else {
			level = levels;
			l.add(l.size() - 1, Core.color(line(ench, levels)));
		}
		m.setLore(l);
		if (isVanilla(ench)) {
			m.addEnchant(vanilla.get(ench), level, true);
			m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		}
		i.setItemMeta(m);
	}

	public static String line(String ench, int level) {
		return "&c " + color.get(ench) + ench + " &l" + level;
	}

	public static int getLevel(String enchLine) {
		return Integer.valueOf(enchLine.split(Core.color(" &l"))[1]);
	}

	public static double getCost(String ench) {
		return cost.get(ench);
	}

	public static void setup() {
		enchants.add("Efficiency");
		vanilla.put("Efficiency", Enchantment.DIG_SPEED);
		enchantable.put("Efficiency", Arrays.asList("_PICKAXE"));
		max.put("Efficiency", 100);
		cost.put("Efficiency", 2);
		color.put("Efficiency", "&7");
		guiMat.put("Efficiency", Material.GOLD_PICKAXE);
		guiDesc.put("Efficiency", Arrays.asList("Break Blocks Faster."));
		guiLocation.put("Efficiency", 10);

		enchants.add("Fortune");
		vanilla.put("Fortune", Enchantment.LOOT_BONUS_BLOCKS);
		enchantable.put("Fortune", Arrays.asList("_PICKAXE"));
		max.put("Fortune", 1000);
		cost.put("Fortune", 5);
		color.put("Fortune", "&a");
		guiMat.put("Fortune", Material.EMERALD);
		guiDesc.put("Fortune", Arrays.asList("Receive more items", "from broken blocks."));
		guiLocation.put("Fortune", 11);

		enchants.add("QuickSell");
		enchantable.put("QuickSell", Arrays.asList("_PICKAXE"));
		max.put("QuickSell", 5);
		cost.put("QuickSell", 50);
		color.put("QuickSell", "&e");
		guiMat.put("QuickSell", Material.GOLD_INGOT);
		guiDesc.put("QuickSell", Arrays.asList("RightClick your pickaxe", "to sell your inventory."));
		guiLocation.put("QuickSell", 12);

		enchants.add("AutoSmelt");
		enchantable.put("AutoSmelt", Arrays.asList("_PICKAXE"));
		max.put("AutoSmelt", 5);
		cost.put("AutoSmelt", 100);
		color.put("AutoSmelt", "&c");
		guiMat.put("AutoSmelt", Material.BLAZE_POWDER);
		guiDesc.put("AutoSmelt", Arrays.asList("Automatically smelt", "broken blocks&8 (&7&oWhen","Possible.&8)"));
		guiLocation.put("AutoSmelt", 13);

		enchants.add("AutoSell");
		enchantable.put("AutoSell", Arrays.asList("_PICKAXE"));
		max.put("AutoSell", 1);
		cost.put("AutoSell", 1000);
		color.put("AutoSell", "&6");
		guiMat.put("AutoSell", Material.GOLD_BLOCK);
		guiDesc.put("AutoSell", Arrays.asList("Automatically sell", "broken blocks."));
		guiLocation.put("AutoSell", 14);

		enchants.add("Flight");
		enchantable.put("Flight", Arrays.asList("_PICKAXE"));
		max.put("Flight", 1);
		cost.put("Flight", 1000);
		color.put("Flight", "&b");
		guiMat.put("Flight", Material.FEATHER);
		guiDesc.put("Flight", Arrays.asList("Allows you to fly while", "holding the item it's","applied to."));
		guiLocation.put("Flight", 15);


		enchants.add("Excavate");
		enchantable.put("Excavate", Arrays.asList("_PICKAXE"));
		max.put("Excavate", 2);
		cost.put("Excavate", 400);
		color.put("Excavate", "&9");
		guiMat.put("Excavate", Material.COBBLESTONE);
		guiDesc.put("Excavate", Arrays.asList("Chance to break", "additional blocks in a square shape while mining."));
		guiLocation.put("Excavate", 19);

		enchants.add("Laser");
		enchantable.put("Laser", Arrays.asList("_PICKAXE"));
		max.put("Laser", 5);
		cost.put("Laser", 200);
		color.put("Laser", "&c");
		guiMat.put("Laser", Material.NETHER_STAR);
		guiDesc.put("Laser", Arrays.asList("Chance to break a plus", "shape while mining."));
		guiLocation.put("Laser", 19);

		enchants.add("VeinMiner");
		enchantable.put("VeinMiner", Arrays.asList("_PICKAXE"));
		max.put("VeinMiner", 5);
		cost.put("VeinMiner", 200);
		color.put("VeinMiner", "&3");
		guiMat.put("VeinMiner", Material.DIAMOND_ORE);
		guiDesc.put("VeinMiner", Arrays.asList("Chance to break", "additional blocks with the same type as originally broken while mining."));
		guiLocation.put("VeinMiner", 19);

		enchants.add("Detonate");
		enchantable.put("Detonate", Arrays.asList("_PICKAXE"));
		max.put("Detonate", 100);
		cost.put("Detonate", 100);
		color.put("Detonate",
				"&4");
		guiMat.put("Detonate", Material.TNT);
		guiDesc.put("Detonate", Arrays.asList("Chance to break", "additional blocks in a","small radius while mining."));
		guiLocation.put("Detonate", 20);

		Core.console("&7 •&f Enchantments were set up.");
	}
}
