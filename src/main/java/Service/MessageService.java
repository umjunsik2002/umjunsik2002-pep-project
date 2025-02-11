package Service;

import DAO.MessageDAO;
import Model.Message;

import java.util.List;

public class MessageService {
    public MessageDAO messageDAO;

    public MessageService() {
        messageDAO = new MessageDAO();
    }

    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }

    public Message postMessage(Message message) {
        return messageDAO.postMessage(message);
    }

    public Message deleteMessage(int messageId) {
        return messageDAO.deleteMessage(messageId);
    }

    public List<Message> getAllMessage() {
        return messageDAO.getAllMessage();
    }

    public Message getMessageById(int messageId) {
        return messageDAO.getMessageById(messageId);
    }

    public Message patchMessage(int messageId, String messageText) {
        return messageDAO.patchMessage(messageId, messageText);
    }
}
