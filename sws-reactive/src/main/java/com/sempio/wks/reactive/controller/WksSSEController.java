package com.sempio.wks.reactive.controller;


import java.time.Duration;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.WebSession;

import com.sempio.wks.reactive.dao.WksDatabaseDAO;
import com.sempio.wks.reactive.util.Hash;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/sse")
public class WksSSEController {
	private static HashMap<String, String> userList = new HashMap<String, String>();
	
	@Autowired
	private WksDatabaseDAO wksDatabaseDAO;
	
	@RequestMapping("/getNotificationList/{psnId}/{key}")
	public Flux<List<HashMap<String, String>>> getNotificationList(WebSession session, @PathVariable String psnId, @PathVariable String key ) {
		if(getUserList().get(psnId) == null || !getUserList().get(psnId).equals(key)) {
			return Flux.never();
		}
		
		return Flux.interval(Duration.ofSeconds(10)).doFinally(e -> {
			if(getUserList().get(psnId) != null) {
				getUserList().remove(psnId);
			}
			System.out.println("ERROR DELETE USER.");
			}).map(l -> this.wksDatabaseDAO.selectLogList(psnId)); 
	}
	
	@RequestMapping("/getKey/{psnId}")
	public Mono<HashMap<String, String>> getKey(@PathVariable String psnId){
		String key = null;
		
		if(getUserList().get(psnId) == null) {
			key = Hash.getSHA256Str(Long.toString(System.nanoTime()) + Long.toString(RandomUtils.nextLong(0000, 9999)));
			getUserList().put(psnId, key);
			System.out.println("KEY GENERATE ::" + psnId + " is key --> " + key);
		}else {
			key = "DUPLICATE_PSNID";
		}
		
		HashMap<String, String> returnMap = new HashMap<>();
		returnMap.put("psnId", psnId);
		returnMap.put("key", key);
		
		return Mono.just(returnMap);
	}
	
	public static HashMap<String, String> getUserList() {
		return userList;
	}

	public static void setUserList(HashMap<String, String> userList) {
		WksSSEController.userList = userList;
	}
}
