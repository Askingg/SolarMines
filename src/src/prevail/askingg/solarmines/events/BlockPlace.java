package prevail.askingg.solarmines.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import prevail.askingg.solarmines.crates.Crates;

public class BlockPlace implements Listener {

	@EventHandler
	public void onBreak(BlockPlaceEvent e) {
		if (Crates.isKey(e.getItemInHand()))
			e.setCancelled(true);
	}
}
