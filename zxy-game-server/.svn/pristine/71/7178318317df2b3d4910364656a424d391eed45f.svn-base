package com.jtang.gameserver.module.user.platform;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.jiatang.common.GameStatusCodeConstant;
import com.jtang.core.result.TResult;
import com.jtang.core.utility.HttpUtils;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.component.Game;

@Component
public class PlatformInvokeImpl implements PlatformInvoke {
	private static final Logger LOGGER = LoggerFactory.getLogger(PlatformInvokeImpl.class);
	@Autowired
	@Qualifier("httputil.login.validate_url")
	private String url;
	@Override
	public TResult<PlatformLoginResult> login(Integer platformId, String token) {
		Map<String, String> param = new HashMap<>();
		param.put("PlatformId", platformId.toString());
		param.put("Extend", token);
		String text = "";
		try {
			text = HttpUtils.sendPost(url, param);
			if (StringUtils.isNotBlank(text)) {
				PlatformJsonResult result =  JSON.parseObject(text, PlatformJsonResult.class);
				if (Game.checkAllowUID(result.UserId) == false) {
					return TResult.valueOf(GameStatusCodeConstant.TOKEN_VALIDATE_ERROR);
				}
				if (result.isOK()) {
					return TResult.sucess(PlatformLoginResult.valueOf(result.UserId));
				} else {
					LOGGER.warn(String.format("url:[%s] suffix:[%s] http text:[%s]", url, param, text));
				}
			}
		} catch (Exception ex) {
			LOGGER.warn(String.format("url:[%s] params:[%s] http text:[%s]", url, param, text), ex);
		}
		LOGGER.warn(String.format("url:[%s] params:[%s] http text:[%s]", url, param, text));
		return TResult.valueOf(GameStatusCodeConstant.TOKEN_VALIDATE_ERROR);
	}

}
