package wbe.hephaestusForge.config;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootTables;
import wbe.hephaestusForge.items.Item;

import java.util.*;

public class Config {

    private FileConfiguration config;

    private FileConfiguration itemsConfig;

    public double wanderingChance;
    public int maxExtraTrades;
    public String wanderingName;
    public boolean wanderingGlow;

    public int vanillaPiglinWeight;
    public List<Material> interestItems = new ArrayList<>();
    public Set<PiglinTrade> piglinTrades = new HashSet<>();

    public double luckLevelMultiplier;

    public HashMap<String, Item> items = new HashMap<>();

    public HashMap<String, ItemStack> savedItems = new HashMap<>();

    public Set<WanderingRecipe> wanderingRecipes = new HashSet<>();

    public HashMap<LootTables, Set<LootTableItem>> lootTables = new HashMap<>();

    public int wanderingRecipesMaxWeight = 0;
    public int piglinTotalWeight = 0;

    public Config(FileConfiguration config, FileConfiguration itemsConfig) {
        this.config = config;
        this.itemsConfig = itemsConfig;

        wanderingChance = config.getDouble("Wandering.chance");
        maxExtraTrades = config.getInt("Wandering.maxExtraTrades");
        wanderingName = config.getString("Wandering.villager.name").replace("&", "§");
        wanderingGlow = config.getBoolean("Wandering.villager.glow");

        vanillaPiglinWeight = config.getInt("Piglin.vanillaPiglinWeight");
        config.getStringList("Piglin.interestItems").forEach((material) -> {
            interestItems.add(Material.valueOf(material));
        });

        luckLevelMultiplier = config.getDouble("LuckLevelMultiplier");

        loadExecutableItems();
        loadSavedItems();
        loadWanderingRecipes();
        loadPiglinTrades();
        loadLootTables();
    }

    private void loadExecutableItems() {
        for(String item : config.getConfigurationSection("Items").getKeys(false)) {
            Material material = Material.valueOf(config.getString("Items." + item + ".material"));
            String name = config.getString("Items." + item + ".name").replace("&", "§");
            List<String> lore = config.getStringList("Items." + item + ".lore");
            List<String> commands = config.getStringList("Items." + item + ".commands");
            boolean glow = config.getBoolean("Items." + item + ".glow");

            items.put(item, new Item(material, name, lore, commands, glow));
        }
    }

    private void loadSavedItems() {
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

    private void loadWanderingRecipes() {
        Set<String> recipes = config.getConfigurationSection("Wandering.trades").getKeys(false);
        for(String recipe : recipes) {
            int weight = config.getInt("Wandering.trades." + recipe + ".weight");
            wanderingRecipesMaxWeight += weight;
            int maxUses = config.getInt("Wandering.trades." + recipe + ".maxUses");
            ItemStack result = parseItem("Wandering.trades." + recipe + ".result");
            ItemStack ingredient1 = parseItem("Wandering.trades." + recipe + ".ingredients.1");
            ItemStack ingredient2 = null;
            if(config.contains("Wandering.trades." + recipe + ".ingredients.2")) {
                ingredient2 = parseItem("Wandering.trades." + recipe + ".ingredients.2");
            }
            wanderingRecipes.add(new WanderingRecipe(recipe, maxUses, weight, result, ingredient1, ingredient2));
        }
    }

    private void loadPiglinTrades() {
        Set<String> trades = config.getConfigurationSection("Piglin.trades").getKeys(false);
        for(String trade : trades) {
            int weight = config.getInt("Piglin.trades." + trade + ".weight");
            piglinTotalWeight += weight;
            ItemStack item = parseItem("Piglin.trades." + trade);
            piglinTrades.add(new PiglinTrade(trade, weight, item));
        }
    }

    private void loadLootTables() {
        Set<String> configLootTables = config.getConfigurationSection("LootTables").getKeys(false);
        for(String lootTable : configLootTables) {
            lootTables.put(LootTables.valueOf(lootTable), loadLootTableItems(lootTable));
        }
    }

    private Set<LootTableItem> loadLootTableItems(String lootTable) {
        Set<String> items = config.getConfigurationSection("LootTables." + lootTable).getKeys(false);
        Set<LootTableItem> lootTableItems = new HashSet<>();
        for(String lootTableItem : items) {
            double chance = config.getDouble("LootTables." + lootTable + "." + lootTableItem + ".chance");
            ItemStack item = parseItem("LootTables." + lootTable + "." + lootTableItem);
            int min = config.getInt("LootTables." + lootTable + "." + lootTableItem + ".min");
            int max = config.getInt("LootTables." + lootTable + "." + lootTableItem + ".max");
            lootTableItems.add(new LootTableItem(lootTableItem, chance, item, min, max));
        }

        return lootTableItems;
    }

    private ItemStack parseItem(String path) {
        String type = config.getString(path + ".type");
        String material = config.getString(path + ".item");
        int amount = 1;
        if(config.contains(path + ".amount")) {
            amount = config.getInt(path + ".amount");
        }

        if(type.equalsIgnoreCase("material")) {
            return new ItemStack(Material.valueOf(material), amount);
        } else {
            ItemStack item = savedItems.get(material).clone();
            item.setAmount(amount);
            return item;
        }
    }
}
