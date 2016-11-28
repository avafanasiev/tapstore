package controllers;

import akka.NotUsed;
import akka.stream.javadsl.Source;
import akka.util.ByteString;
import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;
import messages.impl.AddRequestOuterClass.AddRequest;
import messages.impl.AddResponseOuterClass.AddResponse;
import play.Logger;
import play.libs.ws.WSResponse;
import play.data.FormFactory;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.asynchttpclient.util.HttpConstants.Methods.POST;
import static play.libs.Json.toJson;

public class MessageController extends Controller {

    private final FormFactory formFactory;
    private final List<String> responses = new ArrayList<>();
    private final WSClient ws;

    @Inject
    public MessageController(FormFactory formFactory, WSClient ws) {
        this.formFactory = formFactory;
        this.ws = ws;
    }

    public Result index() {
        return ok(views.html.index.render());
    }

    public Result addMessage() throws IOException {
        String message = formFactory.form().bindFromRequest().get("message");
        Logger.info("Send add message : " + message);
        WSRequest request = buildAddRequest(message);
        request.execute(POST).thenAcceptAsync(this::processResponse);
        return redirect(routes.MessageController.index());
    }

    private WSRequest buildAddRequest(String message) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1000);
        CodedOutputStream codedStream = CodedOutputStream.newInstance(buffer);
        AddRequest addRequest = AddRequest.newBuilder().setMessage(message).build();
        addRequest.writeTo(codedStream);
        codedStream.flush();
        buffer.flip();
        Logger.info("Buffer to send " + buffer.limit() );

        Source<ByteString, NotUsed> source = Source.single(ByteString.fromByteBuffer(buffer));
        Logger.info("Buffer to send " + buffer.limit() + " source " + source.toString());
        return ws.url("http://localhost/tapstore/addProto").setBody(source);
    }

    private void processResponse(WSResponse response) {
        try {
            if (response.getStatus() == OK) {
                AddResponse addResponse = AddResponse.parseFrom(CodedInputStream.newInstance(response.getBodyAsStream()));
                responses.add("Added " + addResponse);
                Logger.info("Response received " + addResponse);
            } else {
                Logger.error("Wrong response status: " + response.getStatusText());
            }
        } catch (IOException e) {
            Logger.error("Error in response", e);
        }
    }

    public Result asyncResponses() {
        return ok(toJson(responses));
    }

}
