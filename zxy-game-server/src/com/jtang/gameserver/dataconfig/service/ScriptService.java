package com.jtang.gameserver.dataconfig.service;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.dataconfig.model.ScriptConfig;
//import org.springframework.stereotype.Component;

/**
 * 脚本配置
 * @author 0x737263
 *
 */
public class ScriptService extends ServiceAdapter {

	private static final Logger LOGGER = LoggerFactory.getLogger(ScriptService.class);
	
	private static Map<Integer,String> SCRIPT_CONFIG_MAPS = new HashMap<Integer,String>();
	
	/**
	 * 配置文件路径
	 */
	@Autowired(required = false)
	@Qualifier("dataconfig.script_path")
	private String path = "dataconfig" + File.separator;
	
	@Override
	public void clear() {
		SCRIPT_CONFIG_MAPS.clear();
	}
	
	@Override
	public void initialize() {
		List<ScriptConfig> list = dataConfig.listAll(this, ScriptConfig.class);
		
		String fullPath;
		URL resource;
		// 批量加载js脚本文件
		for (ScriptConfig cfg : list) {

			fullPath = (path + cfg.getFile());
			try {
				resource = getClass().getClassLoader().getResource(fullPath);
				if (resource == null) {
					LOGGER.error(String.format("load javascript file [%s] error. file path [%s] not exitst!", cfg.getFile(), fullPath));
					continue;
				}
				String script = StringUtils.inputStream2String(resource.openStream());

				SCRIPT_CONFIG_MAPS.put(cfg.getId(), script);

			} catch (Exception ex) {
				LOGGER.error(String.format("load javascript file [%s] error,check script content please!", cfg.getFile(), fullPath));
			} finally {
				resource = null;
			}
		}
	}
	
	/**
	 * 获取脚本内容
	 * @param id
	 * @return
	 */
	public static String getScript(int id) {
		if(SCRIPT_CONFIG_MAPS.containsKey(id)) {
			return SCRIPT_CONFIG_MAPS.get(id);
		}
		
		return "";
	}

}
