package com.search.service.impl;

import com.search.es.EsServer;
import com.search.pojo.SearchItem;
import com.search.service.DeleteAllService;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentFactory;

import java.net.UnknownHostException;
import java.util.List;

public class DeleteAllServiceImpl implements DeleteAllService {
    @Override
    public int DeleteAllItems() {
        // 创建client
//        esServer = new EsServer();
//        TransportClient client = esServer.getClient();
//
//
//        BulkRequestBuilder bulkBuilder = client.prepareBulk();
//        System.out.print("tt\n");
//        try {
//            //查询商品列表
//            List<SearchItem> itemList = itemMapper.getItemList();
//            //遍历商品列表
//            for (SearchItem searchItem : itemList) {
//
//                bulkBuilder.add(client.prepareIndex("test4", "item",searchItem.getId())
//                        .setSource(
//                                XContentFactory.jsonBuilder()
//                                        .startObject()
//                                        .field("item_title", searchItem.getTitle())
//                                        .field("item_sell_point", searchItem.getSell_point()).field("item_price", searchItem.getPrice())
//                                        .field("item_image", searchItem.getImage())
//                                        .field("item_category_name", searchItem.getCategory_name())
//                                        .endObject()
//                        )
//                );
//            }
//            //提交
//            BulkResponse response = bulkBuilder.get();
//            client.close();
//            System.out.println("haha");
//            //返回导入成功
//            return 200;
//
//            //E3Result.ok();
//        } catch (Exception e) {
//            e.printStackTrace();
//            client.close();
//            return 500;
//            //E3Result.build(500, "数据导入时发生异常");
//
//        }
        return 0;
    }
}
