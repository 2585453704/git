package com.search.dao;

import com.search.es.EsServer;
import com.search.pojo.SearchItem;
import com.search.common.SearchResult;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.index.query.functionscore.ScriptScoreFunctionBuilder;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.elasticsearch.index.query.QueryBuilders.functionScoreQuery;

/**
 * 商品搜索dao
 * <p>Title: SearchDao</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p>
 * @version 1.0
 */
@Repository
public class SearchDao {

	/**
	 *根据查询条件查询索引库
	 * <p>Title: search</p>
	 * <p>Description: </p>
	 * @param query
	 * @return
	 */
    private EsServer esServer;

	public SearchResult search(String keyword, int page, int rows ,int search_type) throws Exception {
        SearchResult result = new SearchResult();
        esServer = new EsServer();
        TransportClient client = esServer.getClient();
        SearchRequestBuilder requestBuilder = client.prepareSearch("test")
                .setTypes("item").setQuery(functionScoreQuery(QueryBuilders.matchPhraseQuery("descr", keyword)));

        switch (search_type){
            case 0:
                Map<String, Object> params = new HashMap<>(4);
                Calendar nowTime = Calendar.getInstance();
                params.put("now_millis", nowTime.getTimeInMillis());
                String inlineScript = "long data_millis = doc['create_date'].date.millis;" +
                        "long now_millis  = params.now_millis;" +
                        "long difference = now_millis - data_millis + 28800000;" +
                        "double difference_of_hour = difference/3600000;" +
                        "long click=doc['click'].value;" +
                        "double score = 100*(_score+Math.log(click+1))/(difference_of_hour+5);" +
                        "return score;";
                Script script = new Script( ScriptType.INLINE, "painless",inlineScript, params);
                ScriptScoreFunctionBuilder scriptBuilder = ScoreFunctionBuilders.scriptFunction(script);

                requestBuilder = client.prepareSearch("test")
                        .setTypes("item")
                        .setQuery(functionScoreQuery(QueryBuilders.multiMatchQuery(keyword,"descr"),scriptBuilder));
                break;
            case 1:
                requestBuilder = client.prepareSearch("test")
                        .setTypes("item")
                        .setQuery(functionScoreQuery(QueryBuilders.multiMatchQuery(keyword,"descr")))
                        .addSort("money", SortOrder.DESC);
                break;

            case 2:
                requestBuilder = client.prepareSearch("test")
                        .setTypes("item")
                        .setQuery(functionScoreQuery(QueryBuilders.multiMatchQuery(keyword,"descr")))
                        .addSort("money", SortOrder.ASC);
                break;
            case 3:
                requestBuilder = client.prepareSearch("test")
                        .setTypes("item")
                        .setQuery(functionScoreQuery(QueryBuilders.multiMatchQuery(keyword,"descr")))
                        .addSort("create_date", SortOrder.DESC);
                break;

        }


        SearchResponse searchResponse = requestBuilder.setFrom(page*rows).setSize(rows).execute().actionGet();
        SearchHits hits = searchResponse.getHits();
        List<SearchItem> itemList = new ArrayList<>(page*rows);
        for(SearchHit hit:hits){
            Map<String,Object> hit_source = hit.getSourceAsMap();

            SearchItem item = new SearchItem();
			item.setId(Integer.parseInt(hit.getId()));
			item.setCid((Integer) hit_source.get("cid"));
			item.setDescr((String) hit_source.get("descr"));
            item.setState((Integer) hit_source.get("state"));
            String date = hit_source.get("create_date").toString();
            date = date.replace("Z", " UTC");//注意是空格+UTC
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");//注意格式化的表达式
            Date d = format.parse(date);

            item.setCreatedate(d);
            item.setUpdatedate(d);
            itemList.add(item);
        }
        result.setItemList(itemList);
        result.setRecordCount(hits.getTotalHits());
        result.setTotalPages(1+(int)hits.getTotalHits()/rows);
        return result;
	}

}
