package de.youdan.friede;

import de.youdan.friede.command.FriedeCommand;
import de.youdan.friede.listener.FriedeDamageEvent;
import de.youdan.friede.mysql.AsyncMySQL;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Friede extends JavaPlugin {

    public static Friede friede;
    public static final String FRIEDE_PREFIX = "§8[§eFriede§8] §7";
    private AsyncMySQL asyncMySQL;

    @Override
    public void onEnable() {
        friede = this;

        loadConfig();
        registerEvents();
        connectMySQL();

        System.out.println("Das Friede System wurde erfolgreich Aktiviert.");
    }

    @Override
    public void onDisable() {
        System.out.println("Das Friede System wurde Deaktiviert.");
    }

    public void loadConfig() {
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    private void registerEvents() {
        final PluginManager pluginManager = Bukkit.getPluginManager();

        // Listener
        pluginManager.registerEvents(new FriedeDamageEvent(), friede);
        // Commands
        getCommand("friede").setExecutor(new FriedeCommand());
    }

    private void connectMySQL() {
        String host = getConfig().getString("MySQL.Host"),
               database = getConfig().getString("MySQL.Database"),
               username = getConfig().getString("MySQL.Username"),
               password = getConfig().getString("MySQL.Password");
        int port = getConfig().getInt("MySQL.Port");

        asyncMySQL = new AsyncMySQL(this, host, port, username, password, database);
        asyncMySQL.update("CREATE TABLE IF NOT EXISTS FriedeManager (PlayerUUID VARCHAR(64), TargetUUID VARCHAR(64))");
    }

    public AsyncMySQL getAsyncMySQL() {
        return asyncMySQL;
    }

    public static Friede getFriede() {
        return friede;
    }
}