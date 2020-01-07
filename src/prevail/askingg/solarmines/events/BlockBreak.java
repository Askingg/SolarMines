package prevail.askingg.solarmines.events;

import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import prevail.askingg.solarmines.enchanting.CE;
import prevail.askingg.solarmines.enchanting.Tokens;
import prevail.askingg.solarmines.main.Core;
import prevail.askingg.solarmines.main.SM;
import prevail.askingg.solarmines.misc.Blocks;

public class BlockBreak implements Listener {

	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		if (WorldGuardPlugin.inst().canBuild(p, e.getBlock())) {
			ItemStack i = p.getItemInHand();
			if (i.getType().toString().contains("_PICKAXE") && i.hasItemMeta()) {
				CE.pickaxeCheck(p, i);
				ItemMeta m = i.getItemMeta();
				List<String> l = m.getLore();
				int x = 0;
				for (String s : l) {
					if (s.startsWith(Core.color("&3 &f Broken &8»&e "))) {
						l.set(x, Core
								.color("&3 &f Broken &8»&e " + (Integer.valueOf(s.split(Core.color("&8»&e "))[1]) + 1)));
						break;
					}
					x++;
				}
				m.setLore(l);
				i.setItemMeta(m);
				i.setDurability((short) 0);
				p.setItemInHand(i);
				p.updateInventory();

				UUID u = p.getUniqueId();
				int blocks = Blocks.broken.get(u) + 1;
				Blocks.broken.put(u, blocks);
				if (blocks % 1000000 == 0) {
					Tokens.addNoMsg(p, 500);
					Core.broadcast(SM.prefix + "&6" + p.getName() + "&f mined &e" + Core.decimals(0, blocks)
							+ "&f blocks, and was given &b500&f Tokens.");
				} else if (blocks % 750000 == 0) {
					Tokens.addNoMsg(p, 200);
					Core.broadcast(SM.prefix + "&6" + p.getName() + "&f mined &e" + Core.decimals(0, blocks)
							+ "&f blocks, and was given &b200&f Tokens.");
				} else if (blocks % 500000 == 0) {
					Tokens.addNoMsg(p, 100);
					Core.broadcast(SM.prefix + "&6" + p.getName() + "&f mined &e" + Core.decimals(0, blocks)
							+ "&f blocks, and was given &b100&f Tokens.");
				} else if (blocks % 250000 == 0) {
					Tokens.addNoMsg(p, 50);
					Core.broadcast(SM.prefix + "&6" + p.getName() + "&f mined &e" + Core.decimals(0, blocks)
							+ "&f blocks, and was given &b50&f Tokens.");
				} else if (blocks % 100000 == 0) {
					Tokens.addNoMsg(p, 30);
					Core.broadcast(SM.prefix + "&6" + p.getName() + "&f mined &e" + Core.decimals(0, blocks)
							+ "&f blocks, and was given &b30&f Tokens.");
				} else if (blocks % 50000 == 0) {
					Tokens.addNoMsg(p, 20);
					Core.message(SM.prefix + "You mined &6" + Core.decimals(0, blocks)
							+ "&f blocks, and were given &b50000&f Tokens.", p);
				} else if (blocks % 25000 == 0) {
					Tokens.addNoMsg(p, 10);
					Core.message(SM.prefix + "You mined &6" + Core.decimals(0, blocks)
							+ "&f blocks, and were given &b10&f Tokens.", p);
				} else if (blocks % 10000 == 0) {
					Tokens.addNoMsg(p, 5);
					Core.message(SM.prefix + "You mined &6" + Core.decimals(0, blocks)
							+ "&f blocks, and were given &b5&f Tokens.", p);
				} else if (blocks % 2500 == 0) {
					Tokens.addNoMsg(p, 3);
					Core.message(SM.prefix + "You mined &6" + Core.decimals(0, blocks)
							+ "&f blocks, and were given &b3&f Tokens.", p);
				}

			}
			ItemStack it = new ItemStack(Material.AIR);
			for (ItemStack is : e.getBlock().getDrops()) {
				it = is;
				break;
			}
			Material material = it.getType();
			int data = it.getDurability();
			e.setCancelled(true);
			e.getBlock().setType(Material.AIR);
			new BukkitRunnable() {
				@Override
				public void run() {

					// HashMap<String, Integer > enchants = CE.getEnchants(i);
					ItemStack item = new ItemStack(material, i.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS) + 1);
					item.setDurability((byte) data);
					p.getInventory().addItem(item);
					p.updateInventory();
				}
			}.runTaskLaterAsynchronously(SM.instance, 0);
		}
	}
}
