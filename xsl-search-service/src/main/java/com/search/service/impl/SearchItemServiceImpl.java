package com.search.service.impl;

import com.search.es.EsServer;
import com.search.es.XslResult;
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
		System.out.print("tt\n");
		try {
			//查询商品列表
			List<SearchItem> itemList = itemMapper.getItemList();
			Calendar nowTime = Calendar.getInstance();

			//遍历商品列表
			for (SearchItem searchItem : itemList) {
				nowTime.add(Calendar.HOUR_OF_DAY, -1);
				int year = nowTime.get(Calendar.YEAR);
				int month = nowTime.get(Calendar.MONTH) + 1;
				int day =nowTime.get(Calendar.DAY_OF_MONTH);
				int hour = nowTime.get(Calendar.HOUR_OF_DAY);
				int min = nowTime.get(Calendar.MINUTE);
				String date = year+"-"+month+"-"+day+" "+hour+":"+min;
				System.out.print(searchItem.getId()+":"+hour+"\n");
				bulkBuilder.add(client.prepareIndex("test", "item",searchItem.getId())
								.setSource(
										 XContentFactory.jsonBuilder()
										.startObject()
												 .field("item_title", searchItem.getTitle())
												 .field("item_sell_point", searchItem.getSell_point()).field("item_price", searchItem.getPrice())
												 .field("item_image", searchItem.getImage())
												 .field("item_category_name", searchItem.getCategory_name())
												 .field("click",0)
												 .field("date",date)
										.endObject()
								)
				);
			}
			//提交
			BulkResponse response = bulkBuilder.get();
			client.close();
			System.out.println("haha");

					//E3Result.ok();
		} catch (Exception e) {
			e.printStackTrace();
			client.close();
			return XslResult.build(500, "数据导入发生异常");
		}
//test:
		return XslResult.ok();
	}

}
