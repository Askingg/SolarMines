package prevail.askingg.solarmines.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import prevail.askingg.solarmines.main.Config;
import prevail.askingg.solarmines.main.Core;
import prevail.askingg.solarmines.main.SM;

public class Reload implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String string, String[] args) {
		if (s instanceof ConsoleCommandSender || s.hasPermission("solar.reload")) {
			Config.reload();
			Core.message(SM.prefix+"Reloaded the config file.", s);
		} else {
			Core.message(SM.prefix+"Sorry, but you don't have permission to do that.", s);
		}
		return true;
	}
}
