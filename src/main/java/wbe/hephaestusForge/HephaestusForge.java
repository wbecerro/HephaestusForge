package wbe.hephaestusForge;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import wbe.hephaestusForge.commads.CommandListener;
import wbe.hephaestusForge.config.Config;
import wbe.hephaestusForge.config.Messages;
import wbe.hephaestusForge.listeners.EventListeners;

import java.io.File;

public final class HephaestusForge extends JavaPlugin {

    private FileConfiguration configuration;

    private CommandListener commandListener;

    private EventListeners eventListeners;

    public static Config config;

    public static Messages messages;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getLogger().info("HephaestusForge enabled correctly.");
        reloadConfiguration();

        commandListener = new CommandListener();
        getCommand("hephaestusforge").setExecutor(this.commandListener);
        eventListeners = new EventListeners();
        this.eventListeners.initializeListeners();
    }

    @Override
    public void onDisable() {
        reloadConfig();
        getLogger().info("HephaestusForge disabled correctly.");
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
        config = new Config(configuration);
    }
}
