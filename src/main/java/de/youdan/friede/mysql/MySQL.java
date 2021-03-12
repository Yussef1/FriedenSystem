package de.youdan.friede.mysql;

import java.sql.*;

public class MySQL {

    private String host, user, password, database;
    private int port;

    public Connection conn;

    public MySQL(String host, int port, String user, String password, String database) throws Exception {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
        this.database = database;

        this.openConnection();
    }

    public void queryUpdate(String query) {
        checkConnection();
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            queryUpdate(statement);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void queryUpdate(PreparedStatement statement) {
        checkConnection();
        try {
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                statement.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public ResultSet query(String query) {
        checkConnection();
        try {
            return query(conn.prepareStatement(query));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet query(PreparedStatement statement) {
        checkConnection();
        try {
            return statement.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Connection getConnection() {
        checkConnection();
        return this.conn;
    }

    private void checkConnection() {
        try {
            if (this.conn == null || !this.conn.isValid(10) || this.conn.isClosed()) openConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Connection openConnection() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        return this.conn = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database + "?autoReconnect=true", this.user, this.password);
    }

    public void closeConnection() {
        try {
            this.conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.conn = null;
        }
    }
}
