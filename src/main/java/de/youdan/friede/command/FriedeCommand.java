package de.youdan.friede.command;

import de.youdan.friede.Friede;
import de.youdan.friede.manager.FriedeManager;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class FriedeCommand implements CommandExecutor {

    private final FriedeManager friedeManager = new FriedeManager();
    private final HashMap<Player, Player> request = new HashMap<>();

    private void sendHelp(Player player) {
        player.sendMessage(Friede.FRIEDE_PREFIX + "Friede Commands");
        player.sendMessage("§7/friede add <Spielername>");
        player.sendMessage("§7/friede remove <Spielername>");
        player.sendMessage("§7/friede accept <Spielername>");
        player.sendMessage("§7/friede list");
        player.sendMessage("§7/friede info");
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!(commandSender instanceof Player)) {
            System.out.println("Dieser Command kann nur von Spieler benutzt werden!");
            return true;
        }

        final Player player = (Player) commandSender;
        final int arguments = args.length;

        switch (arguments) {
            default:
                sendHelp(player);
                break;
            case 1:
                if (args[0].equalsIgnoreCase("list")) {
                    if (friedeManager.getFriendList(player.getUniqueId().toString()).size() == 0) {
                        player.sendMessage(Friede.FRIEDE_PREFIX + "§cDu hast mit keinem Frieden geschlossen!");
                        return true;
                    }

                    player.sendMessage(Friede.FRIEDE_PREFIX + "Friedensliste");

                    for (String targetUUID : friedeManager.getFriendList(player.getUniqueId().toString())) {
                        player.sendMessage("§7- §e" + Bukkit.getPlayer(UUID.fromString(targetUUID)).getName());
                    }

                    int friendsAmount = friedeManager.getFriendList(player.getUniqueId().toString()).size();
                    player.sendMessage(Friede.FRIEDE_PREFIX + "Du hast insgesamt mit §e§l" + friendsAmount + "§7 Leuten Frieden geschlossen.");
                } else if(args[0].equalsIgnoreCase("info")) {
                    player.sendMessage(Friede.FRIEDE_PREFIX + "Das FriedeSystem wurde von §eJavaExceptions & Front7HD §7Programmiert.");
                } else {
                    sendHelp(player);
                }
                break;
            case 2:
                final Player target = Bukkit.getPlayer(args[1]);

                if (target == null) {
                    player.sendMessage(Friede.FRIEDE_PREFIX + "§cDer Spieler " + args[1] + " ist nicht online!");
                    return true;
                }

                if (target == player) {
                    player.sendMessage(Friede.FRIEDE_PREFIX + "§cDu kannst nicht mit dir selber Frieden schließen!");
                    return true;
                }

                if (args[0].equalsIgnoreCase("add")) {
                    if (request.containsKey(target)) {
                        if (request.get(target).getName().equals(player.getName())) {
                            player.sendMessage(Friede.FRIEDE_PREFIX + "§cDu hast diesem Spieler bereits eine Anfrage gesendet");
                        }
                        return true;
                    }

                    if (friedeManager.hasFriede(player.getUniqueId().toString(), target.getUniqueId().toString())) {
                        player.sendMessage(Friede.FRIEDE_PREFIX + "§cDu hast mit diesem Spieler bereits Frieden geschlossen!");
                        return true;
                    }

                    TextComponent acceptComponent = new TextComponent();
                    acceptComponent.setText(Friede.FRIEDE_PREFIX + "Klicke hier um die Anfrage anzunehmen §a[ANNEHMEN]");
                    acceptComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/friede accept " + player.getName()));

                    player.sendMessage(Friede.FRIEDE_PREFIX + "Du hast dem Spieler §e" + target.getName() + " §7eine Friedens anfrage gesendet");
                    target.sendMessage(Friede.FRIEDE_PREFIX + "Der Spieler §e" + player.getName() + " §7hat dir eine Friedens anfrage gesendet");
                    target.spigot().sendMessage(acceptComponent);

                    request.put(target, player);
                } else if (args[0].equalsIgnoreCase("remove")) {
                    if (!friedeManager.hasFriede(player.getUniqueId().toString(), target.getUniqueId().toString())) {
                        player.sendMessage(Friede.FRIEDE_PREFIX + "§cDu hast mit diesem Spieler kein Frieden geschlossen!");
                        return true;
                    }

                    player.sendMessage(Friede.FRIEDE_PREFIX + "Du hast denn Frieden mit §e" + target.getName() + " §7aufgelöst");
                    target.sendMessage(Friede.FRIEDE_PREFIX + "Der Spieler §e" + player.getName() + " §7hat denn Frieden aufgelöst");

                    friedeManager.removeFriede(player.getUniqueId().toString());
                    friedeManager.removeFriede(target.getUniqueId().toString());
                } else if (args[0].equalsIgnoreCase("accept")) {
                    if (request.containsKey(player)) {
                        final Player targetPlayer = Bukkit.getPlayer(request.get(player).getName());

                        if (targetPlayer == null) {
                            player.sendMessage(Friede.FRIEDE_PREFIX + "§cDer Spieler " + args[0] + " ist nicht online!");
                            return true;
                        }

                        player.sendMessage(Friede.FRIEDE_PREFIX + "Du hast mit §e" + targetPlayer.getName() + " §7erfolgreich Frieden geschlossen");
                        targetPlayer.sendMessage(Friede.FRIEDE_PREFIX + "Der Spieler §e" + player.getName() + " §7hat deine Friedens anfrage angenommen");

                        friedeManager.addFriede(player.getUniqueId().toString(), targetPlayer.getUniqueId().toString());
                        friedeManager.addFriede(targetPlayer.getUniqueId().toString(), player.getUniqueId().toString());

                        request.remove(player);
                    } else {
                        player.sendMessage(Friede.FRIEDE_PREFIX + "§cDu hast keine Friedens Anfrage von diesem Spieler.");
                    }
                } else {
                    sendHelp(player);
                }
                break;
        }
        return false;
    }
}
