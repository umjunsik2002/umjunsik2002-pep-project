package DAO;

import Util.ConnectionUtil;
import Model.Message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

import java.util.List;
import java.util.ArrayList;

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

    public Message deleteMessage(int messageId) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            String selectSql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement selectStatement = connection.prepareStatement(selectSql);
            selectStatement.setInt(1, messageId);
            ResultSet rs = selectStatement.executeQuery();

            if (rs.next()) {
                int postedBy = rs.getInt("posted_by");
                String messageText = rs.getString("message_text");
                long timePostedEpoch = rs.getLong("time_posted_epoch");

                String deleteSql = "DELETE FROM message WHERE message_id = ?";
                PreparedStatement deleteStatement = connection.prepareStatement(deleteSql);
                deleteStatement.setInt(1, messageId);
                deleteStatement.executeUpdate();
                return new Message(messageId, postedBy, messageText, timePostedEpoch);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Message> getAllMessage() {
        List<Message> messageList = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection()) {
            String selectSql = "SELECT * FROM message";
            PreparedStatement selectStatement = connection.prepareStatement(selectSql);
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

    public Message getMessageById(int messageId) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            String selectSql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement selectStatement = connection.prepareStatement(selectSql);
            selectStatement.setInt(1, messageId);
            ResultSet rs = selectStatement.executeQuery();

            if (rs.next()) {
                int postedBy = rs.getInt("posted_by");
                String messageText = rs.getString("message_text");
                long timePostedEpoch = rs.getLong("time_posted_epoch");
                return new Message(messageId, postedBy, messageText, timePostedEpoch);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Message patchMessage(int messageId, String messageText) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            String selectSql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement selectStatement = connection.prepareStatement(selectSql);
            selectStatement.setInt(1, messageId);
            ResultSet rs = selectStatement.executeQuery();

            if (rs.next()) {
                int postedBy = rs.getInt("posted_by");
                long timePostedEpoch = rs.getLong("time_posted_epoch");
                
                String updateSql = "UPDATE message SET message_text = ? WHERE message_id = ?";
                PreparedStatement updateStatement = connection.prepareStatement(updateSql);
                updateStatement.setString(1, messageText);
                updateStatement.setInt(2, messageId);
                int rowsUpdated = updateStatement.executeUpdate();
                if (rowsUpdated > 0) {
                    return new Message(messageId, postedBy, messageText, timePostedEpoch);
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
