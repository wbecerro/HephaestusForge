package wbe.hephaestusForge.config;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import wbe.hephaestusForge.items.Item;

import java.util.HashMap;
import java.util.List;

public class Config {

    private FileConfiguration config;

    public HashMap<String,Item> items = new HashMap<>();

    public Config(FileConfiguration config) {
        this.config = config;

        for(String item : config.getConfigurationSection("Items").getKeys(false)) {
            Material material = Material.valueOf(config.getString("Items." + item + ".material"));
            String name = config.getString("Items." + item + ".name");
            List<String> lore = config.getStringList("Items." + item + ".lore");
            List<String> commands = config.getStringList("Items." + item + ".commands");
            boolean glow = config.getBoolean("Items." + item + ".glow");

            items.put(item, new Item(material, name, lore, commands, glow));
        }
    }
}
