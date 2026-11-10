package wbe.hephaestusForge.config;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.loot.LootTables;
import org.bukkit.persistence.PersistentDataType;
import wbe.hephaestusForge.items.Item;

import java.util.*;

public class Config {

    private FileConfiguration config;

    private FileConfiguration itemsConfig;

    public String menuTitle;
    public Material menuBorderMaterial;
    public ItemStack menuResultItem;
    public ItemStack menuGoBackItem;
    public String menuGoBackCommand;
    public ItemStack menuCloseItem;

    public double wanderingChance;
    public int maxExtraTrades;
    public String wanderingName;
    public boolean wanderingGlow;

    public int vanillaPiglinWeight;
    public Set<PiglinTradeTier> piglinTiers = new HashSet<>();
    public List<Material> interestItems = new ArrayList<>();

    public double snifferChance;
    public String snifferName;
    public boolean snifferGlow;
    public Set<SnifferReward> snifferRewards = new HashSet<>();

    public double luckLevelMultiplier;

    public HashMap<String, Item> items = new HashMap<>();

    public HashMap<String, ItemStack> savedItems = new HashMap<>();

    public Set<WanderingRecipe> wanderingRecipes = new HashSet<>();

    public HashMap<LootTables, Set<LootTableItem>> lootTables = new HashMap<>();

    public int wanderingRecipesMaxWeight = 0;
    public int snifferTotalWeight = 0;

    public Config(FileConfiguration config, FileConfiguration itemsConfig) {
        this.config = config;
        this.itemsConfig = itemsConfig;

        loadSavedItems();

        menuTitle = config.getString("Menu.title").replace("&", "§");
        menuBorderMaterial = Material.valueOf(config.getString("Menu.borderMaterial"));

        String resultName = config.getString("Menu.resultItem.name").replace("&", "§");
        Material resultMaterial = Material.valueOf(config.getString("Menu.resultItem.material"));
        menuResultItem = new ItemStack(resultMaterial);
        ItemMeta resultMeta = menuResultItem.getItemMeta();
        resultMeta.setDisplayName(resultName);
        menuResultItem.setItemMeta(resultMeta);

        String goBackName = config.getString("Menu.goBackItem.name").replace("&", "§");
        Material goBackMaterial = Material.valueOf(config.getString("Menu.goBackItem.material"));
        menuGoBackItem = new ItemStack(goBackMaterial);
        ItemMeta goBackMeta = menuGoBackItem.getItemMeta();
        goBackMeta.setDisplayName(goBackName);
        NamespacedKey goBackKey = new NamespacedKey("hephaestusforge", "goback");
        goBackMeta.getPersistentDataContainer().set(goBackKey, PersistentDataType.BOOLEAN, true);
        menuGoBackItem.setItemMeta(goBackMeta);
        menuGoBackCommand = config.getString("Menu.goBackItem.command");

        String closeName = config.getString("Menu.closeItem.name").replace("&", "§");
        Material closeMaterial = Material.valueOf(config.getString("Menu.closeItem.material"));
        menuCloseItem = new ItemStack(closeMaterial);
        ItemMeta closeMeta = menuCloseItem.getItemMeta();
        closeMeta.setDisplayName(closeName);
        NamespacedKey closeKey = new NamespacedKey("hephaestusforge", "close");
        closeMeta.getPersistentDataContainer().set(closeKey, PersistentDataType.BOOLEAN, true);
        menuCloseItem.setItemMeta(closeMeta);

        wanderingChance = config.getDouble("Wandering.chance");
        maxExtraTrades = config.getInt("Wandering.maxExtraTrades");
        wanderingName = config.getString("Wandering.villager.name").replace("&", "§");
        wanderingGlow = config.getBoolean("Wandering.villager.glow");

        vanillaPiglinWeight = config.getInt("Piglin.vanillaPiglinWeight");
        config.getStringList("Piglin.interestItems").forEach((material) -> {
            interestItems.add(Material.valueOf(material));
        });

        snifferChance = config.getDouble("Sniffer.chance");
        snifferName = config.getString("Sniffer.name").replace("&", "§");
        snifferGlow = config.getBoolean("Sniffer.glow");

        luckLevelMultiplier = config.getDouble("LuckLevelMultiplier");

        loadExecutableItems();
        loadWanderingRecipes();
        loadPiglinTiers();
        loadSnifferRewards();
        loadLootTables();
    }

    private void loadExecutableItems() {
        for(String item : config.getConfigurationSection("Items").getKeys(false)) {
            Material material = Material.valueOf(config.getString("Items." + item + ".material"));
            String name = config.getString("Items." + item + ".name").replace("&", "§");
            List<String> lore = config.getStringList("Items." + item + ".lore");
            List<String> commands = config.getStringList("Items." + item + ".commands");
            boolean glow = config.getBoolean("Items." + item + ".glow");
            boolean infinite = config.getBoolean("Items." + item + ".infinite");

            items.put(item, new Item(material, name, lore, commands, glow, infinite));
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

    private void loadPiglinTiers() {
        Set<String> tiers = config.getConfigurationSection("Piglin.trades").getKeys(false);
        for(String tier : tiers) {
            Set<Material> interests = new HashSet<>();
            config.getStringList("Piglin.trades." + tier + ".interests").forEach((material) -> {
                interests.add(Material.valueOf(material));
            });

            Set<String> trades = config.getConfigurationSection("Piglin.trades." + tier + ".rewards").getKeys(false);
            Set<PiglinTrade> piglinTrades = new HashSet<>();
            int totalWeight = 0;
            for(String trade : trades) {
                int weight = config.getInt("Piglin.trades." + tier + ".rewards." + trade + ".weight");
                totalWeight += weight;
                ItemStack item = parseItem("Piglin.trades." + tier + ".rewards." + trade);
                piglinTrades.add(new PiglinTrade(trade, weight, item));
            }

            piglinTiers.add(new PiglinTradeTier(tier, totalWeight, piglinTrades, interests));
        }
    }

    private void loadSnifferRewards() {
        Set<String> trades = config.getConfigurationSection("Sniffer.items").getKeys(false);
        for(String trade : trades) {
            int weight = config.getInt("Sniffer.items." + trade + ".weight");
            snifferTotalWeight += weight;
            ItemStack item = parseItem("Sniffer.items." + trade);
            snifferRewards.add(new SnifferReward(trade, weight, item));
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