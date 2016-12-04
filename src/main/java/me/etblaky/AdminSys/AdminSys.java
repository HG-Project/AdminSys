package me.etblaky.AdminSys;

import me.etblaky.vip.VipSys;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.kitteh.tag.AsyncPlayerReceiveNameTagEvent;
import org.kitteh.tag.PlayerReceiveNameTagEvent;
import org.kitteh.tag.TagAPI;

import java.util.List;

/**
 * Created by ETblaky on 30/11/2016.
 */
public class AdminSys extends JavaPlugin implements Listener{

    public static AdminSys instance;

    public enum PlayerLevel {
        REGULAR,
        VIP,
        ADMIN,
        DEVELOPER,
        OWNER
    }

    public void onEnable(){
        instance = this;
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
    }

    public static boolean isOwner(Player p){

        if(instance.getConfig().getStringList("Owners").contains(p.getName())){ return true; }

        return false;
    }

    public static boolean isAdmin(Player p){
        if(instance.getConfig().getStringList("Admins").contains(p.getName())){ return true; }

        return false;
    }

    public static boolean isDeveloper(Player p){
        if(instance.getConfig().getStringList("Developers").contains(p.getName())){ return true; }

        return false;
    }

    public static PlayerLevel getLevel(Player p){

        if(VipSys.isVip(p)) {return PlayerLevel.VIP;}
        if(isOwner(p)) {return PlayerLevel.OWNER;}
        if(isDeveloper(p)) {return PlayerLevel.DEVELOPER;}
        if(isAdmin(p)) {return PlayerLevel.ADMIN;}

        return PlayerLevel.REGULAR;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){

        if(isOwner(e.getPlayer()) || isAdmin(e.getPlayer()) || isDeveloper(e.getPlayer())){
            TagAPI.refreshPlayer(e.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerReceiveTag(AsyncPlayerReceiveNameTagEvent e){
        if(isOwner(e.getPlayer())){
           e.setTag(ChatColor.RED + "Dono " + ChatColor.WHITE + e.getNamedPlayer().getName());
        }
        else if(isAdmin(e.getPlayer())){
            e.setTag(ChatColor.DARK_PURPLE + "Admin " + ChatColor.WHITE + e.getNamedPlayer().getName());
        }
        else if(isDeveloper(e.getPlayer())){
            e.setTag(ChatColor.GREEN + "Dev " + ChatColor.WHITE + e.getNamedPlayer().getName());
        }
        else{
            e.setTag(e.getNamedPlayer().getName());
        }
    }

    @EventHandler
    public void playerRecieveTag(PlayerReceiveNameTagEvent e){
        System.out.println("Is receiving the tag");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(cmd.getName().equalsIgnoreCase("Owner")){
            if(!isOwner((Player) sender) && !isDeveloper((Player) sender) && !(sender instanceof ConsoleCommandSender)) { sender.sendMessage(ChatColor.RED + "Você não tem permissão para isso!");return true; }
            if(args.length < 1) { sender.sendMessage(ChatColor.YELLOW + "Você precisa especificar um jogador!"); return true; }
            if(this.getConfig().getStringList("Owners").contains(args[0])) { sender.sendMessage(ChatColor.RED + "Ele já é um Dono!"); return  true;}

            List<String> list = this.getConfig().getStringList("Owners");
            list.add(args[0]);

            this.getConfig().set("Owners", list);

            if(Bukkit.getPlayer(args[0]) == null) return true;
            TagAPI.refreshPlayer(Bukkit.getPlayer(args[0]));

            Bukkit.getPlayer(args[0]).kickPlayer("Reenter the game to receive your tag!");

        }

        if(cmd.getName().equalsIgnoreCase("Admin")){
            if(!isOwner((Player) sender) && !isDeveloper((Player) sender) && !(sender instanceof ConsoleCommandSender)) { sender.sendMessage(ChatColor.RED + "Você não tem permissão para isso!");return true; }
            if(args.length < 1) { sender.sendMessage("Você precisa especificar um jogador!"); return true; }
            if(this.getConfig().getStringList("Admins").contains(args[0])) { sender.sendMessage(ChatColor.RED + "Ele já é um Admin!"); return  true;}

            List<String> list = this.getConfig().getStringList("Admins");
            list.add(args[0]);

            this.getConfig().set("Admins", list);

            if(Bukkit.getPlayer(args[0]) == null) return true;
            TagAPI.refreshPlayer(Bukkit.getPlayer(args[0]));

            Bukkit.getPlayer(args[0]).kickPlayer("Reenter the game to receive your tag!");

        }

        if(cmd.getName().equalsIgnoreCase("Dev")){
            if(sender instanceof Player && !isOwner((Player) sender) && !isDeveloper((Player) sender)) { sender.sendMessage(ChatColor.RED + "Você não tem permissão para isso!");return true; }
            if(args.length < 1) { sender.sendMessage("Você precisa especificar um jogador!"); return true; }
            if(this.getConfig().getStringList("Developers").contains(args[0])) { sender.sendMessage(ChatColor.RED + "Ele já é um Dev!"); return  true;}

            List<String> list = this.getConfig().getStringList("Developers");
            list.add(args[0]);

            this.getConfig().set("Developers", list);

            if(Bukkit.getPlayer(args[0]) == null) return true;
            TagAPI.refreshPlayer(Bukkit.getPlayer(args[0]));

            Bukkit.getPlayer(args[0]).kickPlayer("Reenter the game to receive your tag!");

        }
        this.saveConfig();

        return true;
    }

}
