package com.jtang.gameserver.dataconfig.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.gameserver.dataconfig.model.BreakThroughConfig;

/**
 * 魂魄突破配置管理类
 * @author vinceruan
 *
 */
@Component
public class BreakThroughService extends ServiceAdapter {
	static Map<Integer, Map<Integer, BreakThroughConfig>> CONFIG = new HashMap<Integer, Map<Integer,BreakThroughConfig>>();
	
	@Override
	public void clear() {
		CONFIG.clear();
	}
	
	@Override
	public void initialize() {
		List<BreakThroughConfig> list = this.dataConfig.listAll(this, BreakThroughConfig.class);
		for (BreakThroughConfig config : list) {
			Map<Integer, BreakThroughConfig> map = CONFIG.get(config.getStar());
			if (map == null) {
				map = new HashMap<>();
				CONFIG.put(config.getStar(), map);
			}
			map.put(config.getBreakOrder(), config);
		}
	}
	
	/**
	 * 根据星级和突破次数获取突破配置
	 * @param star
	 * @param breakOrder
	 * @return
	 */
	public static BreakThroughConfig get(int star, int breakOrder) {
		return CONFIG.get(star).get(breakOrder);
	}
}
