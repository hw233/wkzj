package com.jtang.gameserver.dataconfig.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.gameserver.dataconfig.model.PieceConfig;

/**
 * 碎片配置服务
 * @author liujian
 *
 */
@Component
public class PieceService extends ServiceAdapter {

	private static final Logger LOGGER = LoggerFactory.getLogger(PieceService.class);
	
	/**
	 *  碎片配置,格式是Map<pieceId, PieceConfig>
	 */
	private static Map<String, PieceConfig> PIECE_CFG_LIST = new HashMap<>(); 
	
	@Override
	public void clear() {
		PIECE_CFG_LIST.clear();
	}
	
	@Override
	public void initialize() {
		List<PieceConfig> list = dataConfig.listAll(this, PieceConfig.class);		
		for (PieceConfig piece : list) {
			PIECE_CFG_LIST.put(getKey(piece.type, piece.star), piece);
		}
	}
	
	public static String getKey(int type, int star){
		return type + "_" + star;
	}
	
	/**
	 * 获取碎片
	 * @param pieceId  碎片id
	 * @return
	 */
	public static PieceConfig get(int type, int star) {
		String pieceId = getKey(type, star);
		if (PIECE_CFG_LIST.containsKey(pieceId)) {
			return PIECE_CFG_LIST.get(pieceId);
		}
		if (LOGGER.isWarnEnabled()) {
			LOGGER.warn(String.format("PieceConfig缺少配置，pieceId: [%s]", pieceId));
		}
		return null;
	}

}
