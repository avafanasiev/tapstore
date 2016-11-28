package tapstore.model.dao;


import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import tapstore.model.Message;

import java.util.List;


@Repository
public class MessageDAO {

    private static final Logger logger = Logger.getLogger(MessageDAO.class);
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sf){
        this.sessionFactory = sf;
    }

    public void addMessage(Message message) {
        Session session = this.sessionFactory.getCurrentSession();
        session.persist(message);
        logger.info("Message saved successfully. Message details=" + message);
    }

    @SuppressWarnings("unchecked")
    public List<Message> listMessages() {
        Session session = this.sessionFactory.getCurrentSession();
        return session.createCriteria(Message.class).list();
    }
}
