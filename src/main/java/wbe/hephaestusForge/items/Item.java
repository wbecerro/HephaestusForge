package wbe.hephaestusForge.items;

import org.bukkit.Material;

import java.util.List;

public class Item {

    private Material material;

    private String name;

    private List<String> lore;

    private List<String> commands;

    private boolean glow;

    private boolean infinite;

    public Item(Material material, String name, List<String> lore, List<String> commands, boolean glow, boolean infinite) {
        this.material = material;
        this.name = name;
        this.lore = lore;
        this.commands = commands;
        this.glow = glow;
        this.infinite = infinite;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getLore() {
        return lore;
    }

    public void setLore(List<String> lore) {
        this.lore = lore;
    }

    public List<String> getCommands() {
        return commands;
    }

    public void setCommands(List<String> commands) {
        this.commands = commands;
    }

    public boolean isGlow() {
        return glow;
    }

    public void setGlow(boolean glow) {
        this.glow = glow;
    }

    public boolean isInfinite() {
        return infinite;
    }

    public void setInfinite(boolean infinite) {
        this.infinite = infinite;
    }
}
