package wbe.hephaestusForge.config;

import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class LootTableItem {

    private String id;

    private double chance;

    private ItemStack item;

    private int min;

    private int max;

    public LootTableItem(String id, double chance, ItemStack item, int min, int max) {
        this.id = id;
        this.chance = chance;
        this.item = item;
        this.min = min;
        this.max = max;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getChance() {
        return chance;
    }

    public void setChance(double chance) {
        this.chance = chance;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public ItemStack generateItem() {
        Random random = new Random();
        if(min == 1 && max == 1) {
            item.setAmount(1);
            return item;
        } else {
            item.setAmount(random.nextInt(min, max + 1));
            return item;
        }
    }
}
