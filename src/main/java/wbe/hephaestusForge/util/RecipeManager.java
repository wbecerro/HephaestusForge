package wbe.hephaestusForge.util;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.*;
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

    public void loadFurnaceRecipe(String id, ItemStack input, float exp, int time) {
        NamespacedKey recipeKey = new NamespacedKey(plugin, id + "recipe");
        FurnaceRecipe recipe = new FurnaceRecipe(recipeKey, getResult(id), new RecipeChoice.ExactChoice(input), exp, time);
        plugin.getServer().addRecipe(recipe);
        keys.add(recipeKey);
    }

    public void loadBlastingRecipe(String id, ItemStack input, float exp, int time) {
        NamespacedKey recipeKey = new NamespacedKey(plugin, id + "recipe");
        BlastingRecipe recipe = new BlastingRecipe(recipeKey, getResult(id), new RecipeChoice.ExactChoice(input), exp, time);
        plugin.getServer().addRecipe(recipe);
        keys.add(recipeKey);
    }

    public void loadSmokingRecipe(String id, ItemStack input, float exp, int time) {
        NamespacedKey recipeKey = new NamespacedKey(plugin, id + "recipe");
        SmokingRecipe recipe = new SmokingRecipe(recipeKey, getResult(id), new RecipeChoice.ExactChoice(input), exp, time);
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
            String recipeType = recipeConfig.getString("Recipes." + recipe + ".type");
            if(recipeType.equalsIgnoreCase("shaped")) {
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
            } else if(recipeType.equalsIgnoreCase("shapeless")) {
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
            } else if(recipeType.equalsIgnoreCase("furnace")) {
                String material = recipeConfig.getString("Recipes." + recipe + ".input.item");
                String type = recipeConfig.getString("Recipes." + recipe + ".input.type");
                float exp = (float) recipeConfig.getDouble("Recipes." + recipe + ".exp");
                int time = recipeConfig.getInt("Recipes." + recipe + ".time") * 20;
                ItemStack input = null;
                if(type.equalsIgnoreCase("material")) {
                    input = new ItemStack(Material.valueOf(material));
                } else if(type.equalsIgnoreCase("item")) {
                    input = HephaestusForge.config.savedItems.get(material);
                }

                loadFurnaceRecipe(id, input, exp, time);
            } else if(recipeType.equalsIgnoreCase("blastFurnace")) {
                String material = recipeConfig.getString("Recipes." + recipe + ".input.item");
                String type = recipeConfig.getString("Recipes." + recipe + ".input.type");
                float exp = (float) recipeConfig.getDouble("Recipes." + recipe + ".exp");
                int time = recipeConfig.getInt("Recipes." + recipe + ".time") * 20;
                ItemStack input = null;
                if(type.equalsIgnoreCase("material")) {
                    input = new ItemStack(Material.valueOf(material));
                } else if(type.equalsIgnoreCase("item")) {
                    input = HephaestusForge.config.savedItems.get(material);
                }

                loadBlastingRecipe(id, input, exp, time);
            } else if(recipeType.equalsIgnoreCase("smoker")) {
                String material = recipeConfig.getString("Recipes." + recipe + ".input.item");
                String type = recipeConfig.getString("Recipes." + recipe + ".input.type");
                float exp = (float) recipeConfig.getDouble("Recipes." + recipe + ".exp");
                int time = recipeConfig.getInt("Recipes." + recipe + ".time") * 20;
                ItemStack input = null;
                if(type.equalsIgnoreCase("material")) {
                    input = new ItemStack(Material.valueOf(material));
                } else if(type.equalsIgnoreCase("item")) {
                    input = HephaestusForge.config.savedItems.get(material);
                }

                loadSmokingRecipe(id, input, exp, time);
            }
        }
    }
}
