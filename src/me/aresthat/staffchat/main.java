package me.aresthat.staffchat;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;

import net.minecraft.server.v1_8_R1.ChatSerializer;
import net.minecraft.server.v1_8_R1.PacketPlayOutChat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;


public class main extends JavaPlugin implements Listener, CommandExecutor {
	
	public void onEnable() {
		Bukkit.getServer().getLogger().info( "†=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=†");
		Bukkit.getServer().getLogger().info("");
		Bukkit.getServer().getLogger().info("Plugin: " + this.getName() + " v"
				+ this.getDescription().getVersion());
		Bukkit.getServer().getLogger().info("Autore: AresThat");
		Bukkit.getServer().getLogger().info("");
		Bukkit.getServer().getLogger().info( "†=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=†");
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		getConfig().options().copyDefaults(true);
		saveConfig();
	}
	//Impostiamo la staffchat
	ArrayList<String> staff = new ArrayList<String>();
	@EventHandler
	public void onStaffChat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		if(staff.contains(p.getName())) {
			e.setCancelled(true);
			for(Player pl : Bukkit.getOnlinePlayers()) {
				if(staff.contains(pl.getName())){
					pl.sendMessage(ChatColor.RED + "STAFF" + ChatColor.WHITE + "> " + pl.getName() + ": " + e.getMessage());
				}
			}
		}
	}
	//Impostiamo i comandi
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage("You can't execute this command!");
			return true;
		}
		if(cmd.getName().equalsIgnoreCase("staffchat")) {
		Player p = (Player) sender;
			if(sender.hasPermission("sc.admin")) {
				if(staff.contains(p.getName())) {
					staff.remove(p.getName());
					p.sendMessage(ChatColor.RED + "(!) " + getConfig().getString("SC_Disabled"));
					 PacketPlayOutChat packet = new PacketPlayOutChat(ChatSerializer.a("{\"text\":\"" + getConfig().getString("SC_Disabled") + "\"}"), (byte) 2);
					 ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
					return true;
				}
				else {
					staff.add(p.getName());
					p.sendMessage(ChatColor.RED + "(!) " + getConfig().getString("SC_Enabled"));
					 PacketPlayOutChat packet = new PacketPlayOutChat(ChatSerializer.a("{\"text\":\"" + getConfig().getString("SC_Enabled") + "\"}"), (byte) 2);
					 ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
                    return true;
				}
			}
			else {
				p.sendMessage(getConfig().getString("No_Permissions"));
				return true;
			}
		
	
		}
		return true;
	}

}
