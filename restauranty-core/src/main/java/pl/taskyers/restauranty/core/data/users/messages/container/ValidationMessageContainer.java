package pl.taskyers.restauranty.core.data.users.messages.container;

import pl.taskyers.restauranty.core.data.users.messages.Message;
import pl.taskyers.restauranty.core.data.users.messages.MessageType;
import pl.taskyers.restauranty.core.data.users.messages.ValidationMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ValidationMessageContainer {
    
    private List<Message> messages = new ArrayList<>();
    
    public void addError(String message, String field) {
        messages.add(new ValidationMessage(message, MessageType.ERROR, field));
    }
    
    public boolean hasErrors() {
        return messages.stream().anyMatch(message -> message.getType().equals(MessageType.ERROR));
    }
    
    public List<Message> getAll() {
        return messages;
    }
    
    public List<Message> getErrors() {
        return getAllMessages(MessageType.ERROR);
    }
    
    private List<Message> getAllMessages(MessageType messageType) {
        return messages.stream().filter(message -> message.getType().equals(messageType)).collect(Collectors.toList());
    }
    
}
