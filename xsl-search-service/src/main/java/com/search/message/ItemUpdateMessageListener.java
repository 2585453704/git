package com.search.message;

import com.search.mapper.ItemMapper;
import com.search.es.EsServer;
import com.search.pojo.SearchItem;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class ItemUpdateMessageListener implements MessageListener {
    @Autowired
    private ItemMapper itemMapper;


    private EsServer esServer;


    @Override
    public void onMessage(Message message){
        try {
            esServer = new EsServer();
            TransportClient client = esServer.getClient();
            UpdateRequest request = new UpdateRequest();
            //从消息中取商品id
            TextMessage textMessage = (TextMessage) message;
            String text = textMessage.getText();
            Long itemId = new Long(text);
            //等待事务提交
            Thread.sleep(1000);
            //根据商品id查询商品信息
            SearchItem searchItem = itemMapper.getItemById(itemId);
            //向文档对象中添加域
            request.index("index3")
                    .type("item")
                    .id(searchItem.getId())
                    .doc(
                            XContentFactory.jsonBuilder()
                                    .startObject()
                                    .field("item_title", searchItem.getTitle())
                                    .field("item_sell_point", searchItem.getSell_point()).field("item_price", searchItem.getPrice())
                                    .field("item_image", searchItem.getImage())
                                    .field("item_category_name", searchItem.getCategory_name())
                                    .endObject()
                    );
            //提交
            UpdateResponse response = client.update(request).get();
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
