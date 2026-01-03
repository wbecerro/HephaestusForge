package wbe.hephaestusForge.listeners;

import net.md_5.bungee.api.chat.TranslatableComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import wbe.hephaestusForge.HephaestusForge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MenuListeners implements Listener {

    private static void fillBorders(Inventory inventory) {
        ItemStack borde = new ItemStack(HephaestusForge.config.menuBorderMaterial);
        ItemMeta bordeMeta = borde.getItemMeta();
        NamespacedKey currentPage = new NamespacedKey(HephaestusForge.getInstance(), "recipemenu");
        bordeMeta.setDisplayName(" ");
        bordeMeta.getPersistentDataContainer().set(currentPage, PersistentDataType.BOOLEAN, true);
        borde.setItemMeta(bordeMeta);
        List<Integer> slots = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 13, 14, 15, 16, 17, 18, 22, 24, 26, 27,
                31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 50, 51, 52, 53);

        for(int slot : slots) {
            inventory.setItem(slot, borde);
        }
    }

    public static void fillShapedRecipe(Inventory inventory, ShapedRecipe recipe) {
        List<Integer> slots = Arrays.asList(10, 11, 12, 19, 20, 21, 28, 29, 30);
        String[] shape = recipe.getShape();
        Map<Character, ItemStack> ingredients = recipe.getIngredientMap();
        ArrayList<Character> ingredientCharacters = new ArrayList<>();
        for(String shapeLine : shape) {
            char[] characters = shapeLine.toCharArray();
            for(char character : characters) {
                ingredientCharacters.add(character);
            }
        }

        int size = ingredientCharacters.size();
        for(int i=0;i<size;i++) {
            inventory.setItem(slots.get(i), ingredients.get(ingredientCharacters.get(i)));
        }

        inventory.setItem(25, recipe.getResult());
    }
    public static void fillShapelessRecipe(Inventory inventory, ShapelessRecipe recipe) {
        List<Integer> slots = Arrays.asList(10, 11, 12, 19, 20, 21, 28, 29, 30);
        List<ItemStack> ingredients = recipe.getIngredientList();
        int ingredientsSize = ingredients.size();

        for(int i=0;i<ingredientsSize;i++) {
            inventory.setItem(slots.get(i), ingredients.get(i));
        }

        inventory.setItem(25, recipe.getResult());
    }

    public static void openMenu(Player player, CraftingRecipe recipe) throws Exception {
        String name = "";
        ItemStack result = recipe.getResult();
        if(result.getItemMeta() == null) {
            name = result.getType().toString().replace("_", " ").toLowerCase();
        } else {
            name = result.getItemMeta().getDisplayName();
            if(name.isEmpty()) {
                name = result.getType().toString().replace("_", " ").toLowerCase();
            }
        }
        Inventory inventory = Bukkit.createInventory(null, 54, HephaestusForge.config.menuTitle
                .replace("%item%", name));
        fillBorders(inventory);
        if(recipe instanceof ShapedRecipe shapedRecipe) {
            fillShapedRecipe(inventory, shapedRecipe);
        } else if(recipe instanceof ShapelessRecipe shapelessRecipe) {
            fillShapelessRecipe(inventory, shapelessRecipe);
        } else {
            throw new Exception(HephaestusForge.messages.recipeNotSupported);
        }
        inventory.setItem(23, HephaestusForge.config.menuResultItem);
        inventory.setItem(48, HephaestusForge.config.menuGoBackItem);
        inventory.setItem(49, HephaestusForge.config.menuCloseItem);

        player.openInventory(inventory);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onInventoryClick(InventoryClickEvent event) {
        ItemStack bordeItem = event.getInventory().getItem(0);
        if(bordeItem == null) {
            return;
        }

        NamespacedKey recipemenu = new NamespacedKey(HephaestusForge.getInstance(), "recipemenu");
        if(!bordeItem.getItemMeta().getPersistentDataContainer().has(recipemenu)) {
            return;
        }

        if(!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        ItemStack item = event.getCurrentItem();
        if(item == null || item.getType() == Material.AIR) {
            return;
        }

        NamespacedKey goBackKey = new NamespacedKey("hephaestusforge", "goback");
        NamespacedKey closeKey = new NamespacedKey("hephaestusforge", "close");

        // Clic en volver
        ItemMeta meta = item.getItemMeta();
        if(meta.getPersistentDataContainer().has(goBackKey)) {
            player.performCommand(HephaestusForge.config.menuGoBackCommand);
            event.setCancelled(true);
            return;
        }

        // Clic en cerrar
        if(meta.getPersistentDataContainer().has(closeKey)) {
            player.closeInventory();
            event.setCancelled(true);
            return;
        }

        event.setCancelled(true);
    }
}
