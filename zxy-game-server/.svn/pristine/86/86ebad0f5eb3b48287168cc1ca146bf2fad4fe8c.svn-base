package com.jtang.gameserver.component.lop.request;

import java.util.HashMap;
import java.util.Map;

import com.jtang.core.lop.IRequest;

/**
 * cdkey请求
 * @author 0x737263
 *
 */
public class CdkeyLOPRequest implements IRequest {

	/**
	 * 渠道ID
	 */
    public int channelId;

    /**
     * CDKey
     */
    public String cdKey;

    /**
     * 平台ID
     */
    public int platformId;

    /**
     * UID
     */
    public String uid;
    
    /**
     * 服务器ID
     */
    public int serverId;

    /**
     * 角色ID
     */
    public long actorId;

	@Override
	public String moduleName() {
		return "cdkey";
	}

	@Override
	public String invokeName() {
		return "use";
	}

	@Override
	public Map<String, String> getParameters() {
		Map<String, String> maps = new HashMap<>();
		maps.put("channelid", String.valueOf(this.channelId));
		maps.put("cdkey", this.cdKey);
		maps.put("platformid", String.valueOf(this.platformId));
		maps.put("uid", this.uid);
		maps.put("serverid", String.valueOf(this.serverId));
		maps.put("actorid", String.valueOf(this.actorId));
		return maps;
	}

}
