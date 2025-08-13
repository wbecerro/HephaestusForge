package wbe.hephaestusForge.config;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class Messages {

    private FileConfiguration config;

    public String notEnoughArgs;
    public String noPermission;
    public String itemGiven;
    public String itemGivenPlayer;
    public String itemGiveArgs;
    public String itemNotFound;
    public String itemUsed;
    public String reload;
    public String itemAdded;
    public String itemAlreadyExists;
    public List<String> help = new ArrayList<>();

    public Messages(FileConfiguration config) {
        this.config = config;

        notEnoughArgs = config.getString("Messages.notEnoughArgs").replace("&", "§");
        noPermission = config.getString("Messages.noPermission").replace("&", "§");
        itemGiven = config.getString("Messages.itemGiven").replace("&", "§");
        itemGivenPlayer = config.getString("Messages.itemGivenPlayer").replace("&", "§");
        itemGiveArgs = config.getString("Messages.itemGiveArgs").replace("&", "§");
        itemNotFound = config.getString("Messages.itemNotFound").replace("&", "§");
        itemUsed = config.getString("Messages.itemUsed").replace("&", "§");
        reload = config.getString("Messages.reload").replace("&", "§");
        itemAdded = config.getString("Messages.itemAdded").replace("&", "§");
        itemAlreadyExists = config.getString("Messages.itemAlreadyExists").replace("&", "§");
        help = config.getStringList("Messages.help");
    }
}
