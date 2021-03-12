package de.youdan.friede.manager;

import de.youdan.friede.Friede;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FriedeManager {

    public boolean hasFriede(String playerUUID, String targetUUID) {
        try {
            ResultSet resultSet = Friede.getFriede().getAsyncMySQL().prepare("SELECT * FROM FriedeManager WHERE PlayerUUID='" + playerUUID + "'").executeQuery();

            while(resultSet.next()) {
                if(resultSet.getString("TargetUUID").equals(targetUUID)) {
                    return true;
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return false;
    }

    public void addFriede(String playerUUID, String targetUUID) {
        Friede.getFriede().getAsyncMySQL().update("INSERT INTO FriedeManager (PlayerUUID, TargetUUID) VALUES ('" + playerUUID + "','" + targetUUID + "');");
    }

    public void removeFriede(String playerUUID) {
        Friede.getFriede().getAsyncMySQL().update("DELETE FROM FriedeManager WHERE PlayerUUID='" + playerUUID + "'");
    }

    public List<String> getFriendList(String playerUUID) {
        List<String> players = new ArrayList<String>();

        try {
            ResultSet resultSet = Friede.getFriede().getAsyncMySQL().prepare("SELECT * FROM FriedeManager WHERE PlayerUUID='"+playerUUID+"'").executeQuery();

            while(resultSet.next()) {
                players.add(resultSet.getString("TargetUUID"));
            }
        } catch(SQLException exception) {
            exception.printStackTrace();
        }
        return players;
    }
}