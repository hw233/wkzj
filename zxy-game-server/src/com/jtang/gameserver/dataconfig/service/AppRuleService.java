package com.jtang.gameserver.dataconfig.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.core.utility.PackageScanner;
import com.jtang.gameserver.dataconfig.model.AppRuleConfig;
import com.jtang.gameserver.module.app.model.annotation.AppVO;
import com.jtang.gameserver.module.app.model.extension.BaseRuleConfigVO;

/**
 * 角色服务类
 * 
 * @author 0x737263
 * 
 */
@Component
public class AppRuleService extends ServiceAdapter {
	private static final Logger LOGGER = LoggerFactory.getLogger(AppRuleService.class);
	
	@Autowired
	@Qualifier("appconfig.rule")
	private String packageScanRuleVO;

	/**
	 * 角色升级配置集合 key:等级 value:升级配置
	 */
	private static Map<Long, AppRuleConfig> APP_RULE_MAPS = new HashMap<>();

	@Override
	public void clear() {
		APP_RULE_MAPS.clear();
	}

	@Override
	public void initialize() {
		List<AppRuleConfig> list = dataConfig.listAll(this, AppRuleConfig.class);

		for (AppRuleConfig appRuleConfig : list) {
			APP_RULE_MAPS.put(appRuleConfig.getAppId(), appRuleConfig);
		}

		Collection<Class<BaseRuleConfigVO>> collectionRule = PackageScanner.scanPackages(packageScanRuleVO);
		if (collectionRule == null || collectionRule.isEmpty()) {
			LOGGER.error(String.format("在 [%s]包下没有扫描到实体类!", packageScanRuleVO));
			return;
		}

		Map<Integer, BaseRuleConfigVO> mapRule = new HashMap<Integer, BaseRuleConfigVO>();
		for (Class<BaseRuleConfigVO> clazz : collectionRule) {
			BaseRuleConfigVO obj = initRuleVO(clazz);
			if (obj != null) {
				mapRule.put(obj.getEffectId().getId(), obj);
			}
		}

		for (AppRuleConfig appRuleConfig : list) {
			BaseRuleConfigVO vo = mapRule.get(appRuleConfig.getEffect());
			if (vo != null) {
				try {
					vo = vo.getClass().newInstance();
				} catch (InstantiationException e) {
					LOGGER.error("{}",e);
				} catch (IllegalAccessException e) {
					LOGGER.error("{}",e);
				}
				vo.init(appRuleConfig.getRule());
				appRuleConfig.setConfigRuleVO(vo);
			}
		}
	}

	private BaseRuleConfigVO initRuleVO(Class<BaseRuleConfigVO> clazz) {
		AppVO extVO = clazz.getAnnotation(AppVO.class);
		if (extVO == null) {
			LOGGER.warn(String.format("appRecord扩展字段VO没有注解,className:[%s]", clazz.getName()));
			return null;
		}

		try {
			return clazz.newInstance();
		} catch (InstantiationException e) {
			LOGGER.error("{}", e);
		} catch (IllegalAccessException e) {
			LOGGER.error("{}", e);
		}
		return null;

	}

	public static AppRuleConfig get(long appId) {
		return APP_RULE_MAPS.get(appId);
	}

	public static Set<Long> getAllApp() {
		return APP_RULE_MAPS.keySet();
	}
	
	public static Set<Integer> getAllParser(){
		Set<Integer> parserSet = new HashSet<>();
		for (AppRuleConfig config : APP_RULE_MAPS.values()) {
			parserSet.add(config.getEffect());
		}
		return parserSet;
	}

	public static Set<Long> getAppId(int parserId) {
		Set<Long> appIdSet = new HashSet<>();
		for (AppRuleConfig config : APP_RULE_MAPS.values()) {
			if (config.getEffect() == parserId) {
				appIdSet.add(config.getAppId());
			}
		}
		return appIdSet;
	}

}
