package tapstore.services;

import org.springframework.transaction.annotation.Transactional;
import tapstore.model.Message;

import java.util.List;

/**
 * Created by ASUS on 25.11.2016.
 */
public interface MessageService {
    @Transactional
    void addMessage(Message p);

    @Transactional
    List<Message> listMessages();
}
