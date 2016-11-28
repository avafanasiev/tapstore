package tapstore.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tapstore.model.Message;
import tapstore.model.dao.MessageDAO;
import tapstore.services.MessageService;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    private MessageDAO messageDAO;

    @Override
    @Transactional
    public void addMessage(Message p) {
        messageDAO.addMessage(p);
    }
    @Override
    @Transactional
    public List<Message> listMessages() {
        return messageDAO.listMessages();
    }

    public void setMessageDAO(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }
}
