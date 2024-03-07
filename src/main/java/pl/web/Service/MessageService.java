package pl.web.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.web.Entity.MessageEntity;
import pl.web.Repository.MessageRepository;

import java.util.List;

@Service
public class MessageService extends AccessChecking {
    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    private final MessageRepository messageRepository;

    public ResponseEntity<?> send(MessageEntity messageModel) {
        if (messageModel.isText()) {
            System.out.println(generateNewMessage(messageModel));
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    public ResponseEntity<?> messageList(String from, String to) {
        List<MessageEntity> messageOP = messageRepository.findByFrom(from);
        if (messageOP.isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        List<MessageEntity> filteredMessages = messageOP.stream()
                .filter(message -> message.getFrom().equals(from) && message.getTo().equals(to))
                .toList();
        if (filteredMessages.isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        return ResponseEntity.ok(filteredMessages);

    }

    private MessageEntity generateNewMessage(MessageEntity messageModel) {
        return new MessageEntity(
                messageModel.getFrom(),
                messageModel.getTo(),
                messageModel.getBody(),
                messageModel.isText()
        );
    }
}
