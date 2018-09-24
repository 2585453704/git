package com.search.message;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import com.search.es.EsServer;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.search.pojo.SearchItem;
import com.search.mapper.ItemMapper;

/**
 * 监听商品添加消息，接收消息后，将对应的商品信息同步到索引库
 * <p>Title: ItemAddMessageListener</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p>
 * @version 1.0
 */
public class ItemAddMessageListener implements MessageListener {

	@Autowired
	private ItemMapper itemMapper;

	private EsServer esServer;


	@Override
	public void onMessage(Message message){

		try {
			// 创建client
			esServer = new EsServer();
			TransportClient client = esServer.getClient();

			BulkRequestBuilder bulkBuilder = client.prepareBulk();

			//从消息中取商品id
			TextMessage textMessage = (TextMessage) message;
			String text = textMessage.getText();
			Long itemId = new Long(text);
			//等待事务提交
			Thread.sleep(1000);
			//根据商品id查询商品信息
			SearchItem searchItem = itemMapper.getItemById(itemId);
			//向文档对象中添加域
			bulkBuilder.add(client.prepareIndex("test3", "item",searchItem.getId())
					.setSource(
							XContentFactory.jsonBuilder()
									.startObject()
									.field("item_title", searchItem.getTitle())
									.field("item_sell_point", searchItem.getSell_point()).field("item_price", searchItem.getPrice())
									.field("item_image", searchItem.getImage())
									.field("item_category_name", searchItem.getCategory_name())
									.endObject()
					)
			);
			//提交
			BulkResponse response = bulkBuilder.get();
			client.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
