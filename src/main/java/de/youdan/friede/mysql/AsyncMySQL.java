package de.youdan.friede.mysql;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class AsyncMySQL {

    private ExecutorService executor;
    private Plugin plugin;
    private MySQL sql;

    public AsyncMySQL(Plugin owner, String host, int port, String user, String password, String database) {
        try {
            sql = new MySQL(host, port, user, password, database);
            executor = Executors.newCachedThreadPool();
            plugin = owner;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(PreparedStatement statement) {
        executor.execute(() -> sql.queryUpdate(statement));
    }

    public void update(String statement) {
        executor.execute(() -> sql.queryUpdate(statement));
    }

    public void query(PreparedStatement statement, Consumer<ResultSet> consumer) {
        executor.execute(() -> {
            ResultSet result = sql.query(statement);
            Bukkit.getScheduler().runTask(plugin, () -> consumer.accept(result));
        });
    }

    public void query(String statement, Consumer<ResultSet> consumer) {
        executor.execute(() -> {
            ResultSet result = sql.query(statement);
            Bukkit.getScheduler().runTask(plugin, () -> consumer.accept(result));
        });
    }

    public PreparedStatement prepare(String query) {
        try {
            return sql.getConnection().prepareStatement(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public MySQL getMySQL() {
        return sql;
    }
}

