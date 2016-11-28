package tapstore.controllers;

import messages.impl.AddRequestOuterClass.AddRequest;
import messages.impl.AddResponseOuterClass.AddResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import tapstore.model.Message;
import tapstore.services.MessageService;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

@Controller
public class MessageController {

    @Autowired(required=true)
    @Qualifier(value="messageService")
    private MessageService messageService;

    @RequestMapping(value = "/list", method = RequestMethod.GET,  produces = "application/json")
    @ResponseBody
    public List<Message> listMessages() {
        return messageService.listMessages();
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addMessage(@RequestParam(value = "message", required = true) String message) {
        messageService.addMessage(new Message(message, new Date()));
        return "redirect:/list";
    }

    @RequestMapping(value = "/addProto", method = RequestMethod.POST)
    @ResponseBody()
    public void addProto(HttpEntity<byte[]> requestEntity, OutputStream response) throws IOException {
        AddRequest request = AddRequest.parseFrom(requestEntity.getBody());
        Message m = new Message(request.getMessage(), new Date());
        messageService.addMessage(m);
        AddResponse build = AddResponse.newBuilder().
                setId(m.getId()).setMessage(m.getMessage()).setTimestamp(m.getDate().getTime()).build();

        build.writeTo(response);

    }

    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }
}
