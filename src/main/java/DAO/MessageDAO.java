package DAO;

import Util.ConnectionUtil;
import Model.Message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class MessageDAO {
    
    public Message postMessage(Message message) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            String insertSql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
            PreparedStatement insertStatement = connection.prepareStatement(insertSql, PreparedStatement.RETURN_GENERATED_KEYS);
            insertStatement.setInt(1, message.getPosted_by());
            insertStatement.setString(2, message.getMessage_text());
            insertStatement.setLong(3, message.getTime_posted_epoch());
    
            int rowsAffected = insertStatement.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet rs = insertStatement.getGeneratedKeys();
                if (rs.next()) {
                    int messageId = rs.getInt(1);
                    return new Message(messageId, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
