package wbe.hephaestusForge.config;

import org.bukkit.inventory.ItemStack;

public class PiglinTrade {

    private String id;

    private int weight;

    private ItemStack item;

    public PiglinTrade(String id, int weight, ItemStack item) {
        this.id = id;
        this.weight = weight;
        this.item = item;
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

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }
}
