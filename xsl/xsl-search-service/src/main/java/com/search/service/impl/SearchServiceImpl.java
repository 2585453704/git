package com.search.service.impl;

import com.search.dao.SearchDao;
import com.search.common.SearchResult;
import com.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 商品搜索Service
 * <p>Title: SearchServiceImpl</p>
 * <p>Description: </p>
 * @version 1.0
 */
@Service
public class SearchServiceImpl implements SearchService {

	@Autowired
	private SearchDao searchDao;

    @Override
    public SearchResult search(String keyword, int page, int rows ,int search_type) throws Exception{
        SearchResult searchResult = searchDao.search(keyword,page,rows,search_type);
        return searchResult;
    }


}
