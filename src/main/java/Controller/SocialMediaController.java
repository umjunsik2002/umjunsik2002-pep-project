package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import Model.Message;
import Service.AccountService;
import Service.MessageService;

import java.util.List;
import java.util.ArrayList;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    private AccountService accountService;
    private MessageService messageService;

    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();

        // POST /messages
        app.post("/messages", this::postMessageHandler);

        // DELETE /messages/{message_id}
        app.delete("/messages/{message_id}", this::deleteMessageHandler);

        // GET /messages
        app.get("/messages", this::getAllMessage);

        // GET /messages/{message_id}
        app.get("/messages/{message_id}", this::getMessageById);

        // PATCH /messages/{message_id}
        app.patch("/messages/{message_id}", this::patchMessage);

        // GET /accounts/{account_id}/messages
        app.get("/account/{account_id}/messages", this::getMessageForAccount);

        return app;
    }

    private void postMessageHandler(Context context) {
        try {
            String body = context.body();
            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode rootNode = objectMapper.readTree(body);
            JsonNode postedByNode = rootNode.get("posted_by");
            JsonNode messageTextNode = rootNode.get("message_text");
            JsonNode timePostedEpochNode = rootNode.get("time_posted_epoch");

            if (postedByNode == null || postedByNode.isNull() || postedByNode.asInt() <= 0 ||
                messageTextNode == null || messageTextNode.isNull() || messageTextNode.asText().isEmpty() || messageTextNode.asText().length() > 255 ||
                timePostedEpochNode == null || timePostedEpochNode.isNull() || timePostedEpochNode.asLong() <= 0) {
                context.status(400);
                return;
            }

            Message newMessage = objectMapper.readValue(body, Message.class);
            Message postedMessage = messageService.postMessage(newMessage);
            if (postedMessage != null) {
                context.status(200).json(postedMessage);
                return;
            }
            else {
                context.status(400);
                return;
            }
        }
        catch (Exception e) {
            context.status(500);
        }
    }

    private void deleteMessageHandler(Context context) {
        try {
            int messageId = Integer.parseInt(context.pathParam("message_id"));
            Message deletedMessage = messageService.deleteMessage(messageId);
            if (deletedMessage == null) {
                context.status(200);
            }
            else {
                context.status(200).json(deletedMessage);
            }
        }
        catch (Exception e) {
            context.status(500);
        }
    }

    private void getAllMessage(Context context) {
        try {
            context.status(200).json(messageService.getAllMessage());
        }
        catch (Exception e) {
            context.status(500);
        }
    }

    private void getMessageById(Context context) {
        try {
            int messageId = Integer.parseInt(context.pathParam("message_id"));
            Message selectedMessage = messageService.getMessageById(messageId);
            if (selectedMessage == null) {
                context.status(200);
            }
            else {
                context.status(200).json(selectedMessage);
            }
        }
        catch (Exception e) {
            context.status(500);
        }
    }

    private void patchMessage(Context context) {
        try {
            int messageId = Integer.parseInt(context.pathParam("message_id"));
            String body = context.body();
            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode rootNode = objectMapper.readTree(body);
            JsonNode messageTextNode = rootNode.get("message_text");
            if (messageTextNode == null || messageTextNode.isNull() || messageTextNode.asText().isEmpty() || messageTextNode.asText().length() > 255) {
                context.status(400);
                return;
            }

            Message updatedMessage = messageService.patchMessage(messageId, messageTextNode.asText());
            if (updatedMessage == null) {
                context.status(400);
            }
            else {
                context.status(200).json(updatedMessage);
            }
        }
        catch (Exception e) {
            context.status(500);
        }
    }

    private void getMessageForAccount(Context context) {
        try {

        }
        catch (Exception e) {
            context.status(500);
        }
    }
}
