package com.jtang.gameserver.module.adventures.favor.effect;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.jtang.core.result.TResult;
import com.jtang.gameserver.dbproxy.entity.Favor;
import com.jtang.gameserver.module.adventures.favor.model.PrivilegeVO;
import com.jtang.gameserver.module.adventures.favor.type.FavorParserType;
/**
 * 特权解析器
 * @author ludd
 *
 */
@Component
public abstract class FavorParser {
	
	private static Map<Integer, FavorParser> map = new HashMap<>();

	/**
	 * 执行特权
	 * @param actorId
	 * @param favor
	 * @return 
	 */
	public abstract TResult<PrivilegeVO> execute(long actorId, Favor favor);
	
	public abstract int getParserId();
	
	@PostConstruct
	void init() {
		map.put(getParserId(), this);
	}
	
	public static FavorParser getFavorParser(FavorParserType favorParserType){
		if (map.containsKey(favorParserType.getType())){
			return map.get(favorParserType.getType());
		}
		return null;
	}
}
