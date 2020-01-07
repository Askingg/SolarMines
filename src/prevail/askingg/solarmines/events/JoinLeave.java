package prevail.askingg.solarmines.events;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import prevail.askingg.solarmines.enchanting.Tokens;
import prevail.askingg.solarmines.misc.Blocks;

public class JoinLeave implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		UUID u = p.getUniqueId();
		if (!Tokens.tokens.containsKey(u)) {
			Tokens.tokens.put(u, 0.0);
		}
		Blocks.load(p);
		if (!Blocks.broken.containsKey(u)) {
			Blocks.broken.put(u, 0);
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
	//	UUID u = p.getUniqueId();;
		Blocks.save(p);
	}
}
