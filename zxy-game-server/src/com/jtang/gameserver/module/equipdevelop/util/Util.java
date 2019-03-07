package com.jtang.gameserver.module.equipdevelop.util;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.module.equipdevelop.model.ItemDTO;

/**
 * 工具类
 * @author hezh
 *
 */
public class Util {

	/**
	 * 数据转换接口
	 * @author hezh
	 *
	 * @param <T>
	 * @param <V>
	 */
	public interface Change <T,V>{
		public List<T> changeTo(V v);
	}
	
	/**
	 * list根据一定规则转成map
	 * @author hezh
	 *
	 * @param <T>
	 * @param <V>
	 * @param <E>
	 */
	public interface ChangeListToMap<T,V,E>{
		public Map<T,V> changeTo(List<E> e);
	}
	
	/**
	 * 将v按一定规则转换成Map<T,V>
	 * @param v
	 * @param change
	 * @return
	 */
	public static <T,V,E> Map<T,V> changeListToMap(List<E> e,ChangeListToMap<T,V,E> change){
		return change.changeTo(e);
	}
	
	
	/**
	 * 将v对象按一定规则转换为t对象
	 * @param change
	 * @param v
	 * @return
	 */
	public static <T, V> List<T> change(V v,Change<T, V> change) {
		return change.changeTo(v);
	}
	
	/**
	 * 解析消耗物
	 * @param str
	 * @return
	 */
	public static List<ItemDTO> parserConsumes(String str){
		List<ItemDTO> consumes = Util.change(str,new Change<ItemDTO, String>() {
			@Override
			public List<ItemDTO> changeTo(final String v) {
				//v（物品ID_数量|...)
				List<String[]> list = StringUtils.delimiterString2Array(v);
				List<ItemDTO> dtos = new ArrayList<ItemDTO>();
				for(String[] i : list){
					dtos.add(new ItemDTO(0,Integer.parseInt(i[0]),Integer.parseInt(i[1])));
				}
				return dtos;
			}
		});
		return consumes;
	}
	
	/**
	 * Format字符串
	 * @param message
	 * @param param
	 * @return
	 */
    public static String stringFormat(String message, Object... param) {
        if (param == null || param.length == 0) {
            return message;
        }
        MessageFormat messageFormat = new MessageFormat(message);
        return messageFormat.format(param);
    }
}
