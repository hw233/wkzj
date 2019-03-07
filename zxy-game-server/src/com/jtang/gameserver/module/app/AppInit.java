package com.jtang.gameserver.module.app;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.jtang.core.utility.PackageScanner;
import com.jtang.gameserver.dataconfig.model.AppRuleConfig;
import com.jtang.gameserver.dataconfig.service.AppRuleService;
import com.jtang.gameserver.module.app.model.annotation.AppVO;
import com.jtang.gameserver.module.app.model.extension.BaseGlobalInfoVO;
import com.jtang.gameserver.module.app.model.extension.BaseRecordInfoVO;
import com.jtang.gameserver.module.app.type.EffectId;

@Component
public class AppInit {
	private static final Logger LOGGER = LoggerFactory.getLogger(AppInit.class);

	private static Map<EffectId, BaseRecordInfoVO> RECORD_INFO_MAPS = new HashMap<EffectId, BaseRecordInfoVO>();
	private static Map<EffectId, BaseGlobalInfoVO> GLOBAL_INFO_MAPS = new HashMap<EffectId, BaseGlobalInfoVO>();
	
	@Autowired
	@Qualifier("appconfig.record")
	private String packageScanRecordVo;
	
	@Autowired
	@Qualifier("appconfig.global")
	private String packageScanGlobalVo;
	
	@PostConstruct
	private void init() {
		Collection<Class<BaseRecordInfoVO>> collectionRecord = PackageScanner.scanPackages(packageScanRecordVo);
		if (collectionRecord == null || collectionRecord.isEmpty()) {
			LOGGER.warn(String.format("在 [%s]包下没有扫描到实体类!", packageScanRecordVo));
			return;
		}
		
		for (Class<BaseRecordInfoVO> clazz : collectionRecord) {
			initRecordInfoVO(clazz);
		}
		Collection<Class<BaseGlobalInfoVO>> collectionConfig = PackageScanner.scanPackages(packageScanGlobalVo);
		if (collectionConfig == null || collectionConfig.isEmpty()) {
			LOGGER.warn(String.format("在 [%s]包下没有扫描到实体类!", packageScanGlobalVo));
			return;
		}
		
		for (Class<BaseGlobalInfoVO> clazz : collectionConfig) {
			initConfigInfoVO(clazz);
		}
		LOGGER.info("all AppVO class load complete!");
	}
	
	private void initRecordInfoVO(Class<BaseRecordInfoVO> clazz) {
		AppVO extVO = clazz.getAnnotation(AppVO.class);
		if (extVO == null) {
			LOGGER.warn(String.format("appRecord扩展字段VO没有注解,className:[%s]", clazz.getName()));
			return;
		}

		try {
			BaseRecordInfoVO obj = clazz.newInstance();
			RECORD_INFO_MAPS.put(obj.getAppId(), obj);
		} catch (InstantiationException e) {
			LOGGER.error("{}", e);
		} catch (IllegalAccessException e) {
			LOGGER.error("{}", e);
		}
	}
	
	private void initConfigInfoVO(Class<BaseGlobalInfoVO> clazz) {
		AppVO extVO = clazz.getAnnotation(AppVO.class);
		if (extVO == null) {
			LOGGER.warn(String.format("appConfig扩展字段VO没有注解,className:[%s]", clazz.getName()));
			return;
		}

		try {
			BaseGlobalInfoVO obj = clazz.newInstance();
			GLOBAL_INFO_MAPS.put(obj.getAppId(), obj);
		} catch (InstantiationException e) {
			LOGGER.error("{}", e);
		} catch (IllegalAccessException e) {
			LOGGER.error("{}", e);
		}
	}

	/**
	 * 获取app记录扩展字段vo
	 * @param appId
	 * @return
	 */
	public static BaseRecordInfoVO getRecordInfoVO(long appId, String record) {
		AppRuleConfig config = AppRuleService.get(appId);
		if(config == null){
			return null;
		}
		BaseRecordInfoVO vo = RECORD_INFO_MAPS.get(EffectId.getById(config.getEffect()));
		if (vo != null) {
			return vo.valueOf(record);
		}
		return null;
	}
	
	/**
	 * 获取appConfig扩展字段vo
	 * @param appId
	 * @return
	 */
	public static BaseGlobalInfoVO getGlobalInfoVO(long appId, String record) {
		EffectId effectId = EffectId.getById((int)appId);
		//单独处理
		if (effectId == EffectId.EFFECT_ID_19) {
			BaseGlobalInfoVO vo = GLOBAL_INFO_MAPS.get(effectId);
			if (vo != null) {
				vo.init(record);
				return vo;
			}
		}
		AppRuleConfig config = AppRuleService.get(appId);
		if(config == null){
			return null;
		}
		BaseGlobalInfoVO vo = GLOBAL_INFO_MAPS.get(EffectId.getById(config.getEffect()));
		if (vo != null) {
			vo.init(record);
			return vo;
		}
		return null;
	}
	
}
