package com.jtang.gameserver.module.snatch.handler;

/**
 * 抢夺模块
 * @author vinceruan
 *
 */
public interface SnatchCmd {
	/**
	 * 获取抢夺基本信息，积分，排名，换对手
	 * <pre>
	 * 请求:{@code Request}
	 * 响应:{@code SnatchInfoResponse}
	 * </pre>
	 */
	byte SNATCH_BASE_INFO = 1;
	
	/**
	 * 开始抢夺
	 * <pre>
	 * 请求:{@code StartSnatchRequest}
	 * 响应:{@code StartSnatchResultResponse}
	 * </pre>
	 */
	byte START_SNATCH = 2;
	
	/**
	 * 切换抢夺敌人
	 * <pre>
	 * 请求:{@code request}
	 * 响应:{@code SnatchEnemysResponse}
	 * </pre>
	 */
	byte SNATCH_CHANGE_ENEMY = 3;
	
	/**
	 * 商店兑换物品请求
	 * <pre>
	 * 请求:{@code ExchangeRequest}
	 * 响应:{@code Response}
	 * </pre>
	 */
	byte EXCHANGE = 4;
	    
    /**
     * 推送抢夺的基本信息
     * <pre>
     * 	请求:null
     *  响应: {@code PushSnatchAttributeResponse}
     * </pre>
     */
    byte PUSH_SNATCH_ATTR = 5;
    
    /**
     * 推送成就列表
	 * <pre>
     * 	请求:null
     *  响应: {@code PushSnatchAttributeResponse}
     * </pre>
     */
    byte PUSH_ACHIMENTS = 6;
    
    /**
     * 获取兑换列表
     * 请求:{@code Request}
     * 返回:{@code ExchangeListResponse}
     * 推送:{@code ExchangeListResponse}
     */
    byte GET_EXCHANGE_LIST = 7;
    
    /**
     * 刷新兑换列表
     * 请求:{@code Request}
     * 返回:{@code ReflushExchangeResponse}
     */
    byte FLUSH_EXCHANGE = 8;
    
    /**
     * 购买刷新次数
     * 请求:{@code Request}
     * 返回:{@code SnatchNumResponse}
     */
    byte BUY_SNATCH_NUM = 9;
    
    /**
     * 推送购买信息
     * 推送:{@code SnatchNumResponse}
     */
    byte PUSH_BUY_INFO = 10;
    
    /**
     * 推送抢夺自动回复战斗次数
     * 推送:{@code SnatchFightNumResponse}
     */
    byte PUSH_FIGHT_NUM = 11;
    
}
