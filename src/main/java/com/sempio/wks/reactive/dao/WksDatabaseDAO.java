package com.sempio.wks.reactive.dao;

import java.util.HashMap;
import java.util.List;

import com.sempio.wks.reactive.config.datasource.annotaion.WKSMapper;

@WKSMapper
public interface WksDatabaseDAO {

	public List<HashMap<String, String>> selectLogList(String psnId);
}
