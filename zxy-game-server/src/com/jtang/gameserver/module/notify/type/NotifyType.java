package com.jtang.gameserver.module.notify.type;
/**
 * 信息的类型
 * @author pengzy
 *
 */
public enum NotifyType {
	
	NONE(-1),
	
	/**
	 * 切磋
	 */
	ALLY_FIGHT(1),
	
	/**
	 * 试炼洞
	 */
	TRIAL(2),
	
	/**
	 * 抢夺
	 */
	SNATCH(3),
	
	/**
	 * 抢夺通知盟友
	 */
	SNATCH_ALLY(4),
	
	/**
	 * 合作关卡
	 */
	STORY(5),
	
	/**
	 * 添加盟友
	 */
	ADD_ALLY(6),
	
	/**
	 * 删除盟友
	 */
	REMOVE_ALLY(7),
	
	/**
	 * 势力排行挑战
	 */
	POWER_RANK_CHALLENGE(8),
	
	/**
	 * 发现新洞府邀请盟友
	 */
	HOLE_INVITE_ALLY(9),
	
//	/**
//	 * 在通知收件箱领取（ 合作关卡，试炼洞，送礼）时，感谢对方
//	 */
//	THANK_ALLY(10),
	
//	/**
//	 * 送礼通知
//	 */
//	GIVE_GIFT(11),
	
	/**
	 * 抢夺报仇通知
	 */
	SNATCH_REVENGE(12),
	
//	/**
//	 * 最强势力奖励通知
//	 */
//	POWER_REWARD(13),
	
	/**
	 * vip赠送装备
	 */
	VIP_GIVE_EQUIP(14);
	
//	/**
//	 * 集众降魔奖励
//	 */
//	DEMON_REWARD(15);
	
	private byte code;
	
	private NotifyType(int code){
		this.code = (byte)code;
	}
	
	public byte getCode(){
		return code;
	}
	
	public static NotifyType get(int code){
		for(NotifyType type : NotifyType.values()){
			if(type.getCode() == code){
				return type;
			}
		}
		return NONE;
	}	
}
