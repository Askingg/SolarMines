package prevail.askingg.solarmines.main;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import prevail.askingg.solarmines.commands.BlockCheck;
import prevail.askingg.solarmines.commands.CrateMoney;
import prevail.askingg.solarmines.commands.Donation;
import prevail.askingg.solarmines.commands.Mining;
import prevail.askingg.solarmines.commands.Reload;
import prevail.askingg.solarmines.commands.VoteRewards;
import prevail.askingg.solarmines.crates.Crates;
import prevail.askingg.solarmines.crates.CratesCMD;
import prevail.askingg.solarmines.crates.DropsGUI;
import prevail.askingg.solarmines.enchanting.CE;
import prevail.askingg.solarmines.enchanting.CEGUI;
import prevail.askingg.solarmines.enchanting.PrestigePoints;
import prevail.askingg.solarmines.enchanting.Tokens;
import prevail.askingg.solarmines.enchanting.enchants.AutoSmelt;
import prevail.askingg.solarmines.enchanting.enchants.LuckyBlocks;
import prevail.askingg.solarmines.events.BlockBreak;
import prevail.askingg.solarmines.events.BlockPlace;
import prevail.askingg.solarmines.events.Interact;
import prevail.askingg.solarmines.events.ItemHeld;
import prevail.askingg.solarmines.events.JoinLeave;
import prevail.askingg.solarmines.events.PlayerChat;
import prevail.askingg.solarmines.misc.Blocks;
import prevail.askingg.solarmines.misc.Marry;
import prevail.askingg.solarmines.misc.Tablist;
import prevail.askingg.solarmines.progress.PrestigeCMD;
import prevail.askingg.solarmines.progress.Ranks;
import prevail.askingg.solarmines.progress.RanksCMD;
import prevail.askingg.solarmines.progress.RankupCMD;
import prevail.askingg.solarmines.sell.Booster;
import prevail.askingg.solarmines.sell.SellCMD;

public class SM extends JavaPlugin {

	public static SM instance;
	public static boolean papi;
	public static String prefix = "&6&lSolar&e&lMines&8 &l>>&f ";
	public static Economy eco = null;
	public static Permission perms = null;
	public static WorldGuardPlugin wg;
	public static CE enchants;

	public void onEnable() {
		instance = this;
		eco = getServer().getServicesManager().getRegistration(Economy.class).getProvider();
		perms = getServer().getServicesManager().getRegistration(Permission.class).getProvider();
		wg=WorldGuardPlugin.inst();
		enchants = new CE();
		
		Core.console(prefix + "Loading plugin..");
		if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
			Core.console("&7 �&f PlaceHolderAPI will not be used as it was not found.");
			papi = false;
		} else {
			papi = true;
			Core.console("&7 �&f PlaceHolderAPI will be used.");
		}
		getServer().getPluginManager().registerEvents(new BlockBreak(), this);
		getServer().getPluginManager().registerEvents(new JoinLeave(), this);
		getServer().getPluginManager().registerEvents(new CEGUI(), this);
		getServer().getPluginManager().registerEvents(new Interact(), this);
		getServer().getPluginManager().registerEvents(new PlayerChat(), this);
		getServer().getPluginManager().registerEvents(new Marry(), this);
		getServer().getPluginManager().registerEvents(new ItemHeld(), this);
		getServer().getPluginManager().registerEvents(new DropsGUI(), this);
		getServer().getPluginManager().registerEvents(new BlockPlace(), this);
		getServer().getPluginManager().registerEvents(new LuckyBlocks(), this);
		getCommand("tokens").setExecutor(new Tokens());
		getCommand("customenchant").setExecutor(new CEGUI());
		getCommand("sell").setExecutor(new SellCMD());
		getCommand("blocks").setExecutor(new Blocks());
		getCommand("blockstop").setExecutor(new Blocks());
		getCommand("booster").setExecutor(new Booster());
		getCommand("solarreload").setExecutor(new Reload());
		getCommand("rankup").setExecutor(new RankupCMD());
		getCommand("rankupmax").setExecutor(new RankupCMD());
		getCommand("prestige").setExecutor(new PrestigeCMD());
		getCommand("ranks").setExecutor(new RanksCMD());
		getCommand("crates").setExecutor(new CratesCMD());
		getCommand("marry").setExecutor(new Marry());
		getCommand("cratemoney").setExecutor(new CrateMoney());
		getCommand("mining").setExecutor(new Mining());
		getCommand("blockcheck").setExecutor(new BlockCheck());
		getCommand("luckyblocks").setExecutor(new LuckyBlocks());
		getCommand("voterewards").setExecutor(new VoteRewards());
		getCommand("donationmessage").setExecutor(new Donation());
		getCommand("prestigepoints").setExecutor(new PrestigePoints());
		new Placeholders().register();
		Files.base();
		Tokens.load();
		Blocks.leaderboard();
		Booster.load();
		Booster.timer();
		Tablist.timer();
		AutoSmelt.setup();
		Ranks.load();
		Mining.setup();
		Crates.setup();
		Marry.load();
		PrestigePoints.load();
		enchants.setupVanillaEnchantment(enchant, vanilla, applicable, max, cost, color, material, description, slot);
		
		Core.console("&7 �&f Plugin loading complete.");
	}

	public void onDisable() {
		Tokens.save();
		if (Bukkit.getOnlinePlayers().size()>0) {
			for (Player p : Bukkit.getOnlinePlayers()) {
				Blocks.save(p);
			}
		}
		Booster.save();
		Ranks.save();
		Marry.save();
		PrestigePoints.save();
	}
}
