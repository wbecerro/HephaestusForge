package wbe.hephaestusForge.util;

import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootTables;
import wbe.hephaestusForge.HephaestusForge;
import wbe.hephaestusForge.config.LootTableItem;
import wbe.hephaestusForge.config.PiglinTrade;
import wbe.hephaestusForge.config.WanderingRecipe;
import wbe.hephaestusForge.items.ExecutableItem;
import wbe.hephaestusForge.items.Item;

import java.io.IOException;
import java.util.Random;
import java.util.Set;

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

    public WanderingRecipe getRandomRecipe() {
        Random random = new Random();
        double randomNumber = random.nextDouble(HephaestusForge.config.wanderingRecipesMaxWeight);
        double weight = 0;
        Set<WanderingRecipe> recipes = HephaestusForge.config.wanderingRecipes;

        for(WanderingRecipe recipe : recipes) {
            weight += recipe.getWeight();
            if(randomNumber < weight) {
                return recipe;
            }
        }

        return recipes.stream().toList().getLast();
    }

    public PiglinTrade getRandomTrade() {
        Random random = new Random();
        double randomNumber = random.nextDouble(HephaestusForge.config.piglinTotalWeight);
        double weight = 0;
        Set<PiglinTrade> trades = HephaestusForge.config.piglinTrades;

        for(PiglinTrade trade : trades) {
            weight += trade.getWeight();
            if(randomNumber < weight) {
                return trade;
            }
        }

        return trades.stream().toList().getLast();
    }

    public Set<LootTableItem> getLootTableItems(NamespacedKey key) {
        for(LootTables lootTable : HephaestusForge.config.lootTables.keySet()) {
            if(lootTable.getKey().equals(key)) {
                return HephaestusForge.config.lootTables.get(lootTable);
            }
        }

        return null;
    }
}
