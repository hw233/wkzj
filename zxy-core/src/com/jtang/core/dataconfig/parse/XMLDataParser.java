package com.jtang.core.dataconfig.parse;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.utility.StringUtils;


/**
 * xml格式数据解析
 * 
 * @author 0x737263
 */
@Component
public class XMLDataParser implements DataParser {

	private static final Logger LOGGER = LoggerFactory.getLogger(XMLDataParser.class);
	
	@Override
	public <T extends ModelAdapter> List<T> parse(InputStream stream, Class<T> clazz) {
		List<T> objList = new ArrayList<T>();
		SAXReader saxReader = new SAXReader();
		Document document;
		try {
			Map<String, Field> fieldList = getFieldList(clazz);
		
			document = saxReader.read(stream);
			Element rootElement = document.getRootElement();
			List<?> elementList = rootElement.elements();
			
			//检查nameList是否都存在.不存在则提示error.
			checkField(clazz.getName(), fieldList, elementList);
			
			for (int i = 0; i < elementList.size(); i++) {
				Element subElement = (Element) elementList.get(i);
				List<?> attributeList = subElement.attributes();

				T object = newInstance(clazz);
				if (object == null) {
					LOGGER.error(String.format("data config [%s] file new instance error!", clazz.getName()));
					return null;
				}

				for (int j = 0; j < attributeList.size(); j++) {
					Attribute attribute = (Attribute) attributeList.get(j);
					if (StringUtils.isBlank(attribute.getText())) {
						continue;// 如果是空值则保留类声明里面的默认值
					}

					Field field = fieldList.get(attribute.getName());
					if (field == null) {
						LOGGER.warn(String.format("[%s]->[%s] column not exists in class!", clazz.getName(), attribute.getName()));
						continue;
					}
					
					Class<?> typeClass = field.getType();
					if (typeClass.getCanonicalName() == int.class.getCanonicalName()) {
						field.set(object, Integer.valueOf(attribute.getText()));
					} else if (typeClass.getCanonicalName() == long.class.getCanonicalName()) {
						field.set(object, Long.valueOf(attribute.getText()));
					} else if (typeClass.getCanonicalName() == float.class.getCanonicalName()) {
						field.set(object, Float.valueOf(attribute.getText()));
					} else if (typeClass.getCanonicalName() == boolean.class.getCanonicalName()) {
						field.set(object, "0".equals(attribute.getText()) ? false : true);
					} else {
						field.set(object, String.valueOf(attribute.getText()));
					}
				}
				objList.add(object);
			}
			
		} catch (Exception e) {
			LOGGER.error(String.format("loading [%s] class error!", clazz.getName()), e);
		}
		
		return objList;
	}
	

	
	private static Map<String, Field> getFieldList(Class<?> clazz) {
		Map<String, Field> fieldList = new HashMap<>();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			if (field.getName().equals("serialVersionUID") || field.isAnnotationPresent(FieldIgnore.class) == false) {
				fieldList.put(field.getName(), field);
			}
		}
		return fieldList;
	}
	
	private static void checkField(String className, Map<String, Field> fieldList, List<?> elementList) {
		if(elementList.size() < 1) {
			LOGGER.error(String.format("data config [%s] file 0 row record!", className));
			return;
		}
		
		Element subElement = (Element) elementList.get(0);
		List<?> attributeList = subElement.attributes();

		Set<String> attributeNameList = new HashSet<>();
		for (int j = 0; j < attributeList.size(); j++) {
			Attribute attribute = (Attribute) attributeList.get(j);
			attributeNameList.add(attribute.getName());
		}

		for (String fieldName : fieldList.keySet()) {
			if (attributeNameList.contains(fieldName) == false) {
				LOGGER.error(String.format("class [%s]->[%s] not exists in data config file.", className, fieldName));
			}
		}
	}
	
	private static <T> T newInstance(Class<T> cls) {
		try {
			return cls.newInstance();
		} catch (InstantiationException e) {
			LOGGER.error("",e);
		} catch (IllegalAccessException e) {
			LOGGER.error("",e);
		}
		return null;
	}
}
