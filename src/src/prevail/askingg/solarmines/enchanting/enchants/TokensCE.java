package prevail.askingg.solarmines.enchanting.enchants;

import java.util.Random;

public class TokensCE {

	public static int getTokens(int lvl) {
		int max = 5;
		int min = 3;
		if (lvl > 0) {
			max = 5 + (lvl * 5);
			min = 3 + (lvl * 3);
		}
		return new Random().nextInt((max - min) + 1) + min;
	}
}
