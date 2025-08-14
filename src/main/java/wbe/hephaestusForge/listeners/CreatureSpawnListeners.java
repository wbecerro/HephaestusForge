package wbe.hephaestusForge.listeners;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Piglin;
import org.bukkit.entity.WanderingTrader;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import wbe.hephaestusForge.HephaestusForge;
import wbe.hephaestusForge.config.WanderingRecipe;
import wbe.hephaestusForge.util.Utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CreatureSpawnListeners implements Listener {

    private Utilities utilities = new Utilities();

    @EventHandler(priority = EventPriority.NORMAL)
    public void handleSpecialWanderingTraders(CreatureSpawnEvent event) {
        if(event.isCancelled()) {
            return;
        }

        if(!event.getEntityType().equals(EntityType.WANDERING_TRADER)) {
            return;
        }

        Random random = new Random();
        if(random.nextDouble(100) > HephaestusForge.config.wanderingChance) {
            return;
        }

        WanderingTrader wanderingTrader = (WanderingTrader) event.getEntity();
        wanderingTrader.setCustomName(HephaestusForge.config.wanderingName);
        wanderingTrader.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, PotionEffect.INFINITE_DURATION, 1));

        List<MerchantRecipe> recipes = new ArrayList<>(wanderingTrader.getRecipes());
        WanderingRecipe recipe = utilities.getRandomRecipe();

        recipes.add(recipe.createRecipe());
        wanderingTrader.setRecipes(recipes);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void handleNewPiglinTrades(CreatureSpawnEvent event) {
        if(event.isCancelled()) {
            return;
        }

        if(!event.getEntityType().equals(EntityType.PIGLIN)) {
            return;
        }

        Piglin piglin = (Piglin) event.getEntity();
        for(Material material : HephaestusForge.config.interestItems) {
            piglin.addMaterialOfInterest(material);
            piglin.addBarterMaterial(material);
        }
    }
}
