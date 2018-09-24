package com.search.service;

import com.search.pojo.SearchResult;

public interface SearchService {

	SearchResult search(String keyword, int page, int rows ,int sort_type)  throws Exception;
}
