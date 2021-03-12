package de.youdan.friede.listener;

import de.youdan.friede.Friede;
import de.youdan.friede.manager.FriedeManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class FriedeDamageEvent implements Listener {

    private final FriedeManager friedeManager = new FriedeManager();

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof Player)) {
            return;
        }

        final Player player = (Player) event.getEntity();
        final Player damager = (Player) event.getDamager();

        if (friedeManager.hasFriede(player.getUniqueId().toString(), damager.getUniqueId().toString())) {
            event.setCancelled(true);
            damager.sendMessage(Friede.FRIEDE_PREFIX + "§cDu kannst diesen Spieler nicht angreifen da ihr Frieden geschlossen habt!");
        }
    }
}