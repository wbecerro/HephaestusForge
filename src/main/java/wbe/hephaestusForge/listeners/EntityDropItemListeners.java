package wbe.hephaestusForge.listeners;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Sniffer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDropItemEvent;
import wbe.hephaestusForge.HephaestusForge;
import wbe.hephaestusForge.util.Utilities;

public class EntityDropItemListeners implements Listener {

    private Utilities utilities = new Utilities();

    @EventHandler(priority = EventPriority.NORMAL)
    public void handleSpecialSnifferDrops(EntityDropItemEvent event) {
        if(event.isCancelled()) {
            return;
        }

        if(!(event.getEntity() instanceof Sniffer sniffer)) {
            return;
        }

        NamespacedKey snifferKey = new NamespacedKey(HephaestusForge.getInstance(), "specialSniffer");
        if(!(sniffer.getPersistentDataContainer().has(snifferKey))) {
            return;
        }

        event.getItemDrop().setItemStack(utilities.getRandomReward().getItem());
    }
}
