package wbe.hephaestusForge.listeners;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import wbe.hephaestusForge.HephaestusForge;
import wbe.hephaestusForge.items.Item;
import wbe.hephaestusForge.util.Utilities;

public class PlayerInteractListeners implements Listener {

    private HephaestusForge plugin = HephaestusForge.getInstance();

    private Utilities utilities = new Utilities();

    @EventHandler(priority = EventPriority.NORMAL)
    public void executeItemOnInteract(PlayerInteractEvent event) {
        if(!event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            if(!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                return;
            }
        }

        ItemStack item = event.getItem();
        if(item == null) {
            return;
        }

        if(item.getType().equals(Material.AIR)) {
            return;
        }

        ItemMeta meta = item.getItemMeta();
        if(meta == null) {
            return;
        }

        NamespacedKey itemKey = new NamespacedKey(plugin, "ForgeItem");
        if(!meta.getPersistentDataContainer().has(itemKey)) {
            return;
        }

        String executableItem = meta.getPersistentDataContainer().get(itemKey, PersistentDataType.STRING);
        Player player = event.getPlayer();
        if(!player.hasPermission("hephaestusforge.item." + executableItem)) {
            player.sendMessage(HephaestusForge.messages.noPermission);
            return;
        }

        Location playerLocation = player.getLocation();
        String location = player.getWorld().getName() + "," + playerLocation.getX() + "," + playerLocation.getY() + "," + playerLocation.getZ();
        Item executable = utilities.getItemByName(executableItem);
        for(String command : executable.getCommands()) {
            command = command.replace("%player%", player.getName())
                    .replace("%player_world%", player.getWorld().getName())
                    .replace("%location%", location);
            command = PlaceholderAPI.setPlaceholders(player, command);
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
        }

        item.setAmount(item.getAmount() - 1);
        player.sendMessage(HephaestusForge.messages.itemUsed);
        event.setCancelled(true);
    }
}
