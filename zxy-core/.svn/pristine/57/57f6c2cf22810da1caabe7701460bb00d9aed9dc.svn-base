//package com.jtang.core.dataconfig.parse;
//
//import java.io.BufferedInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.codehaus.jackson.map.DeserializationConfig.Feature;
//import org.codehaus.jackson.map.ObjectMapper;
//import org.codehaus.jackson.map.type.TypeFactory;
//import org.codehaus.jackson.type.JavaType;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//import com.jtang.core.dataconfig.ModelAdapter;
//
///**
// * json格式的配置文件解析
// * 
// * @author 0x737263
// * 
// */
//@Component
//public class JSONDataParser implements DataParser {
//
//	private static final Logger logger = LoggerFactory.getLogger(JSONDataParser.class);
//	private static final ObjectMapper mapper = new ObjectMapper();
//	private static final TypeFactory typeFactory = TypeFactory.defaultInstance();
//
//	@Override
//	public <T extends ModelAdapter> List<T> parse(InputStream stream, Class<T> clazz) {
//		BufferedInputStream bufferedInputStream = new BufferedInputStream(stream);
//		try {
//			JavaType type = typeFactory.constructCollectionType(ArrayList.class, clazz);
//			mapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//
//			return mapper.readValue(stream, type);
//		} catch (Exception e) {
//			logger.error("JSONDataParser读取基础数据:[%s] 文件异常!", clazz, e);
//			throw new RuntimeException(e);
//		} finally {
//			try {
//				bufferedInputStream.close();
//				stream.close();
//			} catch (IOException e) {
//				logger.error("JSONDataParser读取基础数据:[%s] 关闭流异常!", clazz, e);
//			}
//		}
//	}
//}
