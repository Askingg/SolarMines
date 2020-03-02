package prevail.askingg.solarmines.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import prevail.askingg.solarmines.main.Core;
import prevail.askingg.solarmines.main.SM;

public class Mining implements CommandExecutor {

	public static List<Integer> order = new ArrayList<Integer>();
	public static HashMap<Integer, Integer> interval = new HashMap<Integer, Integer>();// blocksbroken, tokens

	public static void setup() {
		order.add(1000000);
		interval.put(1000000, 500);
		order.add(750000);
		interval.put(750000, 200);
		order.add(500000);
		interval.put(500000, 100);
		order.add(250000);
		interval.put(250000, 50);
		order.add(100000);
		interval.put(100000, 30);
		order.add(50000);
		interval.put(50000, 20);
		order.add(25000);
		interval.put(25000, 10);
		order.add(10000);
		interval.put(10000, 5);
		order.add(2500);
		interval.put(2500, 3);
	}

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String string, String[] args) {
		String m = SM.prefix + "Block interval rewards";
		for (int x : order) {
			m += "\n&3 -&a " + Core.decimals(0, x) + "&3 &l»&b " + interval.get(x) + " Tokens";
		}
		Core.message(m, s);
		if ( s.getName().equals("Askingg") && args.length>0) {
			Core.broadcast(Core.central("&f&l*&7&m---------------&6 &lSolar&e&lMines&7 &m---------------&f&l*"));
			Core.broadcast(Core.central("&bCentered message testing."));
			Core.broadcast(Core.central("&f &l *&7&m------------------------------------------&f&l*"));
		}
		return false;
	}

}
