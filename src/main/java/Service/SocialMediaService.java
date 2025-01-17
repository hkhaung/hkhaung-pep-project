package Service;

import java.util.List;

import DAO.SocialMediaDAO;
import Model.Account;
import Model.Message;

public class SocialMediaService {
    SocialMediaDAO socialMediaDAO;

    public SocialMediaService(){
        this.socialMediaDAO = new SocialMediaDAO();
    }

    public Account createAccount(Account account) {
        if (account.getUsername() == "" || account.getPassword().length() < 4) {
            return null;
        }
        return this.socialMediaDAO.createAccount(account);
    }

    public Account login(Account account) {
        return this.socialMediaDAO.login(account);
    }

    public Message createNewMessage(Message message) {
        String text = message.getMessage_text();
        if (text.length() > 0 && text.length() < 255) {
            return this.socialMediaDAO.createNewMessage(message);
        }
        return null;
    }

    public List<Message> getAllMessages() {
        return this.socialMediaDAO.getAllMessages();
    }

    public Message getMessageById(int id) {
        return this.socialMediaDAO.getMessageById(id);
    }

    public Message deleteMessageById(int message_id) {
        Message messageToDelete = this.getMessageById(message_id);
        if (messageToDelete == null) {
            return null;
        }
        this.socialMediaDAO.deleteMessageById(message_id);
        return messageToDelete;
    }

    public Message updateMessageById(int message_id, String new_text) {
        Message message = this.getMessageById(message_id);
        if (message != null && new_text.length() > 0 && new_text.length() <= 255) {
            message.setMessage_text(new_text); 
            this.socialMediaDAO.updateMessageById(message_id, new_text);
            return message;
        }
        return null;
    }

    public List<Message> getAllMessagesFromUserByAccountId(int id) {
        return this.socialMediaDAO.getAllMessagesFromUserByAccountId(id);
    }
}
