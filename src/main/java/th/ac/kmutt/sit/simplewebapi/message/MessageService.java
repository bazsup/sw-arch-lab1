package th.ac.kmutt.sit.simplewebapi.message;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MessageService {

    private Map<String, Integer> messageCount = new HashMap<>();

    public Message postMessage(Message message) {
        Boolean hasMessage = messageCount.containsKey(message.getText());
        Integer count = hasMessage ? messageCount.get(message.getText()) + 1 : 1;
        messageCount.put(message.getText(), count);
        return message;
    }

    public List<MessageCount> getMessagesCount() {
        List<MessageCount> messageCounts = new ArrayList<>();
        for (Map.Entry countEntry : messageCount.entrySet()) {
            String text = countEntry.getKey().toString();
            int count = Integer.parseInt(countEntry.getValue().toString());
            messageCounts.add(new MessageCount(text, count));
        }
        return messageCounts;
    }
}
