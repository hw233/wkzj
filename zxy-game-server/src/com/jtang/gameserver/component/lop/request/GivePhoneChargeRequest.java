package com.jtang.gameserver.component.lop.request;

import java.util.HashMap;
import java.util.Map;

import com.jtang.core.lop.IRequest;

/**
 * 赠送手机话费接口
 * @author 0x737263
 *
 */
public class GivePhoneChargeRequest implements IRequest {

	/**
	 * 渠道ID
	 */
    public int channelId;

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
    
    /**
     * 角色名
     */
    public String actorName;
    
    /**
     * 手机号码
     */
    public String mobilenum;
    
    /**
     * 活动id
     */
    public long actId;
    
    /**
     * 活动类型
     * 1.话费
     * 2.充值
     */
    public int type;
	
	@Override
	public String moduleName() {
		return "mobilepay";
	}

	@Override
	public String invokeName() {
		return "add";
	}

	@Override
	public Map<String, String> getParameters() {
		Map<String, String> maps = new HashMap<>();
		maps.put("channelid", String.valueOf(this.channelId));
		maps.put("platformid", String.valueOf(this.platformId));
		maps.put("uid", this.uid);
		maps.put("serverid", String.valueOf(this.serverId));
		maps.put("actorid", String.valueOf(this.actorId));
		maps.put("actorname", actorName);
		maps.put("mobilenum", mobilenum);
		maps.put("actid", String.valueOf(actId));
		maps.put("type", String.valueOf(type));
		return maps;
	}

}
