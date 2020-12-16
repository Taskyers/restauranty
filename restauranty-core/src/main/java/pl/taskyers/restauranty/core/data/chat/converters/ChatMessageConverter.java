package pl.taskyers.restauranty.core.data.chat.converters;

import lombok.experimental.UtilityClass;
import pl.taskyers.restauranty.core.data.chat.dto.ChatMessageDTO;
import pl.taskyers.restauranty.core.data.chat.entity.ChatMessage;
import pl.taskyers.restauranty.core.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class ChatMessageConverter {
    
    public List<ChatMessageDTO> convertToDTOList(List<ChatMessage> messages){
        List<ChatMessageDTO> result= new ArrayList<>(messages.size());
        for(ChatMessage message : messages){
            result.add(new ChatMessageDTO(message.getAuthor().getUsername(),message.getRecipient().getUsername(),message.getContent(), DateUtils.parseStringDatetime(message.getTimestamp())));
        }
        return result;
    }
    
}
