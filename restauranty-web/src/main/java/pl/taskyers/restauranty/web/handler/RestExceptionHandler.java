package pl.taskyers.restauranty.web.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.taskyers.restauranty.core.error.exceptions.BadRequestException;
import pl.taskyers.restauranty.core.error.exceptions.NotFoundException;
import pl.taskyers.restauranty.core.messages.Message;
import pl.taskyers.restauranty.core.messages.ResponseMessage;
import pl.taskyers.restauranty.core.messages.enums.MessageType;

import java.util.List;

@ControllerAdvice
public class RestExceptionHandler {
    
    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<ResponseMessage<String>> handleNotFoundException(NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseMessage<>(NotFoundException.MESSAGE, MessageType.ERROR, e.getMessage()));
    }
    
    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<ResponseMessage<List<Message>>> handleBadRequestException(BadRequestException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseMessage<>(BadRequestException.MESSAGE, MessageType.ERROR, e.getMessages()));
    }
    
}
