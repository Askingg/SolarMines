package prevail.askingg.solarmines.main;

import java.util.UUID;

import org.bukkit.entity.Player;

import prevail.askingg.solarmines.misc.Marry;

public class User {

	private UUID uuid;
	
	public User(UUID u) {
	uuid = u;	
	}
	
	public User (Player p) {
		uuid = p.getUniqueId();
	}
	
	public boolean isMarried() {
		if (Marry.marriages.containsKey(uuid))
			return true;
		return false;
	}
}
