package prevail.askingg.solarmines.enchanting.enchants;

import java.util.HashMap;

import org.bukkit.Material;

public class AutoSmelt {

	private static HashMap<Material, Material> smelting = new HashMap<Material, Material>();

	public static void setup() {
		smelting.put(Material.COBBLESTONE, Material.STONE);
		smelting.put(Material.IRON_ORE, Material.IRON_INGOT);
		smelting.put(Material.GOLD_ORE, Material.GOLD_INGOT);
	}

	public static boolean check(Material m) {
		if (smelting.containsKey(m))
			return true;
		return false;
	}

	public static Material getResult(Material m) {
		if (check(m))
			return smelting.get(m);
		return m;
	}

}
