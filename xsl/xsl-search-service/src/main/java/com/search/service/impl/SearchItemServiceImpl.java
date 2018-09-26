package com.search.service.impl;

import com.search.es.EsServer;
import com.search.common.XslResult;
import com.search.mapper.ItemMapper;
import com.search.pojo.SearchItem;
import com.search.service.SearchItemService;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.List;

//import cn.e3mall.common.utils.E3Result;

/**
 * 索引库维护Service
 * <p>Title: SearchItemServiceImpl</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */
@Service
public class SearchItemServiceImpl implements SearchItemService {

	@Autowired
	private ItemMapper itemMapper;

	private EsServer esServer;
	
	@Override
	public XslResult importAllItems() throws UnknownHostException {
		// 创建client
		esServer = new EsServer();
		TransportClient client = esServer.getClient();
		BulkRequestBuilder bulkBuilder = client.prepareBulk();
		try {
			//查询商品列表
			List<SearchItem> itemList = itemMapper.getItemList();
			Calendar nowTime = Calendar.getInstance();

			//遍历商品列表
			for (SearchItem searchItem : itemList) {
				System.out.print(searchItem.getCreatedate());
				bulkBuilder.add(client.prepareIndex("test", "item",searchItem.getId()+"")
								.setSource(
										 XContentFactory.jsonBuilder()
										.startObject()
												 .field("cid", searchItem.getCid())
												 .field("descr", searchItem.getDescr())
												 .field("send_id", searchItem.getSendid())
												 .field("money",searchItem.getMoney())
												 .field("state", searchItem.getState())
												 .field("create_date", searchItem.getCreatedate())
												 .field("click", 0)
												 .field("update_date",searchItem.getUpdatedate())
												 .field("revoke_date",searchItem.getRevokedate())
										.endObject()
								)
				);
			}
			//提交
			BulkResponse response = bulkBuilder.get();
			client.close();
		} catch (Exception e) {
			e.printStackTrace();
			client.close();
			return XslResult.build(500, "数据导入发生异常");
		}
		return XslResult.ok();
	}

}
