package prevail.askingg.solarmines.sell;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SellCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String string, String[] args) {
		if (s instanceof Player) {
			Player p = (Player) s;
			new Shops(p).sellall(p);
		}
		return false;
	}

}
