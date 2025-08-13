package wbe.hephaestusForge.listeners;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import wbe.hephaestusForge.util.RecipeManager;

public class PlayerJoinListeners implements Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    public void discoverRecipesOnJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        for(NamespacedKey key : RecipeManager.keys) {
            player.discoverRecipe(key);
        }
    }
}
