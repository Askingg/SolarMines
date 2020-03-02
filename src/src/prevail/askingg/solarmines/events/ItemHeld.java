package prevail.askingg.solarmines.events;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import prevail.askingg.solarmines.enchanting.CE;

public class ItemHeld implements Listener {

	@EventHandler
	public void onHold(PlayerItemHeldEvent e) {
		Player p = e.getPlayer(); 
		boolean fly = false;
		int speed = 0;
		int haste = 0;
		HashMap<String, Integer> enchants = CE.getEnchants(p.getInventory().getItem(e.getNewSlot()));
		if (enchants.size() > 0) {
			if (enchants.containsKey("Flight"))
				fly = true;
			if (enchants.containsKey("Speed"))
				speed = enchants.get("Speed");
			if (enchants.containsKey("Haste"))
				haste = enchants.get("Haste");
		}
		if (fly) {
				p.setAllowFlight(true);
				p.setFlying(true);
		} else {
			if (!p.hasPermission("essentials.fly")) {
				p.setAllowFlight(false);
				p.setFlying(false);
			}
		}
		if (speed > 0) {
			p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, speed-1,true));
		}else {
			p.removePotionEffect(PotionEffectType.SPEED);
		}
		if (haste > 0) {
			p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, haste-1,true));
		}else {
			p.removePotionEffect(PotionEffectType.FAST_DIGGING);
		}
	}
}
