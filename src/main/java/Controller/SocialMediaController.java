package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import Model.Account;
import Model.Message;
import Service.SocialMediaService;


/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    SocialMediaService socialMediaService;

    public SocialMediaController() {
        socialMediaService = new SocialMediaService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {

        Javalin app = Javalin.create();
        app.post("/register", this::postUserRegisterHandler);
        app.post("/login", this::postUserLoginHandler);
        app.post("/messages", this::postCreateNewMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);
        app.patch("/messages/{message_id}", this::updateMessageByIdHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesFromUserByUserIdHandler);
        return app;
    }
    
    /* Registers a new account */
    private void postUserRegisterHandler(Context context) throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account addedAccount = this.socialMediaService.createAccount(account);
        if (addedAccount == null) {
            context.status(400);
        } else {
            context.json(mapper.writeValueAsString(addedAccount));
        }
    }

    private void postUserLoginHandler(Context context) throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account loggedInAccount = this.socialMediaService.login(account);
        if (loggedInAccount == null) {
            context.status(401);
        } else {
            context.json(mapper.writeValueAsString(loggedInAccount));
        }
    }

    private void postCreateNewMessageHandler(Context context) throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        Message createdMessage = this.socialMediaService.createNewMessage(message);
        if (createdMessage == null) {
            context.status(400);
        } else {
            context.json(mapper.writeValueAsString(createdMessage));
        }
    }

    private void getAllMessagesHandler(Context context) {
        context.json(this.socialMediaService.getAllMessages());
    }

    private void getMessageByIdHandler(Context context) throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message message = this.socialMediaService.getMessageById(message_id);
        if (message == null) {
            return;
        }
        context.json(mapper.writeValueAsString(message));
    }

    private void deleteMessageByIdHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message deletedMessage = this.socialMediaService.deleteMessageById(message_id);
        if (deletedMessage == null) {
            return;
        }
        context.json(mapper.writeValueAsString(deletedMessage));
    }

    private void updateMessageByIdHandler(Context context) throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        String text = message.getMessage_text();
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message updatedMessage = this.socialMediaService.updateMessageById(message_id, text);
        if (updatedMessage == null) {
            context.status(400);
            return;
        }
        context.json(mapper.writeValueAsString(updatedMessage));
    }

    private void getAllMessagesFromUserByUserIdHandler(Context context) throws JsonMappingException, JsonProcessingException {
        int account_id = Integer.parseInt(context.pathParam("account_id"));
        context.json(this.socialMediaService.getAllMessagesFromUserByAccountId(account_id));
    }
}