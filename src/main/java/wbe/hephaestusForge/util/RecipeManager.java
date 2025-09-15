package wbe.hephaestusForge.util;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import wbe.hephaestusForge.HephaestusForge;

import java.util.*;

public class RecipeManager {

    private HephaestusForge plugin;

    private FileConfiguration recipeConfig;

    public static List<NamespacedKey> keys = new ArrayList<>();

    public RecipeManager(HephaestusForge plugin, FileConfiguration recipeConfig) {
        this.plugin = plugin;
        this.recipeConfig = recipeConfig;
    }

    public void loadRecipes() {
        loadConfigRecipes();
    }

    public void unloadRecipes() {
        for(NamespacedKey key : keys) {
            plugin.getServer().removeRecipe(key);
        }
    }

    public void loadShapedRecipe(String id, String[] shape, HashMap<Character, ItemStack> ingredients) {
        NamespacedKey recipeKey = new NamespacedKey(plugin, id + "recipe");
        ShapedRecipe recipe = new ShapedRecipe(recipeKey, getResult(id));
        recipe.shape(shape);
        for(Map.Entry<Character, ItemStack> ingredient : ingredients.entrySet()) {
            ItemStack item = ingredient.getValue();
            recipe.setIngredient(ingredient.getKey(), new RecipeChoice.ExactChoice(item));
        }

        plugin.getServer().addRecipe(recipe);
        keys.add(recipeKey);
    }

    public void loadShapelessRecipe(String id, HashMap<ItemStack, Integer> ingredients) {
        NamespacedKey recipeKey = new NamespacedKey(plugin, id + "recipe");
        ShapelessRecipe recipe = new ShapelessRecipe(recipeKey, getResult(id));
        for(Map.Entry<ItemStack, Integer> ingredient : ingredients.entrySet()) {
            for(int i=0;i<ingredient.getValue();i++) {
                recipe.addIngredient(new RecipeChoice.ExactChoice(ingredient.getKey()));
            }
        }

        plugin.getServer().addRecipe(recipe);
        keys.add(recipeKey);
    }

    private ItemStack getResult(String id) {
        String type = recipeConfig.getString("Recipes." + id + ".result.type");
        String material = recipeConfig.getString("Recipes." + id + ".result.item");
        int amount = recipeConfig.getInt("Recipes." + id + ".result.amount");
        if(type.equalsIgnoreCase("material")) {
            return new ItemStack(Material.valueOf(material), amount);
        } else {
            ItemStack item = HephaestusForge.config.savedItems.get(material);
            item.setAmount(amount);
            return item;
        }
    }

    private void loadConfigRecipes() {
        Set<String> configRecipes = recipeConfig.getConfigurationSection("Recipes").getKeys(false);
        for(String recipe : configRecipes) {
            String id = recipe;
            String shapeType = recipeConfig.getString("Recipes." + recipe + ".type");
            if(shapeType.equalsIgnoreCase("shaped")) {
                String[] shape = recipeConfig.getStringList("Recipes." + recipe + ".shape").toArray(new String[3]);
                HashMap<Character, ItemStack> ingredients = new HashMap<>();
                Set<String> configIngredients = recipeConfig.getConfigurationSection("Recipes." + recipe + ".ingredients").getKeys(false);
                for(String ingredient : configIngredients) {
                    String material = recipeConfig.getString("Recipes." + recipe + ".ingredients." + ingredient + ".item");
                    String type = recipeConfig.getString("Recipes." + recipe + ".ingredients." + ingredient + ".type");
                    if(type.equalsIgnoreCase("material")) {
                        ingredients.put(ingredient.charAt(0), new ItemStack(Material.valueOf(material)));
                    } else if(type.equalsIgnoreCase("item")) {
                        ingredients.put(ingredient.charAt(0), HephaestusForge.config.savedItems.get(material));
                    }
                }

                loadShapedRecipe(id, shape, ingredients);
            } else if(shapeType.equalsIgnoreCase("shapeless")) {
                HashMap<ItemStack, Integer> ingredients = new HashMap<>();
                Set<String> configIngredients = recipeConfig.getConfigurationSection("Recipes." + recipe + ".ingredients").getKeys(false);
                for(String ingredient : configIngredients) {
                    String material = recipeConfig.getString("Recipes." + recipe + ".ingredients." + ingredient + ".item");
                    String type = recipeConfig.getString("Recipes." + recipe + ".ingredients." + ingredient + ".type");
                    int amount = recipeConfig.getInt("Recipes." + recipe + ".ingredients." + ingredient + ".amount");
                    if(type.equalsIgnoreCase("material")) {
                        ingredients.put(new ItemStack(Material.valueOf(material)), amount);
                    } else if(type.equalsIgnoreCase("item")) {
                        ingredients.put(HephaestusForge.config.savedItems.get(material), amount);
                    }
                }

                loadShapelessRecipe(id, ingredients);
            }
        }
    }
}
