package me.dreamvoid.miraimc.bukkit.papi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.dreamvoid.miraimc.api.MiraiBot;
import me.dreamvoid.miraimc.api.MiraiMC;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;

public class PlaceholderAPIExpansion extends PlaceholderExpansion {
    // For development.
    private final boolean enforcePlugin = false;
    private final Plugin plugin = Bukkit.getPluginManager().getPlugin("MiraiMC");
    private final boolean hasPlugin;

    public PlaceholderAPIExpansion(){
        hasPlugin = plugin != null && plugin.isEnabled();
    }

    @Override
    public String getName() {
        return "MiraiMC";
    }

    @Override
    public String getIdentifier() {
        return "miraimc";
    }

    @Override
    public String getAuthor() {
        return "DreamVoid";
    }

    @Override
    public String getVersion() {
        return "1.2";
    }

    @Override
    public String getRequiredPlugin() {
        return enforcePlugin ? "MiraiMC" : null;  // I need to monitoring plugin status.
    }

    @Override
    public boolean canRegister() {
        return !enforcePlugin || (plugin != null && plugin.isEnabled()); // Let "isworking" working.
    }

    @Override
    public String onRequest(OfflinePlayer p, String params) {
        String[] args = params.split("_");
        switch (args[0].toLowerCase()) {
            case "isworking":{
                return String.valueOf(hasPlugin);
            }
            case "version":
                if(hasPlugin) return plugin.getDescription().getVersion();
                break;
            case "isonline":
                if(hasPlugin && args.length>=2) {
                    MiraiBot miraiBot = MiraiBot.getBot(Long.parseLong(args[1]));
                    if (miraiBot.isExist() && miraiBot.isOnline()) {
                        return "true";
                    } else return "false";
                }
                break;
            case "counts":
                if(hasPlugin) return String.valueOf(MiraiBot.getOnlineBots().toArray().length);
                break;
            case "nick":
                if(hasPlugin && args.length>=2){
                    MiraiBot bot = MiraiBot.getBot(Long.parseLong(args[1]));
                    if (bot.isExist() && bot.isOnline()) {
                        return bot.getNick();
                    }
                }
                break;
            case "friendcounts":
                if(hasPlugin && args.length>=2) {
                    MiraiBot miraiBot = MiraiBot.getBot(Long.parseLong(args[1]));
                    if (miraiBot.isExist() && miraiBot.isOnline()) {
                        return String.valueOf(miraiBot.getFriendList().toArray().length);
                    }
                }
                break;
            case "groupcounts":
                if(hasPlugin) {
                    MiraiBot miraiBot = MiraiBot.getBot(Long.parseLong(args[1]));
                    if (miraiBot.isExist() && miraiBot.isOnline()) {
                        return String.valueOf(miraiBot.getGroupList().toArray().length);
                    }
                }
                break;
            case "bindqq":
                if (hasPlugin && args.length >= 2) {
                    OfflinePlayer player = Bukkit.getPlayer(args[1]);
                    return String.valueOf(MiraiMC.getBind(player.getUniqueId()));
                } else return String.valueOf(MiraiMC.getBind(p.getUniqueId()));
            case "binduuid":
                if(hasPlugin && args.length >= 2) return MiraiMC.getBind(Long.parseLong(args[1])).toString();
                break;
            case "bindname":
                if(hasPlugin && args.length >= 2) return Bukkit.getOfflinePlayer(MiraiMC.getBind(Long.parseLong(args[1]))).getName();
                break;
            default:
                return null;
        }
        return null;
    }
}
