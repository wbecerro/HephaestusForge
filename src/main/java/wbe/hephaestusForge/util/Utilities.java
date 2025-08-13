package wbe.hephaestusForge.util;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import wbe.hephaestusForge.HephaestusForge;
import wbe.hephaestusForge.items.ExecutableItem;
import wbe.hephaestusForge.items.Item;

import java.io.IOException;

public class Utilities {

    public Item getItemByName(String name) {
        return HephaestusForge.config.items.get(name);
    }

    public boolean giveItem(Player player, String name) {
        Item item = getItemByName(name);
        if(item == null) {
            return false;
        }

        ExecutableItem executableItem = new ExecutableItem(item, name);
        if(player.getInventory().firstEmpty() == -1) {
            player.getWorld().dropItem(player.getLocation(), executableItem);
        } else {
            player.getInventory().addItem(executableItem);
        }
        player.updateInventory();

        player.sendMessage(HephaestusForge.messages.itemGivenPlayer
                .replace("%item%", executableItem.getItemMeta().getDisplayName()));

        return true;
    }

    public boolean addItem(ItemStack item, String identifier) {
        FileConfiguration itemsConfig = HephaestusForge.itemsConfig;
        if(itemsConfig.contains("Items." + identifier)) {
            return false;
        }

        itemsConfig.set("Items." + identifier, item);

        try {
            itemsConfig.save(HephaestusForge.getInstance().items);
            HephaestusForge.getInstance().reloadConfiguration();
            return true;
        } catch(IOException e) {
            throw new RuntimeException("Error while trying to save the items file.");
        }
    }
}
