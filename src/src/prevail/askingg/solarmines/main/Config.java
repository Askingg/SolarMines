package prevail.askingg.solarmines.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;

import prevail.askingg.solarmines.progress.Ranks;

public class Config {

	public static HashMap<String, String> highestSell = new HashMap<String, String>();
	public static HashMap<String, Integer> sellValue = new HashMap<String, Integer>();

	public static HashMap<String, Double> worth = new HashMap<String, Double>();
	public static HashMap<String, Double> modifier = new HashMap<String, Double>();

	public static List<String> player = new ArrayList<String>();
	public static HashMap<String, String> playerChat = new HashMap<String, String>();
	public static HashMap<String, List<String>> playerHover = new HashMap<String, List<String>>();
	public static HashMap<String, String> playerClick = new HashMap<String, String>();
	public static List<String> perm = new ArrayList<String>();
	public static HashMap<String, String> permChat = new HashMap<String, String>();
	public static HashMap<String, List<String>> permHover = new HashMap<String, List<String>>();
	public static HashMap<String, String> permClick = new HashMap<String, String>();

	public static List<String> player2 = new ArrayList<String>();
	public static HashMap<String, String> playerTab = new HashMap<String, String>();
	public static List<String> perm2 = new ArrayList<String>();
	public static HashMap<String, String> permTab = new HashMap<String, String>();

	public static void apply() {
		ConfigurationSection conf = Files.config.getConfigurationSection("highestSell");
		if (conf != null) {
			for (String s : conf.getKeys(false)) {
				String st = conf.getString(s);
				if (st.contains(";")) {
					highestSell.put(s, st);
				} else {
					highestSell.put(s, st + ";0");
				}
			}
		}
		 conf = Files.config.getConfigurationSection("sellValue");
		if (conf != null) {
			for (String s : conf.getKeys(false)) {
				if (s.contains(";")) {
					sellValue.put(s, conf.getInt(s));
				} else {
					sellValue.put(s + ";0", conf.getInt(s));
				}
			}
		}

		conf = Files.config.getConfigurationSection("baseWorth");
		if (conf != null) {
			for (String s : conf.getKeys(false)) {
				if (s.contains(";")) {
					worth.put(s, conf.getDouble(s));
				} else {
					worth.put(s + ";0", conf.getDouble(s));
				}
			}
		}

		conf = Files.config.getConfigurationSection("rankModifiers");
		if (conf != null) {
			for (String s : conf.getKeys(false)) {
				modifier.put(s, conf.getDouble(s));
			}
		}

		conf = Files.config.getConfigurationSection("chat.player");
		if (conf != null) {
			for (String p : conf.getKeys(false)) {
				player.add(p);
				playerChat.put(p, conf.getString(p + ".chat"));
				playerHover.put(p, conf.getStringList(p + ".hover"));
				playerClick.put(p, conf.getString(p + ".click"));
			}
		}

		conf = Files.config.getConfigurationSection("chat.permission");
		if (conf != null) {
			for (String s : conf.getKeys(false)) {
				perm.add(s);
				permChat.put(s, conf.getString(s + ".chat"));
				permHover.put(s, conf.getStringList(s + ".hover"));
				permClick.put(s, conf.getString(s + ".click"));
			}
		}

		conf = Files.config.getConfigurationSection("tab.player");
		if (conf != null) {
			for (String p : conf.getKeys(false)) {
				player2.add(p);
				playerTab.put(p, conf.getString(p));
			}
		}

		conf = Files.config.getConfigurationSection("tab.permission");
		if (conf != null) {
			for (String p : conf.getKeys(false)) {
				perm2.add(p);
				permTab.put(p, conf.getString(p));
			}
		}

		Ranks.ranks.clear();
		Ranks.costBase.clear();
		conf = Files.config.getConfigurationSection("ranks");
		if (conf != null) {
			for (String r : conf.getKeys(false)) {
				Ranks.ranks.add(r);
				Ranks.costBase.put(r, conf.getDouble(r));
			}
		}
	}

	public static void reload() {
		worth.clear();
		modifier.clear();

		player = new ArrayList<String>();
		playerChat = new HashMap<String, String>();
		playerHover = new HashMap<String, List<String>>();
		playerClick = new HashMap<String, String>();
		perm = new ArrayList<String>();
		permChat = new HashMap<String, String>();
		permHover = new HashMap<String, List<String>>();
		permClick = new HashMap<String, String>();

		player2 = new ArrayList<String>();
		playerTab = new HashMap<String, String>();
		perm2 = new ArrayList<String>();
		permTab = new HashMap<String, String>();

		Files.base();
	}
}
