package com.jtang.core.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;

/**
 * 缓存监听处理抽像类
 * @author 0x737263
 *
 */
public abstract class AbstractCacheListener implements InitializingBean {
	protected Logger LOGGER = LoggerFactory.getLogger(getClass());
	//缓存输出的单独日志
	protected Logger CACHE_LOGGER = LoggerFactory.getLogger("cachelistener");
	
	/**
	 * 每次处理角色数
	 */
	@Autowired(required = false)
	@Qualifier("cache.each_clean_actor_num")
	protected Integer eachCleanActorNum = 300; 
	
	/**
	 * 退出超出x秒后进行清理
	 */
	@Autowired(required = false)
	@Qualifier("cache.logout_expired_time")
	protected Integer logoutExpiredTime = 1800;

	@Autowired(required = false)
	@Qualifier("cache.trigger_schedule_time")
	protected Integer triggerScheduleTime = 5;
	
	@Autowired
	protected ApplicationContext applicationContext;
	
	/**
	 * 实现接口的缓存执行类
	 */
	protected static List<Entry<String,CacheListener>> CACHE_LISTENER_LIST = new ArrayList<>();

	/**
	 * 子类初始化
	 */
	protected abstract void init();
	
	/**
	 * 清理指定actor的数据库缓存
	 * @param actorId
	 */
	public abstract void clearSpecifyActorId(long actorId);
	
	@Override
	public void afterPropertiesSet() throws Exception {
		Map<String, CacheListener> cacheClazz = applicationContext.getBeansOfType(CacheListener.class);
		for (Entry<String, CacheListener> cacheEntry : cacheClazz.entrySet()) {
			CACHE_LISTENER_LIST.add(cacheEntry);
		}	
		init();
	}
	

}
