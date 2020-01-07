package prevail.askingg.solarmines.main;

import java.util.UUID;

import org.bukkit.entity.Player;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import prevail.askingg.solarmines.enchanting.Tokens;
import prevail.askingg.solarmines.misc.Blocks;
import prevail.askingg.solarmines.sell.Booster;

public class Placeholders extends PlaceholderExpansion {

	public String getIdentifier() {
		return "sm";
	}

	public String getPlugin() {
		return null;
	}

	public String getAuthor() {
		return "Askingg";
	}

	public String getVersion() {
		return "1.0";
	}

	public String onPlaceholderRequest(Player p, String identifier) {

		// %sm_tokens%
		if (identifier.equalsIgnoreCase("tokens")) {
			return Core.decimals(0, Tokens.balance(p));
		}

		// %sm_broken%
		if (identifier.equalsIgnoreCase("broken")) {
			return Core.decimals(0, Blocks.getBlocks(p));
		}

		// %sm_booster_general%
		if (identifier.equalsIgnoreCase("booster_general")) {
			UUID u = p.getUniqueId();
			long time = 0;
			if (Booster.globalMoneyExpire > 1L)
				time = Booster.globalMoneyExpire - System.currentTimeMillis();
			if (Booster.moneyExpire.containsKey(u))
				time = Booster.moneyExpire.get(u) - System.currentTimeMillis();
			if (time > 0L)
				return "&a" + Core.decimals(0, Booster.getMoneyBooster(p)) + "&8 (&b"
						+ Core.timeOneUnit((int) (time / 1000)) + "&8)";
			return "&a" + Core.decimals(0, Booster.getMoneyBooster(p));
		}

		return "&7&oUnknown placeholder";
	}
}