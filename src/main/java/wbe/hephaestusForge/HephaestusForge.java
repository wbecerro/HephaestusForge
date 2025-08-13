package wbe.hephaestusForge;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import wbe.hephaestusForge.commads.CommandListener;
import wbe.hephaestusForge.commads.TabListener;
import wbe.hephaestusForge.config.Config;
import wbe.hephaestusForge.config.Messages;
import wbe.hephaestusForge.listeners.EventListeners;
import wbe.hephaestusForge.util.RecipeManager;

import java.io.File;

public final class HephaestusForge extends JavaPlugin {

    private FileConfiguration configuration;

    private CommandListener commandListener;

    private TabListener tabListener;

    private EventListeners eventListeners;

    public static Config config;

    public static Messages messages;

    public File items;
    public static FileConfiguration itemsConfig;

    private File recipes;
    public static FileConfiguration recipesConfig;

    private RecipeManager recipeManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        createItemsFile();
        createRecipesFile();
        getLogger().info("Hephaestus' Forge enabled correctly.");
        reloadConfiguration();
        recipeManager = new RecipeManager(this, recipesConfig);

        recipeManager.loadRecipes();
        commandListener = new CommandListener();
        getCommand("hephaestusforge").setExecutor(this.commandListener);
        tabListener = new TabListener();
        getCommand("hephaestusforge").setTabCompleter(this.tabListener);
        eventListeners = new EventListeners();
        this.eventListeners.initializeListeners();
    }

    @Override
    public void onDisable() {
        reloadConfig();
        recipeManager.unloadRecipes();
        getLogger().info("Hephaestus' Forge disabled correctly.");
    }

    public static HephaestusForge getInstance() {
        return getPlugin(HephaestusForge.class);
    }

    public void reloadConfiguration() {
        if(!new File(getDataFolder(), "config.yml").exists()) {
            saveDefaultConfig();
        }
        reloadConfig();
        configuration = getConfig();
        messages = new Messages(configuration);
        config = new Config(configuration, itemsConfig);
    }

    private void createItemsFile() {
        items = new File(getDataFolder(), "items.yml");
        if(!items.exists()) {
            items.getParentFile().mkdirs();
            saveResource("items.yml", false);
        }

        itemsConfig = YamlConfiguration.loadConfiguration(items);
    }

    private void createRecipesFile() {
        recipes = new File(getDataFolder(), "recipes.yml");
        if(!recipes.exists()) {
            recipes.getParentFile().mkdirs();
            saveResource("recipes.yml", false);
        }

        recipesConfig = YamlConfiguration.loadConfiguration(recipes);
    }
}
