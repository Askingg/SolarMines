package prevail.askingg.solarmines.main;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Files {

	public static File dataFile;
	public static FileConfiguration data;
	public static File configFile;
	public static FileConfiguration config;
	public static File cratesFile;
	public static FileConfiguration crates;

	public static void base() {
		SM main = SM.getPlugin(SM.class);
		if (!main.getDataFolder().exists()) {
			main.getDataFolder().mkdirs();
			Core.console("&7 •&f Created the &a" + main.getDataFolder().getName().toString() + "&7 folder");
		}
		dataFile = new File(main.getDataFolder(), "data.yml");
		if (!dataFile.exists()) {
			main.saveResource("data.yml", false);
			Core.console("&7 •&f Created the &adata.yml&7 file");
		}
		data = YamlConfiguration.loadConfiguration(dataFile);
		configFile = new File(main.getDataFolder(), "config.yml");
		if (!configFile.exists()) {
			main.saveResource("config.yml", false);
			Core.console("&7 •&f Created the &aconfig.yml&7 file");
		}
		config = YamlConfiguration.loadConfiguration(configFile);
		cratesFile = new File(main.getDataFolder(), "crates.yml");
		if (!cratesFile.exists()) {
			main.saveResource("crates.yml", false);
			Core.console("&7 •&f Created the &acrates.yml&7 file");
		}
		crates = YamlConfiguration.loadConfiguration(cratesFile);
		Config.apply();
	}
	
	public static FileConfiguration reloadCrates() {
		SM main = SM.getPlugin(SM.class);
		cratesFile = new File(main.getDataFolder(), "crates.yml");
		if (!cratesFile.exists()) {
			main.saveResource("crates.yml", false);
			Core.console("&7 •&f Created the &acrates.yml&7 file");
		}
		crates = YamlConfiguration.loadConfiguration(cratesFile);
		return crates;
	}
}