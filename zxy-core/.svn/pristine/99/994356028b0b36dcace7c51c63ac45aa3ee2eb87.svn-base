package com.jtang.core.utility;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author 0x737263
 *
 */
public class BeanUtils {
	private static Logger LOGGER = LoggerFactory.getLogger(BeanUtils.class);

	/**
	 * 对象属性拷贝
	 * @param source
	 * @param target
	 * @param ignoreFields
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void copyProperties(Object source, Object target, String... ignoreFields) {
		if ((source == null) || (target == null)) {
			return;
		}
		Class<?> actualEditable = target.getClass();
		PropertyDescriptor[] targetPds = org.springframework.beans.BeanUtils.getPropertyDescriptors(actualEditable);
		List<String> ignoreList = (ignoreFields != null) ? Arrays.asList(ignoreFields) : null;

		for (PropertyDescriptor targetPd : targetPds)
			if ((targetPd.getWriteMethod() != null) && (((ignoreFields == null) || (!ignoreList.contains(targetPd.getName()))))) {
				PropertyDescriptor sourcePd = org.springframework.beans.BeanUtils.getPropertyDescriptor(source.getClass(), targetPd.getName());
				if ((sourcePd == null) || (sourcePd.getReadMethod() == null))
					continue;
				try {
					Method readMethod = sourcePd.getReadMethod();
					if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
						readMethod.setAccessible(true);
					}
					Object value = readMethod.invoke(source, new Object[0]);

					Class sourceType = sourcePd.getPropertyType();
					PropertyDescriptor pd = org.springframework.beans.BeanUtils.getPropertyDescriptor(target.getClass(), targetPd.getName());
					Class targetType = pd.getPropertyType();

					if ((sourceType.isEnum()) && (((Integer.class.equals(targetType)) || (Integer.TYPE.equals(targetType))))) {
						if (value == null)
							value = Integer.valueOf(0);
						else
							value = Integer.valueOf(Enum.valueOf(sourceType, String.valueOf(value)).ordinal());
					} else if ((targetType.isEnum()) && (((Integer.class.equals(sourceType)) || (Integer.TYPE.equals(sourceType))))) {
						if (value == null) {
							value = Integer.valueOf(0);
						}
						int intValue = ((Integer) value).intValue();
						Method method = targetType.getMethod("values", new Class[0]);
						Object[] enumValues = (Object[]) method.invoke(targetType, new Object[0]);
						if ((intValue >= 0) && (intValue < enumValues.length)) {
							value = enumValues[intValue];
						}

					}

					if ((String.class.equals(sourceType)) && (Number.class.isAssignableFrom(targetType))) {
						Constructor constructor = targetType.getConstructor(new Class[] { String.class });
						value = constructor.newInstance(new Object[] { String.valueOf(value) });
					} else if ((String.class.equals(targetType)) && (Number.class.isAssignableFrom(sourceType))) {
						value = String.valueOf(value);
					}

					if ((((Boolean.class.equals(sourceType)) || (Boolean.TYPE.equals(sourceType))))
							&& (((Integer.class.equals(targetType)) || (Integer.TYPE.equals(targetType))))) {
						value = Integer.valueOf((((Boolean) value).booleanValue()) ? 1 : 0);
					} else if ((((Boolean.class.equals(targetType)) || (Boolean.TYPE.equals(targetType))))
							&& (((Integer.class.equals(sourceType)) || (Integer.TYPE.equals(sourceType))))) {
						value = Boolean.valueOf(((Integer) value).intValue() > 0);
					}

					Method writeMethod = targetPd.getWriteMethod();
					if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
						writeMethod.setAccessible(true);
					}

					writeMethod.invoke(target, new Object[] { value });
				} catch (Throwable e) {
					LOGGER.error("BeanUtil copy object error.", e);
				}
			}
	}
	
	/**
	 * 将对象的结构格式化
	 * @param obj
	 * @return
	 */
	public String formateBean(Object obj) {
		StringBuilder sb = new StringBuilder();
		if (Number.class.isAssignableFrom(obj.getClass())) {
			sb.append(obj.toString());
		} else if (obj instanceof Character) {
			sb.append(obj.toString());
		} else if (obj instanceof String) {
			sb.append(obj.toString());
		} else if (obj instanceof List) {
			
		} else if (obj instanceof Map) {
			
		} else if (obj instanceof Set) {
			
		} else {
			
		}
		
		
		return sb.toString();
	}

}