package com.jtang.gameserver.component.lop.impl;

import java.util.Date;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.jtang.core.lop.IRequest;
import com.jtang.core.lop.LopClient;
import com.jtang.core.lop.result.LopResult;
//import com.alibaba.fastjson.TypeReference;
import com.jtang.core.utility.DateUtils;
import com.jtang.core.utility.HttpUtils;
import com.jtang.core.utility.SecurityUtils;
import com.jtang.core.utility.StringUtils;

@Component
public class LopClientImpl implements LopClient {
	private static final Logger LOGGER = LoggerFactory.getLogger(LopClient.class);
	
	@Autowired
	@Qualifier("lop.url")
    private String serverUrl;
	@Autowired
	@Qualifier("lop.appkey")
    private String appKey;
	@Autowired
	@Qualifier("lop.appsecret")
    private String appSecret;
	
    private String format = "json";
    private boolean encrypt = false;

	@Override
	public LopResult executeResult(IRequest request) {
		String json = execute(request);
		if (StringUtils.isBlank(json)) {
			return new LopResult();
		}
		
		return JSON.parseObject(json, LopResult.class);
	}

//	@Override
//	public <T> TLopResult<T> executeTResult(IRequest request, Class<T> clazz) {
//		String json = execute(request);
//		if (StringUtils.isBlank(json)) {
//			return new TLopResult<T>();
//		}
//		
//		return JSON.parseObject(json, new TypeReference<TLopResult<T>>() {
//		});
//	}
//
//	@Override
//	public <T> ListLopResult<T> executeListResult(IRequest request, Class<T> clazz) {
//		String json = execute(request);
//		if (StringUtils.isBlank(json)) {
//			return new ListLopResult<T>();
//		}
//
//		return JSON.parseObject(json, new TypeReference<ListLopResult<T>>() {
//		});
//	}
	
	@Override
	public String execute(IRequest request) {
		TreeMap<String, String> dic = new TreeMap<>();
		dic.putAll(request.getParameters());
		dic.put("method", request.invokeName());
		dic.put("v", "0.0.1");
		dic.put("appkey", this.appKey);
		dic.put("format", format.toString().toLowerCase());
		dic.put("timestamp", DateUtils.formatTime(new Date()));
		dic.put("encrypt", String.valueOf(encrypt).toLowerCase());
		String sign = generateSign(dic, appSecret);
		dic.put("sign", sign);

		try {
			String fullUrl = this.serverUrl + request.moduleName() + "/" + request.invokeName();
			return HttpUtils.sendPost(fullUrl, dic);
		} catch (Exception ex) {
			LOGGER.error("", ex);
		}
		return "";
	}

	private String generateSign(TreeMap<String, String> parameters, String secret) {
		// 把所有参数名和参数值串在一起
		StringBuilder query = new StringBuilder(secret);
		for (Entry<String, String> entry : parameters.entrySet()) {
			if (StringUtils.isNotBlank(entry.getKey()) && StringUtils.isNotBlank(entry.getValue())) {
				query.append(entry.getKey()).append(entry.getValue());
			}
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(String.format("lop sign text:[%s] md5:[%s]", query.toString(), SecurityUtils.md5(query.toString())));
		}
		return SecurityUtils.md5(query.toString());
	}

}
