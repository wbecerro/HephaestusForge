package wbe.hephaestusForge.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.inventory.ItemStack;
import wbe.hephaestusForge.HephaestusForge;
import wbe.hephaestusForge.config.LootTableItem;
import wbe.hephaestusForge.util.Utilities;

import java.util.*;

public class LootGenerateListeners implements Listener {

    private Utilities utilities = new Utilities();

    public static HashMap<Location, UUID> pendingLoot = new HashMap<>();

    @EventHandler(priority = EventPriority.NORMAL)
    public void handleLootTableModification(LootGenerateEvent event) {
        if(event.isCancelled()) {
            return;
        }

        Set<LootTableItem> extraLoot = utilities.getLootTableItems(event.getLootTable().getKey());
        if(extraLoot == null) {
            return;
        }

        List<ItemStack> loot = event.getLoot();
        UUID uuid = pendingLoot.get(event.getInventoryHolder().getInventory().getLocation());
        double luckMultiplier = 1;
        Player player;
        if(uuid != null) {
            player = Bukkit.getPlayer(uuid);
            luckMultiplier = 1 + HephaestusForge.config.luckLevelMultiplier * player.getAttribute(Attribute.GENERIC_LUCK).getValue();
        }

        Random random = new Random();
        for(LootTableItem item : extraLoot) {
            if(random.nextDouble(100) < item.getChance() * luckMultiplier) {
                loot.add(item.generateItem());
            }
        }
    }
}
