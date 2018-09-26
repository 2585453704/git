package com.search.mapper;

import java.util.List;

import com.search.pojo.SearchItem;

public interface ItemMapper {

	List<SearchItem> getItemList();
	SearchItem getItemById(long itemId);

}
