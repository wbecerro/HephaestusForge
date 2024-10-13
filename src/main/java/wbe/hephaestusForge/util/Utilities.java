package wbe.hephaestusForge.util;

import org.bukkit.entity.Player;
import wbe.hephaestusForge.HephaestusForge;
import wbe.hephaestusForge.items.ExecutableItem;
import wbe.hephaestusForge.items.Item;

public class Utilities {

    public Utilities() {

    }

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
}
