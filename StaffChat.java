package com.aProDude.StaffChat;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class StaffChat extends JavaPlugin implements Listener {

	public ArrayList<String> staff = new ArrayList<String>();

	FileConfiguration c = this.getConfig();

	public final Logger l = Bukkit.getLogger();

	public final PluginDescriptionFile pdf = this.getDescription();

	public void onEnable() {
		this.saveDefaultConfig();
		Bukkit.getPluginManager().registerEvents(this, this);
		this.saveResource("changelog.yml", true);
	}

	public void onDisable() {
		l.info(pdf.getName() + " has been disabled!");
	}

	String prefix = c.getString("StaffChat.Prefix");
	String noperm = c.getString("StaffChat.NoPermissionMessage");
	String instaff = c.getString("StaffChat.InStaffChatMessage");
	String outstaff = c.getString("StaffChat.OutStaffChatMessage");
	String alreadystaff = c.getString("StaffChat.AlreadyInStaffChatMessage");
	String notstaff = c.getString("StaffChat.NotInStaffChatMessage");

	@EventHandler
	public void onStaffChat(AsyncPlayerChatEvent e) {

		String staffchat = ChatColor.translateAlternateColorCodes('&', c.getString("StaffChat.Format"));
		staffchat = ChatColor.translateAlternateColorCodes('&', staffchat.replace("%prefix%", prefix));
		staffchat = ChatColor.translateAlternateColorCodes('&',
				staffchat.replace("%displayname%", ChatColor.LIGHT_PURPLE + e.getPlayer().getDisplayName()));
		staffchat = ChatColor.translateAlternateColorCodes('&',
				staffchat.replace("%message%", ChatColor.GREEN + e.getMessage()));

		Player p = e.getPlayer();

		if (staff.contains(p.getName())) {
			for (Player staff : Bukkit.getOnlinePlayers()) {
				if (staff.hasPermission("staffchat.use")) {
					e.setCancelled(true);
					staff.sendMessage(staffchat);
				}
			}

		}

	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		prefix = c.getString("StaffChat.Prefix");

		if (!(sender instanceof Player)) {
			cmsg(sender, "&cThe console can't use this command!");
		}

		Player p = (Player) sender;

		if (cmd.getName().equalsIgnoreCase("staffchat")) {
			if (args.length == 0) {
				if (!p.hasPermission("staffchat.use")) {
					msg(p, noperm);
					return true;
				}
				msg(p, "&c-----&bStaffChat&c-----");
				msg(p, "");
				msg(p, "&eInfo:");
				msg(p, "");
				msg(p, "&eCoded By: &a_xXProDudeXx_");
				msg(p, "&eVersion: 1.0.0");
				msg(p, "&eCommands:");
				msg(p, "");
				msg(p, "&c---&b/sc [on/off]&c---");
				msg(p, "&c---&b/sc reload &c---");
				msg(p, "");
				msg(p, "&c-----&bStaffChat&c-----");
				return true;
			}

			if (args[0].equalsIgnoreCase("reload") && args.length == 1) {
				if (!p.hasPermission("staffchat.use")) {
					msg(p, noperm);
					return true;
				}
				reloadConfig();
				msg(p, "&aConfig has been reloaded!");
			}

			if (args[0].equalsIgnoreCase("on") && args.length == 1) {
				if (!p.hasPermission("staffchat.use")) {
					msg(p, noperm);
					return true;
				}

				if (staff.contains(p.getName())) {
					msg(p, alreadystaff);
					return true;
				} else {
					staff.add(p.getName());
					msg(p, instaff);
					return true;
				}
			}

			if (args[0].equalsIgnoreCase("off") && args.length == 1) {
				if (!p.hasPermission("staffchat.use")) {
					msg(p, noperm);
					return true;
				}

				if (!staff.contains(p.getName())) {
					msg(p, notstaff);
					return true;
				} else {
					staff.remove(p.getName());
					msg(p, outstaff);
					return true;
				}
			}
		}

		return true;
	}

	public void msg(Player p, String msg) {
		msg = prefix + msg;
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
	}

	public void cmsg(CommandSender sender, String msg) {
		msg = prefix + msg;
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + msg));
	}

}
