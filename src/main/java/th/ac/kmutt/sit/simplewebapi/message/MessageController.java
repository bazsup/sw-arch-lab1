package th.ac.kmutt.sit.simplewebapi.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MessageController {
    @Autowired
    private MessageService messageService;

    @PostMapping("/messages")
    public ResponseEntity<Message> postMessage(@RequestBody Message message) {
        Message responseMsg = messageService.postMessage(message);
        return new ResponseEntity<>(responseMsg, HttpStatus.CREATED);
    }

    @GetMapping("/message")
    public ResponseEntity<Map> getPostedMessages() {
        List<MessageCount> messagesCount = messageService.getMessagesCount();
        Map<String, Object> responseMessageCount = new HashMap<>();
        responseMessageCount.put("data", messagesCount);
        return new ResponseEntity(responseMessageCount, HttpStatus.OK);
    }
}
