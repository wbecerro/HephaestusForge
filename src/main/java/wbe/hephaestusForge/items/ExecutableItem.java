package wbe.hephaestusForge.items;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import wbe.hephaestusForge.HephaestusForge;

import java.util.ArrayList;

public class ExecutableItem extends ItemStack {

    private HephaestusForge plugin = HephaestusForge.getInstance();

    public ExecutableItem(Item item, String internalName) {
        super(item.getMaterial());

        ItemMeta meta;
        if(hasItemMeta()) {
            meta = getItemMeta();
        } else {
            meta = Bukkit.getItemFactory().getItemMeta(item.getMaterial());
        }

        meta.setDisplayName(item.getName());

        ArrayList<String> lore = new ArrayList<>();
        for(String line : item.getLore()) {
            lore.add(line.replace("&", "§"));
        }
        meta.setLore(lore);

        if(item.isGlow()) {
            meta.addEnchant(Enchantment.MENDING, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        NamespacedKey itemKey = new NamespacedKey(plugin, "ForgeItem");
        meta.getPersistentDataContainer().set(itemKey, PersistentDataType.STRING, internalName);

        setItemMeta(meta);
    }
}
