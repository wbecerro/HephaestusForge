package wbe.hephaestusForge.config;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

public class WanderingRecipe {

    private String id;

    private int weight;

    private int maxUses;

    private ItemStack result;

    private ItemStack ingredient1;

    private ItemStack ingredient2;

    public WanderingRecipe(String id, int maxUses, int weight, ItemStack result, ItemStack ingredient1, ItemStack ingredient2) {
        this.id = id;
        this.weight = weight;
        this.maxUses = maxUses;
        this.result = result;
        this.ingredient1 = ingredient1;
        this.ingredient2 = ingredient2;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getMaxUses() {
        return maxUses;
    }

    public void setMaxUses(int maxUses) {
        this.maxUses = maxUses;
    }

    public ItemStack getResult() {
        return result;
    }

    public void setResult(ItemStack result) {
        this.result = result;
    }

    public ItemStack getIngredient1() {
        return ingredient1;
    }

    public void setIngredient1(ItemStack ingredient1) {
        this.ingredient1 = ingredient1;
    }

    public ItemStack getIngredient2() {
        return ingredient2;
    }

    public void setIngredient2(ItemStack ingredient2) {
        this.ingredient2 = ingredient2;
    }

    public MerchantRecipe createRecipe() {
        MerchantRecipe recipe = new MerchantRecipe(result, maxUses);
        recipe.addIngredient(ingredient1);
        recipe.addIngredient(ingredient2);
        recipe.setExperienceReward(true);

        return recipe;
    }
}
