package wbe.hephaestusForge.config;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import wbe.hephaestusForge.items.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Config {

    private FileConfiguration config;

    public HashMap<String, Item> items = new HashMap<>();

    public HashMap<String, ItemStack> savedItems = new HashMap<>();

    public Config(FileConfiguration config, FileConfiguration itemsConfig) {
        this.config = config;

        for(String item : config.getConfigurationSection("Items").getKeys(false)) {
            Material material = Material.valueOf(config.getString("Items." + item + ".material"));
            String name = config.getString("Items." + item + ".name").replace("&", "§");
            List<String> lore = config.getStringList("Items." + item + ".lore");
            List<String> commands = config.getStringList("Items." + item + ".commands");
            boolean glow = config.getBoolean("Items." + item + ".glow");

            items.put(item, new Item(material, name, lore, commands, glow));
        }

        ConfigurationSection section = itemsConfig.getConfigurationSection("Items");
        if(section == null) {
            return;
        }
        Set<String> savedConfigItems = section.getKeys(false);
        for(String savedItem : savedConfigItems) {
            ItemStack item = itemsConfig.getItemStack("Items." + savedItem);
            savedItems.put(savedItem, item);
        }
    }
}
