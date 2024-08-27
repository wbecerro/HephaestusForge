package wbe.hephaestusForge.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
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

    public PlayerInteractListeners() {

    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void executeItemOnInteract(PlayerInteractEvent event) {
        ItemStack item = event.getItem();

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

        Item executable = utilities.getItemByName(executableItem);
        for(String command : executable.getCommands()) {
            command = command.replace("%player%", player.getName());
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
        }

        player.sendMessage(HephaestusForge.messages.itemUsed);
        event.setCancelled(true);
    }
}
