package com.jtang.core.utility;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 集合工具类
 * @author 0x737263
 *
 */
public abstract class CollectionUtils {
	
	/**
	 * 连续复制一段集合对象
	 * @param source	数据源集合
	 * @param start		开始索引
	 * @param count		复制总记录数
	 * @return
	 */
	public static <T> List<T> subListCopy(List<T> source, int start, int count) {
		if ((source == null) || (source.size() == 0)) {
			return new ArrayList<T>(0);
		}

		int fromIndex = (start <= 0) ? 0 : start;
		if (start > source.size()) {
			fromIndex = source.size();
		}

		count = (count <= 0) ? 0 : count;
		int endIndex = fromIndex + count;
		if (endIndex > source.size()) {
			endIndex = source.size();
		}
		return new ArrayList<T>(source.subList(fromIndex, endIndex));
	}
	
	@SafeVarargs
	public static <T> List<T> asList(T ...items) {
		List<T> list = new ArrayList<T>();
		if (items != null) {
			for (T item : items) {
				list.add(item);
			}
		}
		return list;
	}
	
	public static <T,S> Map<T,S> asMap(T key, S val) {
		Map<T, S> map = new HashMap<T, S>();
		map.put(key, val);
		return map;
	}
	
	/**
	 * 截取分页集合
	 * @param list			集合
	 * @param startIndex	索引
	 * @param fetchCount	获取总数
	 * @return
	 */
	public static <T> List<T> pageResult(List<T> list, int startIndex, int fetchCount) {
		if ((list != null) && (list.size() > 0)) {
			if (startIndex >= list.size()) {
				return null;
			}
			startIndex = (startIndex < 0) ? 0 : startIndex;
			if (fetchCount <= 0) {
				return list.subList(startIndex, list.size());
			}
			int toIndex = Math.min(startIndex + fetchCount, list.size());
			return list.subList(startIndex, toIndex);
		}

		return null;
	}
	
	/**
	 * 判断集合是否有元素
	 * @param c
	 * @return
	 */
	public static <T> boolean isEmpty(Collection<T> c) {
		return c == null || c.size() == 0;
	}
	
	/**
	 * 判断集合是否有元素
	 * @param c
	 * @return
	 */
	public static <T> boolean isNotEmpty(Collection<T> c) {
		return c != null && c.size() > 0;
	}
	
	/**
	 * 判断字典是否有元素
	 * @param c
	 * @return
	 */
	public static <K,V> boolean isEmpty(Map<K,V> c) {
		return c == null || c.size() == 0;
	}
	
	/**
	 * 判断字典是否有元素
	 * @param c
	 * @return
	 */
	public static <K,V> boolean isNotEmpty(Map<K,V> c) {
		return c != null && c.size() > 0;
	}
	
	/**
	 * 将collection2中的元素从collection1中移除
	 * @param collection1
	 * @param collection2
	 */
	public static <T> void detainAll(Collection<T> collection1, Collection<T> collection2) {
		if (isEmpty(collection1) || isEmpty(collection2)) {
			return;
		}
		
		Set<T> set = new HashSet<T>();
		set.addAll(collection2);
		Iterator<T> iter = collection1.iterator();
		while(iter.hasNext()) {
			if (set.contains(iter.next())) {
				iter.remove();
			}
		}
	}
	
	public static <K,V> void detainAll(Map<K, V> map, Set<K> exclutions) {
		if (isEmpty(exclutions) || isEmpty(exclutions)) {
			return;
		}
		
		for (K key : exclutions) {
			map.remove(key);
		}
	}
	
	/**
	 * 判断两个集合的内容是否是一样的(顺序不一定相同)
	 * @param collection1
	 * @param collection2
	 * @return
	 */
	public static <T> boolean isSame(Collection<T> collection1, Collection<T> collection2) {
		Set<T> set1 = new HashSet<>();
		Set<T> set2 = new HashSet<>();
		set1.addAll(collection1);
		set2.addAll(collection2);
		
		for (T t : collection1) {
			if (!set2.contains(t)) {
				return  false;
			}
		}
		
		for (T t : collection2) {
			if (!set1.contains(t)) {
				return false;
			}
		}
		return true;
	}
	
	public static <T> boolean isNotSame(Collection<T> collection1, Collection<T> collection2) {
		return isSame(collection1, collection2) == false;
	}
}