package com.jtang.gameserver.module.buffer.model;

import static com.jiatang.common.model.HeroVOAttributeKey.NONE;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.jiatang.common.model.BufferVO;
import com.jiatang.common.model.HeroVOAttributeKey;
import com.jtang.core.utility.CollectionUtils;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;

/**
 * 一个仙人的buffer信息
 * @author vinceruan
 *
 */
public class HeroBuffer implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7380676883592873363L;

	/**
	 * 仙人的配置ID
	 */
	public int heroId;
	
	/**
	 * buffer列表
	 * 格式是:
	 * Map<BufferSourceType, List<BufferVO>>
	 */
	public Map<Integer, List<BufferVO>> bufferTypeMap;
	
	/**
	 * 根据分类移除buffer
	 * @param sourceType
	 */
	public List<BufferVO> removeBuffersByType(Integer sourceType) {		
		List<BufferVO> list = bufferTypeMap.remove(sourceType);		
		return list;
	}
	
	/**
	 * 添加buffer
	 * @param buf
	 */
	public void addBuffer(BufferVO buf) {		
		List<BufferVO> list = bufferTypeMap.get(buf.sourceType);
		if (list == null) {
			list = new ArrayList<BufferVO>();
			bufferTypeMap.put(buf.sourceType, list);
		}		
		list.add(buf);
	}

	/**
	 * 构造函数,根据字符串构造HeroBuffer对象
	 * @param bufferStr
	 */
	public HeroBuffer(String bufferStr) {
		this.bufferTypeMap = new ConcurrentHashMap<Integer, List<BufferVO>>();
		
		List<String> bufferList = StringUtils.delimiterString2List(bufferStr, Splitable.ATTRIBUTE_SPLIT);
		Iterator<String> iter = bufferList.iterator();
		String heroId = iter.next();
		this.heroId = Integer.valueOf(heroId);
		
		while (iter.hasNext()) {
			String bufStr = iter.next();
			List<String> str = StringUtils.delimiterString2List(bufStr, Splitable.BETWEEN_ITEMS);
			int addVal = Integer.valueOf(str.get(0));
			byte code = Byte.valueOf(str.get(1));
			int sourceType = Integer.valueOf(str.get(2));
			HeroVOAttributeKey key = HeroVOAttributeKey.getByCode(code);
			if (key == NONE) continue;
			BufferVO buf = new BufferVO(key, addVal, sourceType);
			
			List<BufferVO> list = bufferTypeMap.get(buf.sourceType);
			if (list == null) {
				list = new ArrayList<BufferVO>();
				bufferTypeMap.put(buf.sourceType, list);
			}
			list.add(buf);			
		}
	}
	
	public List<BufferVO> get(){
		List<BufferVO> list = new ArrayList<>();
		for (Collection<BufferVO> buffvos : bufferTypeMap.values()) {
			list.addAll(buffvos);
		}
		return list;
	}
	
	public HeroBuffer(int heroId) {
		this.heroId = heroId;
		this.bufferTypeMap = new ConcurrentHashMap<>();
	}
	
	/**
	 * 将实体转化成字符串
	 * @return
	 */
	public String parseModel2Str(){
		List<String> list = new ArrayList<String>();
		list.add(heroId+"");
		if (CollectionUtils.isNotEmpty(bufferTypeMap)) {			
			for (List<BufferVO> bufList : bufferTypeMap.values()) {
				for (BufferVO buf : bufList) {
					StringBuilder sb = new StringBuilder();
					sb.append(buf.addVal).append(Splitable.BETWEEN_ITEMS)
					  .append(buf.key.getCode()).append(Splitable.BETWEEN_ITEMS)  
					  .append(buf.sourceType);
					list.add(sb.toString());
				}
			}
		}
		if (list.size() > 1) {
			return StringUtils.collection2SplitString(list, Splitable.ATTRIBUTE_SPLIT);
		} else {
			return null;
		}
	}
}
