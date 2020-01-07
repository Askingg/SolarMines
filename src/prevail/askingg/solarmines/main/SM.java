package prevail.askingg.solarmines.main;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;
import prevail.askingg.solarmines.enchanting.CE;
import prevail.askingg.solarmines.enchanting.CEGUI;
import prevail.askingg.solarmines.enchanting.Tokens;
import prevail.askingg.solarmines.events.BlockBreak;
import prevail.askingg.solarmines.events.Interact;
import prevail.askingg.solarmines.events.JoinLeave;
import prevail.askingg.solarmines.misc.Blocks;
import prevail.askingg.solarmines.sell.Booster;
import prevail.askingg.solarmines.sell.SellCMD;

public class SM extends JavaPlugin {

	public static SM instance;
	public static boolean papi;
	public static String prefix = "&6&lSolar&e&lMines&8 »&f ";
	public static Economy eco = null;

	public void onEnable() {
		instance = this;
		Core.console(prefix + "Loading plugin..");
		if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
			Core.console("&7 •&f PlaceHolderAPI will not be used as it was not found.");
			papi = false;
		} else {
			papi = true;
			Core.console("&7 •&f PlaceHolderAPI will be used.");
		}
		getServer().getPluginManager().registerEvents(new BlockBreak(), this);
		getServer().getPluginManager().registerEvents(new JoinLeave(), this);
		getServer().getPluginManager().registerEvents(new CEGUI(), this);
		getServer().getPluginManager().registerEvents(new Interact(), this);
		getCommand("tokens").setExecutor(new Tokens());
		getCommand("customenchant").setExecutor(new CEGUI());
		getCommand("sell").setExecutor(new SellCMD());
		getCommand("blocks").setExecutor(new Blocks());
		getCommand("blockstop").setExecutor(new Blocks());
		getCommand("booster").setExecutor(new Booster());
		new Placeholders().register();
		Files.base();
		CE.setup();
		Tokens.load();
		Blocks.leaderboard();
		Booster.load();
		Booster.timer();

		eco = getServer().getServicesManager().getRegistration(Economy.class).getProvider();

		Core.console("&7 •&f Plugin loading complete.");
	}

	public void onDisable() {
		Tokens.save();
		if (Bukkit.getOnlinePlayers().size()>0) {
			for (Player p : Bukkit.getOnlinePlayers()) {
				Blocks.save(p);
			}
		}
		Booster.save();
	}
}
