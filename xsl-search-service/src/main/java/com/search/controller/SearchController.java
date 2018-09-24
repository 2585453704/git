package com.search.controller;

//import org.apache.commons.lang3.StringUtils;

import com.search.es.XslResult;
import com.search.pojo.SearchResult;
import com.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 商品查询Controller
 * <p>Title: SearchController</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.com</p>
 * @author	入云龙
 * @date	2015年9月11日下午4:22:42
 * @version 1.0
 */
@Controller
public class SearchController {

	@Autowired
	private SearchService searchService;

	@RequestMapping(value="/query", method=RequestMethod.GET)
	@ResponseBody
	public XslResult search(@RequestParam String query,
                            @RequestParam(defaultValue="0")Integer page,
                            @RequestParam(defaultValue="10")Integer rows,
							@RequestParam(defaultValue="0")Integer sort_type) {
		//查询条件不能为空
		if (query.isEmpty()) {
			return XslResult.build(400, "查询条件不能为空");
		}
		SearchResult searchResult = null;
		try {
			System.out.println(query);
			searchResult = searchService.search(query,page,rows,sort_type);
		} catch (Exception e) {
			e.printStackTrace();
			return XslResult.build(500, e.toString());
		}
		return XslResult.ok(searchResult);
	}

}
