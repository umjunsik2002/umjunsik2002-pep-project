package DAO;

import Util.ConnectionUtil;
import Model.Account;
import Model.Message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

import java.util.List;

import org.h2.command.Prepared;

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
}
