package prevail.askingg.solarmines.events;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import prevail.askingg.solarmines.enchanting.CE;
import prevail.askingg.solarmines.enchanting.Tokens;
import prevail.askingg.solarmines.enchanting.enchants.TokensCE;
import prevail.askingg.solarmines.main.Core;
import prevail.askingg.solarmines.main.SM;
import prevail.askingg.solarmines.sell.Sell;

public class BlockBreak implements Listener {

	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		Block b = e.getBlock();
		if (SM.wg.canBuild(p, b)) {
			if (b.getType() == Material.SPONGE && !p.hasPermission("solar.luckyblocks.break")) {
				e.setCancelled(true);
				return;
			}
			ItemStack i = p.getInventory().getItemInMainHand();
			HashMap<String, Integer> enchants = CE.getEnchants(i);
			Random r = new Random();
			ItemStack item = new ItemStack(Material.AIR);
			for (ItemStack drop : b.getDrops()) {
				item = drop;
				break;
			}
			item.setAmount(r.nextInt(i.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS)+1)+1);
			if (i.getType().toString().contains("_PICKAXE") && i.hasItemMeta()) {
				CE.pickaxeCheck(p, i);
				ItemMeta m = i.getItemMeta();
				List<String> l = m.getLore();
				int x = 0;
				for (String s : l) {
					if (s.startsWith(Core.color("&3 &f Broken &8»&e "))) {
						l.set(x, Core.color(
								"&3 &f Broken &8»&e " + (Integer.valueOf(s.split(Core.color("&8»&e "))[1]) + 1)));
						break;
					}
					x++;
				}
				m.setLore(l);
				i.setItemMeta(m);
				i.setDurability((short) 0);
				p.getInventory().setItemInMainHand(i);
				p.updateInventory();
				

				if (r.nextDouble() < 0.0011) {
					int level = 0;
					if (enchants.containsKey("Tokens"))
						level = enchants.get("Tokens");
					int tokens = TokensCE.getTokens(level);
					Tokens.addNoMsg(p, tokens);
					Core.message("&b&lTokens&8 &l>>&f You found &b" + Core.decimals(0, tokens) + "&f Tokens", p);
				}
			}
			new Sell(p).sell(item);
			b.setType(Material.AIR);
		}
	}
}
