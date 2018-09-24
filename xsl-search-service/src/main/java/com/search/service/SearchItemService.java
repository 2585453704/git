package com.search.service;

//import cn.e3mall.common.utils.E3Result;

import com.search.es.XslResult;

import java.net.UnknownHostException;

public interface SearchItemService{

	XslResult importAllItems() throws UnknownHostException;
}
