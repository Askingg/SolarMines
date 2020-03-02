package prevail.askingg.solarmines.main;

import java.util.UUID;

import org.bukkit.entity.Player;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import prevail.askingg.solarmines.commands.CrateMoney;
import prevail.askingg.solarmines.enchanting.Tokens;
import prevail.askingg.solarmines.misc.Blocks;
import prevail.askingg.solarmines.progress.Ranks;
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
		User u = new User(p);

		// %sm_tokens%
		if (identifier.equalsIgnoreCase("tokens")) {
			return Core.decimals(0, Tokens.balance(p));
		}

		// %sm_broken%
		if (identifier.equalsIgnoreCase("broken")) {
			return Core.decimals(0, Blocks.getBlocks(p));
		}

		// %sm_marriage_prefix%
		if (identifier.equalsIgnoreCase("marriage_prefix")) {
			if (u.isMarried()) {
				return "&c&ki&8[&c♥&8]&c&ki";
			}
			return "&7&ki&8[&7♥&8]&7&ki";
		}

		// %sm_ascension_prefix%
		if (identifier.equalsIgnoreCase("ascension_prefix")) {
			int a = Ranks.getAscension(p);
			if (a > 0) {
				return "&c &ki&8[&c&l" + a + "&8]&c&ki";
			}
			return "&f";
		}

		// %sm_prestige_prefix%
		if (identifier.equalsIgnoreCase("prestige_prefix")) {
			int pr = Ranks.getPrestige(p);
			if (pr > 0) {
				return "&b &ki&8[&b&l" + pr + "&8]&b&ki";
			}
			return "&f";
		}

		// %sm_rank_prefix%
		if (identifier.equalsIgnoreCase("rank_prefix")) {
			return "&e &ki&8[&e&l" + SM.perms.getPrimaryGroup(p) + "&8]&e&ki&f";
		}

		// %sm_booster_general%
		if (identifier.equalsIgnoreCase("booster_general")) {
			UUID uuid = p.getUniqueId();
			long time = 0;
			if (Booster.globalMoneyExpire > 1L)
				time = Booster.globalMoneyExpire - System.currentTimeMillis();
			if (Booster.moneyExpire.containsKey(uuid))
				time = Booster.moneyExpire.get(uuid) - System.currentTimeMillis();
			if (time > 0L)
				return "&a" + Core.decimals(2, Booster.getMoneyBooster(p)) + "&8 (&b"
						+ Core.timeOneUnit((int) (time / 1000)) + "&8)";
			return "&a" + Core.decimals(2, Booster.getMoneyBooster(p));
		}

		// %sm_balance%
		if (identifier.equalsIgnoreCase("balance")) {
			return Core.number(SM.eco.getBalance(p));
		}

		// %sm_rankup_cost%
		if (identifier.equalsIgnoreCase("rankup_cost")) {
			String r = SM.perms.getPrimaryGroup(p);
			if (!Ranks.isFinalRank(r))
				return Core.number(Ranks.getCost(p));
			return "n/a";
		}

		// %sm_rankup_progress%
		if (identifier.equalsIgnoreCase("rankup_progress")) {
			String r = SM.perms.getPrimaryGroup(p);
			if (Ranks.ranks.contains(r) && !Ranks.isFinalRank(r)) {
				return Core.decimals(1, (SM.eco.getBalance(p) / Ranks.getCost(p)) * 100);
			}
			return "n/a";
		}

		// %sm_cratemoneys_<amount>%
		if (identifier.startsWith("cratemoney_")) {
			double d = Double.valueOf(identifier.split("cratemoney_")[1]);
			return "&a$" + Core.number(CrateMoney.getCrateMoney(p, d)) + "&8 (&7$" + Core.number(d) + "&8)";
		}

		return "&7&oUnknown placeholder";
	}
}