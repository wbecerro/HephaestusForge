package wbe.hephaestusForge.listeners;

import org.bukkit.plugin.PluginManager;
import wbe.hephaestusForge.HephaestusForge;

public class EventListeners {

    private HephaestusForge plugin = HephaestusForge.getInstance();

    public void initializeListeners() {
        PluginManager pluginManager = plugin.getServer().getPluginManager();

        pluginManager.registerEvents(new PlayerInteractListeners(), plugin);
        pluginManager.registerEvents(new PlayerJoinListeners(), plugin);
        pluginManager.registerEvents(new CreatureSpawnListeners(), plugin);
        pluginManager.registerEvents(new PiglinBarterListeners(), plugin);
        pluginManager.registerEvents(new LootGenerateListeners(), plugin);
    }
}
