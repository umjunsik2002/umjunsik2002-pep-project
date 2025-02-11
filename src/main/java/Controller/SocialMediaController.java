package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import Model.Message;
import Service.AccountService;
import Service.MessageService;

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
            Message returnedMessage = messageService.postMessage(newMessage);
            if (returnedMessage != null) {
                context.status(200).json(returnedMessage);
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
        context.status(200);
    }
}
