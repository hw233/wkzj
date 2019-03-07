package com.jtang.core.db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.jtang.core.utility.StringUtils;

/**
 * 备份错误实体
 * @author ludd
 *
 */
@Component
public class ErrorEntityBackup {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ErrorEntityBackup.class);
	/**
	 * 备份路径
	 */
	@Autowired(required = false)
	@Qualifier("dbqueue.backup_path")
	private String backupPath = "backup" + File.separator;
	
	/**
	 * 后缀名
	 */
	@Autowired(required = false)
	@Qualifier("dbqueue.backup_extension")
	private String backupExtension = ".data";
	/**
	 * 写入备份
	 * @param entity
	 * @param fileName
	 */
	public void write(Entity<?> entity, String fileName) {
		if (entity == null || StringUtils.isBlank(fileName)) {
			return;
		}
		URL resource = getClass().getClassLoader().getResource(backupPath);
		if (resource == null) {
			resource = checkFolderExist();
		}
		String filePath = resource.getPath();
		StringBuffer sb = new StringBuffer();
		sb.append(filePath);
		sb.append(fileName);
		sb.append("-");
		sb.append(System.currentTimeMillis());
		sb.append(backupExtension);
		ObjectOutputStream oos = null;
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(sb.toString());
			oos = new ObjectOutputStream(fos);
			oos.writeObject(entity);
		} catch (Exception e) {
			LOGGER.error("{}", e);
		} finally {
			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
					LOGGER.error("{}", e);
				}
			}
			
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					LOGGER.error("{}", e);
				}
			}
		}
	}
	
	
	private URL checkFolderExist() {
		URL url = ClassLoader.getSystemResource("");
		File dir = new File(url.getPath() + backupPath);
		if (!dir.exists() && !dir.isDirectory()) {// 判断文件目录是否存在
			boolean isSuccess = dir.mkdirs();
			if (isSuccess) {
				LOGGER.info("create backup folder success...");
			} else {
				LOGGER.warn("create backup folder fail");
			}
		}
		return url;
	}
}
