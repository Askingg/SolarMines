package prevail.askingg.solarmines.enchanting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import prevail.askingg.solarmines.main.Core;

public class CE {

	public void pickaxeCheck(Player p, ItemStack i) {
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
				p.getInventory().setItemInMainHand(i);
				p.updateInventory();
				return;
			}
		}
	}

	public List<String> enchants = new ArrayList<String>();
	public HashMap<String, Enchantment> vanilla = new HashMap<String, Enchantment>();
	public HashMap<String, List<String>> enchantable = new HashMap<String, List<String>>();
	public HashMap<String, List<String>> overlap = new HashMap<String, List<String>>();
	public HashMap<String, Integer> max = new HashMap<String, Integer>();
	public HashMap<String, String> color = new HashMap<String, String>();
	public HashMap<String, Material> guiMat = new HashMap<String, Material>();
	public HashMap<String, List<String>> guiDesc = new HashMap<String, List<String>>();
	public HashMap<String, Integer> guiLocation = new HashMap<String, Integer>();
	public HashMap<String, Integer> cost = new HashMap<String, Integer>();
	public HashMap<String, Integer> prestigeCost = new HashMap<String, Integer>();

	public HashMap<String, Integer> getEnchants(ItemStack i) {
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

	public int getLevel(ItemStack i, String ench) {
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

	public boolean isVanilla(String ench) {
		if (vanilla.containsKey(ench)) {
			return true;
		}
		return false;
	}

	public Enchantment getVanilla(String ench) {
		return vanilla.get(ench);
	}

	public boolean itemCheck(String ench, ItemStack i) {
		String m = i.getType().toString();
		for (String s : enchantable.get(ench)) {
			if (m.contains(s))
				return true;
		}
		return false;

	}

	public boolean overlapCheck(String ench, HashMap<String, Integer> enchants) {
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

	public int getMax(String ench) {
		return max.get(ench);
	}

	public void enchant(ItemStack i, String ench, int levels) {
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

	public String line(String ench, int level) {
		return "&c " + color.get(ench) + ench + " &l" + level;
	}

	public int getLevel(String enchLine) {
		return Integer.valueOf(enchLine.split(Core.color(" &l"))[1]);
	}

	public double getCost(String ench, int level) {
		if (cost.containsKey(ench)) {
			return cost.get(ench) + (cost.get(ench) * level);
		} else if (prestigeCost.containsKey(ench)) {
			return prestigeCost.get(ench) + (prestigeCost.get(ench) * level);
		}
		return -1;
	}

	public void setupVanillaEnchantment(String enchant, Enchantment vanilla, List<String> applicable, int max, int cost,
			String color, Material material, List<String> description, int slot, boolean costPoints) {
		this.enchants.add(enchant);
		this.vanilla.put(enchant, vanilla);
		this.enchantable.put(enchant, applicable);
		this.max.put(enchant, max);
		if (costPoints) {
			this.prestigeCost.put(enchant, cost);
		} else {
			this.cost.put(enchant, cost);
		}
		this.color.put(enchant, color);
		this.guiMat.put(enchant, material);
		this.guiDesc.put(enchant, description);
		this.guiLocation.put(enchant, slot);
	}

	public void setupEnchantment(String enchant, List<String> applicable, int max, int cost, String color,
			Material material, List<String> description, int slot, boolean costPoints) {
		this.enchants.add(enchant);
		this.enchantable.put(enchant, applicable);
		this.max.put(enchant, max);
		if (costPoints) {
			this.prestigeCost.put(enchant, cost);
		} else {
			this.cost.put(enchant, cost);
		}
		this.color.put(enchant, color);
		this.guiMat.put(enchant, material);
		this.guiDesc.put(enchant, description);
		this.guiLocation.put(enchant, slot);
	}

	@SuppressWarnings("deprecation")
	public void setup() {
		enchants.add("Efficiency");
		vanilla.put("Efficiency", Enchantment.DIG_SPEED);
		enchantable.put("Efficiency", Arrays.asList("_PICKAXE"));
		max.put("Efficiency", 100);
		cost.put("Efficiency", 5);
		color.put("Efficiency", "&7");
		guiMat.put("Efficiency", Material.GOLD_PICKAXE);
		guiDesc.put("Efficiency", Arrays.asList("Break Blocks Faster."));
		guiLocation.put("Efficiency", 10);

		enchants.add("Fortune");
		vanilla.put("Fortune", Enchantment.LOOT_BONUS_BLOCKS);
		enchantable.put("Fortune", Arrays.asList("_PICKAXE"));
		max.put("Fortune", 2500);
		cost.put("Fortune", 15);
		color.put("Fortune", "&a");
		guiMat.put("Fortune", Material.DIAMOND_ORE);
		guiDesc.put("Fortune", Arrays.asList("Receive more items", "from blocks broken."));
		guiLocation.put("Fortune", 11);

		enchants.add("Tokens");
		enchantable.put("Tokens", Arrays.asList("_PICKAXE"));
		max.put("Tokens", Integer.MAX_VALUE);
		cost.put("Tokens", 25);
		color.put("Tokens", "&b");
		guiMat.put("Tokens", Material.SLIME_BALL);
		guiDesc.put("Tokens", Arrays.asList("Receive more tokens", "while mining."));
		guiLocation.put("Tokens", 12);

		enchants.add("Keys");
		enchantable.put("Keys", Arrays.asList("_PICKAXE"));
		max.put("Keys", 10);
		cost.put("Keys", 2500);
		color.put("Keys", "&b");
		guiMat.put("Keys", Material.TRIPWIRE_HOOK);
		guiDesc.put("Keys", Arrays.asList("Receive more keys", "while mining."));
		guiLocation.put("Keys", 13);

		enchants.add("LuckyBlocks");
		enchantable.put("LuckyBlocks", Arrays.asList("_PICKAXE"));
		max.put("LuckyBlocks", 10);
		cost.put("LuckyBlocks", 1000);
		color.put("LuckyBlocks", "&6");
		guiMat.put("LuckyBlocks", Material.SPONGE);
		guiDesc.put("LuckyBlocks", Arrays.asList("Receive better rewards", "from luckyblocks."));
		guiLocation.put("LuckyBlocks", 14);

		enchants.add("Fountain");
		enchantable.put("Fountain", Arrays.asList("_PICKAXE"));
		max.put("Fountain", 10);
		cost.put("Fountain", 2500);
		color.put("Fountain", "&a");
		guiMat.put("Fountain", Material.EMERALD);
		guiDesc.put("Fountain", Arrays.asList("Find lots of money", "while mining"));
		guiLocation.put("Fountain", 15);

		enchants.add("Charity");
		enchantable.put("Charity", Arrays.asList("_PICKAXE"));
		max.put("Charity", 100);
		prestigeCost.put("Charity", 200);
		color.put("Charity", "&a");
		guiMat.put("Charity", Material.PAPER);
		guiDesc.put("Charity", Arrays.asList("All online players", "receive some money,", "and you receive a",
				"large amount of money."));
		guiLocation.put("Charity", 16);

		enchants.add("Blessing");
		enchantable.put("Blessing", Arrays.asList("_PICKAXE"));
		max.put("Blessing", 100);
		prestigeCost.put("Blessing", 250);
		color.put("Blessing", "&b");
		guiMat.put("Blessing", Material.SLIME_BALL);
		guiDesc.put("Blessing", Arrays.asList("All online players", "receive some tokens,", "and you receive a",
				"large amount of tokens."));
		guiLocation.put("Blessing", 19);

		enchants.add("Ranker");
		enchantable.put("Ranker", Arrays.asList("_PICKAXE"));
		max.put("Ranker", 100);
		cost.put("Ranker", 500);
		color.put("Ranker", "&7");
		guiMat.put("Ranker", Material.ARROW);
		guiDesc.put("Ranker", Arrays.asList("Instantly receive a", "free rankup."));
		guiLocation.put("Ranker", 20);

		enchants.add("Prestiger");
		enchantable.put("Prestiger", Arrays.asList("_PICKAXE"));
		max.put("Prestiger", 100);
		prestigeCost.put("Prestiger", 150);
		color.put("Prestiger", "&f");
		guiMat.put("Prestiger", Material.SPECTRAL_ARROW);
		guiDesc.put("Prestiger", Arrays.asList("Instantly receive a", "free prestige."));
		guiLocation.put("Prestiger", 21);

		enchants.add("MoneyBoosters");
		enchantable.put("MoneyBoosters", Arrays.asList("_PICKAXE"));
		max.put("MoneyBoosters", 10);
		cost.put("MoneyBoosters", 25000);
		color.put("MoneyBoosters", "&a");
		guiMat.put("MoneyBoosters", Material.MAGMA_CREAM);
		guiDesc.put("MoneyBoosters", Arrays.asList("Find money boosters", "while mining"));
		guiLocation.put("MoneyBoosters", 22);

		enchants.add("TokenBoosters");
		enchantable.put("TokenBoosters", Arrays.asList("_PICKAXE"));
		max.put("TokenBoosters", 10);
		cost.put("TokenBoosters", 30000);
		color.put("TokenBoosters", "&b");
		guiMat.put("TokenBoosters", Material.MAGMA_CREAM);
		guiDesc.put("TokenBoosters", Arrays.asList("Find token boosters", "while mining"));
		guiLocation.put("TokenBoosters", 23);

		enchants.add("Duplicate");
		enchantable.put("Duplicate", Arrays.asList("_PICKAXE"));
		max.put("Duplicate", 10);
		cost.put("Duplicate", 20000);
		color.put("Duplicate", "&b");
		guiMat.put("Duplicate", Material.GOLD_BLOCK);
		guiDesc.put("Duplicate", Arrays.asList("Chance to multiply", "rewards from other", "enchantments."));
		guiLocation.put("Duplicate", 24);

		enchants.add("Decoy");
		enchantable.put("Decoy", Arrays.asList("_PICKAXE"));
		max.put("Decoy", 10);
		cost.put("Decoy", 10000);
		color.put("Decoy", "&a");
		guiMat.put("Decoy", Material.SLIME_BLOCK);
		guiDesc.put("Decoy", Arrays.asList("Replace nearby blocks", "with your highest", "selling block."));
		guiLocation.put("Decoy", 25);

		enchants.add("Disc");
		enchantable.put("Disc", Arrays.asList("_PICKAXE"));
		max.put("Disc", 100);
		cost.put("Disc", 500);
		color.put("Disc", "&c");
		guiMat.put("Disc", Material.getMaterial(2256));
		guiDesc.put("Disc", Arrays.asList("Virtually break up to", "75 additional blocks while mining"));
		guiLocation.put("Disc", 28);

		enchants.add("Explosive");
		enchantable.put("Explosive", Arrays.asList("_PICKAXE"));
		max.put("Explosive", 100);
		cost.put("Explosive", 750);
		color.put("Explosive", "&4");
		guiMat.put("Explosive", Material.TNT);
		guiDesc.put("Explosive", Arrays.asList("Virtually break up to", "125 additional blocks while mining"));
		guiLocation.put("Explosive", 29);

		enchants.add("JackHammer");
		enchantable.put("JackHammer", Arrays.asList("_PICKAXE"));
		max.put("JackHammer", 100);
		prestigeCost.put("JackHammer", 250);
		color.put("JackHammer", "&c");
		guiMat.put("JackHammer", Material.ANVIL);
		guiDesc.put("JackHammer", Arrays.asList("Virtually break up to", "1,000 additional blocks while mining."));
		guiLocation.put("JackHammer", 30);

		Core.console("&7 •&f Enchantments were set up.");
	}
}
