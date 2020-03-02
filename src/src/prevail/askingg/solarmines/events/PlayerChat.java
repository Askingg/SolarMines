package prevail.askingg.solarmines.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import prevail.askingg.solarmines.main.Config;
import prevail.askingg.solarmines.main.Core;
import prevail.askingg.solarmines.misc.Tablist;

public class PlayerChat implements Listener {

	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		if (!e.isCancelled()) {
			Player p = e.getPlayer();
			String m = "";
			String h = "";
			String c = "";
			String message;
			if (p.hasPermission("solar.chat.color")) {
				message = e.getMessage();
			} else {
				message = ChatColor.stripColor(e.getMessage().replace("&", "§"));
			}
			if (Config.player.contains(p.getName())) {
				m = color(p, Core.papi(p, Config.playerChat.get(p.getName())).replace("%message%", message)
						.replace("%player%", p.getName()));

				if (Config.playerHover.get(p.getName()).size() > 0) {
					for (String s : Config.playerHover.get(p.getName())) {
						h += (Core.papi(p, s).replace("%player%", p.getName()) + "\n");
					}
				}
				c = Core.papi(p, (Config.playerClick.get(p.getName()).replace("%player%", p.getName())));
			} else {
				for (String s : Config.perm) {
					if (p.hasPermission("solar.chat." + s)) {
						m = color(p, Core.papi(p, Config.permChat.get(s)).replace("%message%", message)
								.replace("%player%", p.getName()));
						if (Config.permHover.get(s).size() > 0) {
							for (String st : Config.permHover.get(s)) {
								h += (Core.papi(p, st).replace("%player%", p.getName()) + "\n");
							}
						}
						c = Core.papi(p, Config.permClick.get(s).replace("%player%", p.getName()));
						break;
					}
				}
			}
			if (m.equals("")) {
				String s = Config.perm.get(Config.perm.size() - 1);
				m = color(p, Core.papi(p, Config.permChat.get(s)).replace("%message%", message).replace("%player%",
						p.getName()));
				if (Config.permHover.get(s).size() > 0) {
					for (String st : Config.permHover.get(s)) {
						h += (Core.papi(p, st).replace("%player%", p.getName()) + "\n");
					}
				}
				c = Core.papi(p, Config.permClick.get(s)).replace("%player%", p.getName());
			}
			for (Player pl : Bukkit.getOnlinePlayers()) {
				chat(pl, Core.color(m), c, h.replace("\\n", System.lineSeparator()));
			}
			Core.console(m);
			e.setCancelled(true);
			p.setPlayerListName(Tablist.getFormat(p));
		}
	}

	public static void chat(Player p, String chat, String click, String hover) {
		p.spigot().sendMessage(new ComponentBuilder(color(p, chat))
				.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(color(p, hover)).create()))
				.event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, click)).create());
	}

	private static String color(Player p, String s) {
		String m = "";
		String[] arg = s.split(" ");
		for (int x = 0; x < arg.length; x++) {
			if (x > 0) {
				m += " " + ChatColor.getLastColors(m.replace("&", "§")) + arg[x];
			} else {
				m += arg[x];
			}
		}
		return m;
	}
}
