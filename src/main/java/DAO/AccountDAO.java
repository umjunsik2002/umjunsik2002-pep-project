package DAO;

import Util.ConnectionUtil;
import Model.Account;
import Model.Message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

import java.util.List;
import java.util.ArrayList;

public class AccountDAO {
    
    public List<Message> getMessageForAccount(int accountId) {
        List<Message> messageList = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection()) {
            String selectSql = "SELECT * FROM message WHERE posted_by = ?";
            PreparedStatement selectStatement = connection.prepareStatement(selectSql);
            selectStatement.setInt(1, accountId);
            ResultSet rs = selectStatement.executeQuery();

            while (rs.next()) {
                int messageId = rs.getInt("message_id");
                int postedBy = rs.getInt("posted_by");
                String messageText = rs.getString("message_text");
                long timePostedEpoch = rs.getLong("time_posted_epoch");
                messageList.add(new Message(messageId, postedBy, messageText, timePostedEpoch));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return messageList;
    }

    public Account login(Account account) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            String selectSql = "SELECT * FROM account WHERE username = ? AND password = ?";
            PreparedStatement selectStatement = connection.prepareStatement(selectSql);
            selectStatement.setString(1, account.getUsername());
            selectStatement.setString(2, account.getPassword());
            ResultSet rs = selectStatement.executeQuery();

            if (rs.next()) {
                int accountId = rs.getInt("account_id");
                String username = rs.getString("username");
                String password = rs.getString("password");
                return new Account(accountId, username, password);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Account register(Account account) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            String selectSql = "SELECT * FROM account WHERE username = ?";
            PreparedStatement selectStatement = connection.prepareStatement(selectSql);
            selectStatement.setString(1, account.getUsername());
            ResultSet rsSelect = selectStatement.executeQuery();
            if (rsSelect.next()) {
                return null;
            }

            String insertSql = "INSERT INTO account (username, password) VALUES (?, ?)";
            PreparedStatement insertStatement = connection.prepareStatement(insertSql, PreparedStatement.RETURN_GENERATED_KEYS);
            insertStatement.setString(1, account.getUsername());
            insertStatement.setString(2, account.getPassword());

            int rowsAffected = insertStatement.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet rsInsert = insertStatement.getGeneratedKeys();
                if (rsInsert.next()) {
                    int accountId = rsInsert.getInt(1);
                    return new Account(accountId, account.getUsername(), account.getPassword());
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
