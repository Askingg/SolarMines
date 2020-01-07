package prevail.askingg.solarmines.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;

public class Config {

	public static List<String> shops = new ArrayList<String>();
	public static HashMap<String, Double> worth = new HashMap<String, Double>(); // shop;material;data, worth

	public static void apply() {
		ConfigurationSection conf = Files.config.getConfigurationSection("shops");
		if (conf != null) {
			for (String shop : conf.getKeys(false)) {
				shops.add(shop);
				for (String s : conf.getConfigurationSection(shop).getKeys(false)) {
					String st = s;
					if (!s.contains(";")) {
						st = s + ";0";
					}
					worth.put(shop + ";" + st, conf.getDouble(shop + "." + s));
				}
				Core.console("&7 •&f Shop, " + shop + ", was set up.");
			}
		}
	}

	public static void reload() {
		shops.clear();
		worth.clear();
		Files.base();
	}
}
