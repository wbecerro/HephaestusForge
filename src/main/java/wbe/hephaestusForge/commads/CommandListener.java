package wbe.hephaestusForge.commads;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import wbe.hephaestusForge.HephaestusForge;
import wbe.hephaestusForge.items.Item;
import wbe.hephaestusForge.listeners.MenuListeners;
import wbe.hephaestusForge.util.Utilities;

public class CommandListener implements CommandExecutor {

    private HephaestusForge plugin = HephaestusForge.getInstance();

    private Utilities utilities = new Utilities();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("HephaestusForge")) {
            Player player = null;
            if(sender instanceof Player) {
                player = (Player) sender;
            }

            if(args.length == 0 || args[0].equalsIgnoreCase("help")) {
                if(!sender.hasPermission("hephaestusforge.command.help")) {
                    sender.sendMessage(HephaestusForge.messages.noPermission);
                    return false;
                }

                for(String line : HephaestusForge.messages.help) {
                    sender.sendMessage(line.replace("&", "§"));
                }
            } else if(args[0].equalsIgnoreCase("give")) {
                if(!sender.hasPermission("hephaestusforge.command.give")) {
                    sender.sendMessage(HephaestusForge.messages.noPermission);
                    return false;
                }

                if(args.length < 2) {
                    sender.sendMessage(HephaestusForge.messages.notEnoughArgs);
                    sender.sendMessage(HephaestusForge.messages.itemGiveArgs);
                    return false;
                }

                if(args.length > 2) {
                    player = Bukkit.getPlayer(args[2]);
                }

                boolean ok = utilities.giveItem(player, args[1]);
                if(ok) {
                    Item item = utilities.getItemByName(args[1]);
                    sender.sendMessage(HephaestusForge.messages.itemGiven
                            .replace("%item%", item.getName())
                            .replace("%player%", player.getName()));
                } else {
                    sender.sendMessage(HephaestusForge.messages.itemNotFound
                            .replace("%item%", args[1]));
                }
            } else if(args[0].equalsIgnoreCase("reload")) {
                if(!sender.hasPermission("hephaestusforge.command.reload")) {
                    sender.sendMessage(HephaestusForge.messages.noPermission);
                    return false;
                }

                plugin.reloadConfiguration();
                sender.sendMessage(HephaestusForge.messages.reload);
            } else if(args[0].equalsIgnoreCase("addItem")) {
                if(!sender.hasPermission("hephaestusforge.command.addItem")) {
                    sender.sendMessage(HephaestusForge.messages.noPermission);
                    return false;
                }

                ItemStack item = player.getInventory().getItemInMainHand();
                String identifier = args[1];
                boolean ok = utilities.addItem(item, identifier);
                if(ok) {
                    sender.sendMessage(HephaestusForge.messages.itemAdded);
                } else {
                    sender.sendMessage(HephaestusForge.messages.itemAlreadyExists.replace("%identifier%", identifier));
                }
            } else if(args[0].equalsIgnoreCase("recipe")) {
                if(!sender.hasPermission("hephaestusforge.command.recipe")) {
                    sender.sendMessage(HephaestusForge.messages.noPermission);
                    return false;
                }

                String namespace = args[1];
                String recipe = args[2];
                NamespacedKey recipeKey = new NamespacedKey(namespace, recipe);
                Recipe finalRecipe = utilities.getRecipe(recipeKey);
                if(finalRecipe instanceof CraftingRecipe craftingRecipe) {
                    try {
                        MenuListeners.openMenu(player, craftingRecipe);
                    } catch(Exception ex) {
                        sender.sendMessage(ex.getMessage());
                        ex.printStackTrace();
                    }
                } else {
                    sender.sendMessage(HephaestusForge.messages.recipeNotSupported);
                }
            }
        }

        return true;
    }
}
