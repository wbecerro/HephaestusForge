package wbe.hephaestusForge.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PiglinBarterEvent;
import org.bukkit.inventory.ItemStack;
import wbe.hephaestusForge.HephaestusForge;
import wbe.hephaestusForge.config.PiglinTradeTier;
import wbe.hephaestusForge.util.Utilities;

import java.util.List;
import java.util.Random;

public class PiglinBarterListeners implements Listener {

    private Utilities utilities = new Utilities();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handleCustomPiglinBarter(PiglinBarterEvent event) {
        if(event.isCancelled()) {
            return;
        }

        PiglinTradeTier tier = utilities.getTierByInput(event.getInput().getType());

        if(tier == null) {
            return;
        }

        int totalWeight = HephaestusForge.config.vanillaPiglinWeight + tier.getTotalWeight();
        Random random = new Random();
        if(random.nextInt(totalWeight) <= HephaestusForge.config.vanillaPiglinWeight) {
            return;
        }

        List<ItemStack> outcome = event.getOutcome();
        outcome.clear();
        outcome.add(tier.getRandomReward().getItem().clone());
    }
}
