package com.jtang.gameserver.module.notice.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.module.notice.type.NoticeType;
import com.jtang.gameserver.module.notice.type.NotickKey;

/**
 * 广播消息的结构
 * @author pengzy
 *
 */
public class NoticeVO extends IoBufferSerializer {
	
	/**
	 * 类型
	 */
	public int type;
	
	/**
	 * 可变参数列表
	 */
	public Map<String, String> paramMap = new HashMap<>();
	
	public static NoticeVO valueOf(NoticeType type, Object... params) {
		NoticeVO notice = new NoticeVO();
		notice.type = type.getCode();
		switch (type) {
		case SNATCH_SUCCESS:
			notice.paramMap.put(NotickKey.fromActorId, String.valueOf(params[0]));
			notice.paramMap.put(NotickKey.fromActorName, (String) params[1]);
			notice.paramMap.put(NotickKey.toActorId, String.valueOf(params[2]));
			notice.paramMap.put(NotickKey.toActorName, (String) params[3]);
			break;
		case POWER_RANK:// 最强势力排行：“最强势力第X名：[玩家名X]，今天得到[仙人名X]魂魄X个”
			notice.paramMap.put(NotickKey.rank, String.valueOf(params[0]));
			notice.paramMap.put(NotickKey.actorName, (String) params[1]);
			notice.paramMap.put(NotickKey.heroName, (String) params[2]);
			notice.paramMap.put(NotickKey.num, String.valueOf(params[3]));
			break;
		case SYSTEM_NOTICE:// 系统公告
			notice.paramMap.put(NotickKey.msg, params[0].toString());
			break;
		case DEMON_NOTICE://集众降魔抢夺积分
			notice.paramMap.put(NotickKey.fromActorId, params[0].toString());
			notice.paramMap.put(NotickKey.fromActorName, params[1].toString());
			break;
		case CROSS_BATTLE:
			notice.paramMap.put(NotickKey.msg, params[0].toString());
			notice.paramMap.put(NotickKey.reward, params[1].toString());
			break;
		default:
			break;
		}
		return notice;
	}

	@Override
	public void write() {
		writeInt(type);
		writeShort((short) paramMap.size());
		for (Entry<String, String> entry : paramMap.entrySet()) {
			writeString(entry.getKey());
			writeString(entry.getValue());
		}
	}
}
