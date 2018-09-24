package com.search.message;

import com.search.es.EsServer;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.HashMap;
import java.util.Map;

public class ItemClickListener implements MessageListener {

    private EsServer esServer;

    @Override
    public void onMessage(Message message) {
        System.out.print("in");
        try {
            esServer = new EsServer();
            TransportClient client = esServer.getClient();
            //从消息中取商品id
            TextMessage textMessage = (TextMessage) message;
            String itemId = textMessage.getText();
            System.out.print(textMessage.getText());
            //等待事务提交
            Thread.sleep(1000);
            //设置script令click自增1
            String inlineScript = "ctx._source.click=ctx._source.click+1;";

            Map<String, Object> params = new HashMap<>();
            Script script = new Script(ScriptType.INLINE, "painless",inlineScript, params);

            UpdateRequest request = new UpdateRequest();
            request.index("test")
                    .type("item")
                    .id(itemId)
                    .script(script);
            client.update(request).get();
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
