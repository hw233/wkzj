package com.jtang.core.dataconfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.parse.DataParser;
import com.jtang.core.schedule.Schedule;
import com.jtang.core.utility.PackageScanner;

/**
 * 数据配置接口功能实现
 * @author 0x737263
 *
 */
@Component
public class DataConfigImpl implements DataConfig, InitializingBean {
	private static final Logger LOGGER = LoggerFactory.getLogger(DataConfigImpl.class);

	@Autowired
	private ApplicationContext applicationContext;

	/**
	 * 配置文件格式(xml,json)
	 */
	@Autowired(required = false)
	@Qualifier("datacofig.format")
	private String format = "xml";

	/**
	 * 配置文件路径
	 */
	@Autowired(required = false)
	@Qualifier("dataconfig.path")
	private String path = "dataconfig" + File.separator;

	/**
	 * 数据配置映射对应的包
	 */
	@Autowired(required = false)
	@Qualifier("dataconfig.package_scan")
	private String packageScan = "com.jtang.core.dataconfig.model";

	/**
	 * 配置文件扩展名
	 */
	private String extension = ".xml";
	
	@Autowired
	private Schedule schedule;

	/**
	 * 所有数据配置存储集合 key:className value: extend ModelAdapter
	 */
	private static ConcurrentHashMap<String, List<ModelAdapter>> MODEL_MAPS = new ConcurrentHashMap<>();
	
	/**
	 * model类与名称的映射
	 * key:DataFile.fileName() value:Class
	 */
	private static ConcurrentHashMap<String, Class<ModelAdapter>> MODEL_CLASS_MAPS = new ConcurrentHashMap<>();
	
	/**
	 * model类被哪些Service调用了的索引(用于反向初始化service) 
	 */
	private static ConcurrentHashMap<String, Set<ServiceAdapter>> MODEL_BE_INVOKE_MAPS = new ConcurrentHashMap<>();
	
	@Override
	public void afterPropertiesSet() throws Exception {
		initModelAdapterList();
		initServiceAdapterList();
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends ModelAdapter> List<T> listAll(ServiceAdapter invokeClazz, Class<T> modelClass) {
		String name = modelClass.getName();
		List<ModelAdapter> list = MODEL_MAPS.get(name);

		if (list == null) {
			LOGGER.warn(String.format("get data config file: [%s] is null!", name));
			return new ArrayList<>();
		}

		// 被调用的model类与 调用者Service建立关联
		DataFile df = modelClass.getAnnotation(DataFile.class);
		if (df != null) {
			Set<ServiceAdapter> serviceClazzSet = MODEL_BE_INVOKE_MAPS.get(df.fileName());
			if (serviceClazzSet == null) {
				serviceClazzSet = new HashSet<>();
			}
			serviceClazzSet.add(invokeClazz);
			MODEL_BE_INVOKE_MAPS.put(df.fileName(), serviceClazzSet);
		}

		return (List<T>) list;
	}
	
	@Override
	public boolean reload(String fileName) {
		Class<ModelAdapter> clazz = MODEL_CLASS_MAPS.get(fileName);
		if (clazz == null) {
			return false;
		}

		// 重载model
		if (initModelAdapter(clazz)) {
			// 反向重载Service类
			Set<ServiceAdapter> serviceClazzSet = MODEL_BE_INVOKE_MAPS.get(fileName);
			if (serviceClazzSet != null) {
				for (ServiceAdapter service : serviceClazzSet) {
					initServiceAdapter(service);
				}
			}
			LOGGER.info(String.format("reload file:[%s]", fileName));
			return true;
		}

		return false;
	}

	@Override
	public boolean reload(String fileName, URL url) {
		if (fileName.isEmpty() || url == null) {
			return false;
		}

		String filePath = getFullPath(fileName);
		URL resource = null;
		InputStream inputStream = null;
		OutputStream outputStream = null;
		try {
			resource = getClass().getClassLoader().getResource(filePath);
			inputStream = url.openStream();
			outputStream = new FileOutputStream(URLDecoder.decode(resource.getPath(), "utf-8"));
			byte[] buffer = new byte[1024];
			int readed = 0; // 一次读多个，readed代表当前已读的数据总数
			while ((readed = inputStream.read(buffer)) != -1) {
				// 从第0位写，reader代表读写几位
				outputStream.write(buffer, 0, readed);
			}

			return reload(fileName);
		} catch (Exception e) {
			LOGGER.error("{}", e);
		} finally {
			try {
				inputStream.close();
				outputStream.close();
			} catch (Exception e) {
				LOGGER.error("{}", e);
			}
		}
		return false;
	}
	
	/**
	 * 获取所有配置文件名
	 */
	@Override
	public Set<String> getAllConfigName() {
		return MODEL_CLASS_MAPS.keySet();
	}
	
	/**
	 * 
	 * @param clazz
	 * @return
	 */
	@Override
	public boolean checkModelAdapter(String fileName, InputStream inputStream) {
		Class<ModelAdapter> clazz = MODEL_CLASS_MAPS.get(fileName);
		if (clazz == null) {
			return false;
		}

		try {
			DataFile df = clazz.getAnnotation(DataFile.class);
			if (df == null) {
				return false;
			}

			List<ModelAdapter> list = new ArrayList<>();
			list = getDataParser().parse(inputStream, clazz);

			if (list.size() < 1) {
				return false;
			}
			return true;
		} catch (Exception e) {
			LOGGER.error(String.format("file: [%s] read error!", clazz.getName()), e);
			return false;
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				LOGGER.error("{}", e);
			}
		}
	}

	/**
	 * 初始化ModelAdapter
	 */
	private void initModelAdapterList() {

		// 通过包名扫描获取对应的类集合
		Collection<Class<ModelAdapter>> collection = PackageScanner.scanPackages(packageScan);
		if (collection == null || collection.isEmpty()) {
			LOGGER.error(String.format("在 [%s]包下没有扫描到实体类!", path));
			return;
		}
		
		for (Class<ModelAdapter> clazz : collection) {
			initModelAdapter(clazz);
		}
		LOGGER.info("all data config file load complete!");
	}
	
	/**
	 * 初始化
	 * @param clazz
	 */
	public boolean initModelAdapter(Class<ModelAdapter> clazz) {
		try {
			DataFile df = clazz.getAnnotation(DataFile.class);
			if (df == null) {
				return false;
			}

			String fullPath = getFullPath(df.fileName());
			URL resource = getClass().getClassLoader().getResource(fullPath);
			if (resource == null) {
				LOGGER.error(String.format("load data config file [%s] error. file name [%s] not exists!", clazz.getName(), fullPath));
				return false;
			}

			List<ModelAdapter> list = new ArrayList<>();
			InputStream input = null;
			input = resource.openStream();
			list = getDataParser().parse(input, clazz);
			input.close();
			
			if(list.size() < 1){
				return false;
			}
						
			for (ModelAdapter obj : list) {
				obj.initialize();
			}
			
			synchronized (MODEL_MAPS) {
				if (MODEL_MAPS.contains(clazz.getName())) {
					MODEL_MAPS.clear();
				}
				MODEL_MAPS.put(clazz.getName(), list);
			}
			MODEL_CLASS_MAPS.put(df.fileName(), clazz);

			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(String.format("[%s] file load complete!", fullPath));
			}
			return true;
		} catch (Exception e) {
			LOGGER.error(String.format("file: [%s] read error!", clazz.getName()), e);
			return false;
		}
	}


	/**
	 * 循环 执行适配器服务类的初始化方法
	 */
	private void initServiceAdapterList() {
		Map<String, ServiceAdapter> list = this.applicationContext.getBeansOfType(ServiceAdapter.class);
		if (list != null && !list.isEmpty()) {
			for (ServiceAdapter service : list.values()) {
				initServiceAdapter(service);
			}
		}
	}
	
	public void initServiceAdapter(ServiceAdapter clazz) {
		try {
			synchronized (clazz) {
				clazz.clear();
				clazz.initialize();
			}
		} catch (Exception e) {
			LOGGER.error("initializeServiceAdapter error!", e);
		}
	}
	
	/**
	 * 获取数据解析器
	 * @return
	 */
	private DataParser getDataParser() {
		return this.applicationContext.getBean(format.toUpperCase() + "DataParser", DataParser.class);
	}
	
	/**
	 * 根据文件名获取全路径
	 * @param fileName
	 * @return
	 */
	private String getFullPath(String fileName) {
		return this.path + fileName + this.extension;
	}


}
