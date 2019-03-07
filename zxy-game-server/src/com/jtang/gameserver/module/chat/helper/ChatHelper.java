package com.jtang.gameserver.module.chat.helper;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.core.model.RewardObject;
import com.jtang.core.result.ObjectReference;
import com.jtang.gameserver.module.chat.handler.response.ActorChatResponse;
import com.jtang.gameserver.module.chat.handler.response.ChatResponse;
import com.jtang.gameserver.module.chat.handler.response.DemonChatResponse;
import com.jtang.gameserver.module.chat.handler.response.DemonWinResponse;
import com.jtang.gameserver.module.chat.handler.response.HeroBookChatResponse;
import com.jtang.gameserver.module.chat.handler.response.LadderChatResponse;
import com.jtang.gameserver.module.chat.handler.response.OpenBoxChatResponse;
import com.jtang.gameserver.module.chat.handler.response.PlantChatResponse;
import com.jtang.gameserver.module.chat.handler.response.PowerChatResponse;
import com.jtang.gameserver.module.chat.handler.response.SnatchChatResponse;
import com.jtang.gameserver.module.chat.handler.response.SystemChatResponse;
import com.jtang.gameserver.module.chat.handler.response.TreasureBigRewardResponse;
import com.jtang.gameserver.module.chat.handler.response.TreasureChatResponse;
import com.jtang.gameserver.module.chat.handler.response.WelkinChatResponse;
import com.jtang.gameserver.module.chat.type.MsgType;
import com.jtang.gameserver.module.goods.type.UseGoodsResult;
import com.jtang.gameserver.module.icon.facade.IconFacade;
import com.jtang.gameserver.module.icon.model.IconVO;

@Component
public class ChatHelper {
	@Autowired
	IconFacade iconFacade;
	
	private static ObjectReference<ChatHelper> ref = new ObjectReference<ChatHelper>();

	@PostConstruct
	protected void init() {
		ref.set(this);
	}
	
	private static ChatHelper getInstance() {
		return ref.get();
	}
	
	/**
	 * 获得抢夺消息对象
	 * 
	 * @param actorName 发起人名称
	 * @param actorId 发起人id
	 * @param level 发起人等级
	 * @param vipLevel 发起人vip等级
	 * @param targetName 目标名称
	 * @param targetLevel 目标等级
	 * @param targetVipLevel 目标vip等级
	 * @param num 抢夺积分或金币数量
	 * @return
	 */
	public static ChatResponse getSnatchResponse(String actorName, long actorId, int level, int vipLevel, String otherActorName,
			int otherLevel, int otherVipLevel, int num, int isWin) {
		IconVO iconVO = getInstance().iconFacade.getIconVO(actorId);
		return new SnatchChatResponse(MsgType.SNATCH.getCode(), actorName, actorId, level, vipLevel, otherActorName, otherLevel,
				otherVipLevel, num, isWin,iconVO);
	}

	/**
	 * 获得装备或仙人系统消息
	 * 
	 * @param actorName 发起人名称
	 * @param actorId 发起人id
	 * @param level 发起人等级
	 * @param vipLevel 发起人vip等级
	 * @param type 仙人或装备类型
	 * @param itemId 装备或仙人id
	 * @param getType 获取类型
	 * @return
	 */
	public static ChatResponse getEquipHeroResponse(String actorName, long actorId, int level, int vipLevel, int type, int itemId, int getType) {
		IconVO iconVO = getInstance().iconFacade.getIconVO(actorId);
		return new SystemChatResponse(MsgType.SYSTEM.getCode(), actorName, actorId, level, vipLevel, type, itemId, getType,iconVO);
	}

	/**
	 * 获得世界聊天消息
	 * 
	 * @param senderName 发送者名称
	 * @param actorId 发送者id
	 * @param senderLevel 发送者等级
	 * @param msg 消息
	 * @param vipLevel 发送者vip等级
	 * @return
	 */
	public static ChatResponse getActorMsgResponse(String senderName, long actorId, int senderLevel, String msg, int vipLevel) {
		IconVO iconVO = getInstance().iconFacade.getIconVO(actorId);
		return new ActorChatResponse(MsgType.ACTOR.getCode(), senderName, actorId, senderLevel, vipLevel, msg,iconVO);
	}

	/**
	 * 获得最强势力排行榜消息
	 * 
	 * @param actorName 发起人名称
	 * @param actorId 发起人id
	 * @param level 发起人等级
	 * @param vipLevel 发起人vip等级
	 * @param targetLevel 目标等级
	 * @param targetVipLevel 目标vip等级
	 * @param targetName 目标名称
	 * @param isFirst 是否第一名
	 * @return
	 */
	public static ChatResponse getPowerResponse(String actorName, long actorId, int level, int vipLevel, int isWin, int targetLevel,
			int targetVipLevel, String targetName, int isFirst) {
		IconVO iconVO = getInstance().iconFacade.getIconVO(actorId);
		return new PowerChatResponse(MsgType.POWER.getCode(), actorName, actorId, level, vipLevel, isWin, targetName, targetLevel, targetVipLevel,isFirst,iconVO);
	}

	/**
	 * 获得开宝箱消息
	 * @param actorName 发起人名称
	 * @param actorId 发起人id
	 * @param level 发起人等级
	 * @param vipLevel 发起人vip等级
	 * @param boxId 开启的宝箱id
	 * @param results 宝箱的内容列表
	 * @return
	 */
	public static ChatResponse getOpenBoxResponse(String actorName, long actorId, int level, int vipLevel, int boxId, List<UseGoodsResult> results) {
		IconVO iconVO = getInstance().iconFacade.getIconVO(actorId);
		return new OpenBoxChatResponse(MsgType.BOX.getCode(), actorName, actorId, level, vipLevel, boxId, results,iconVO);
	}
	
	
	/**
	 * 获得集众降魔消息
	 * @param actorName 发起人名称
	 * @param actorId 发起人id
	 * @param level 发起人等级
	 * @param vipLevel 发起人vip等级
	 * @param boosName  boos名字
	 * @param reward 奖励列表
	 * @return
	 */
	public static ChatResponse getDemonChatResponse(String actorName,long actorId,int level,int vipLevel,String boosName,List<RewardObject> reward){
		IconVO iconVO = getInstance().iconFacade.getIconVO(actorId);
		return new DemonChatResponse(MsgType.DEMON.getCode(), actorName, actorId, level, vipLevel,boosName, reward,iconVO);
	}

	/**
	 * 获得集众降魔胜利方消息
	 * @param actorName 第一名名称
	 * @param actorId 第一名id
	 * @param level 第一名等级
	 * @param vipLevel 第一名vip等级
	 * @param firstDemonReward 阵营所有人获得奖励
	 * @param winCampReward 第一名获得的奖励
	 * @return
	 */
	public static ChatResponse getDemonWinResponse(String actorName, long actorId, int level, int vipLevel, List<RewardObject> firstDemonReward,
			List<RewardObject> winCampReward) {
		IconVO iconVO = getInstance().iconFacade.getIconVO(actorId);
		return new DemonWinResponse(MsgType.DEMON_WIN.getCode(),actorName,actorId,level,vipLevel,firstDemonReward,winCampReward,iconVO);
	}

	/**
	 * 获得迷宫寻宝大奖消息
	 * @param actorName
	 * @param actorId
	 * @param level
	 * @param vipLevel
	 * @param rewardObject
	 * @return
	 */
	public static ChatResponse getTreasureResponse(String actorName, long actorId, int level, int vipLevel, RewardObject rewardObject) {
		IconVO iconVO = getInstance().iconFacade.getIconVO(actorId);
		return new TreasureBigRewardResponse(MsgType.MAZE_TREASURE.getCode(),actorName,actorId,level,vipLevel,rewardObject,iconVO);
	}
	
	/**
	 * 获得仙人图鉴消息
	 */
	public static ChatResponse getHeroBookResponse(String actorName, long actorId, int level, int vipLevel,int num,int heroStar,List<RewardObject> rewardObject){
		IconVO iconVO = getInstance().iconFacade.getIconVO(actorId);
		return new HeroBookChatResponse(MsgType.HERO_BOOK.getCode(),actorName,actorId,level,vipLevel,num,heroStar,rewardObject,iconVO);
	}
	
	/**
	 * 获得天财地宝消息
	 * @param actorName
	 * @param actorId
	 * @param level
	 * @param vipLevel
	 * @param targetId
	 * @param targetVipLevel
	 * @param targetName
	 * @param targetLevel
	 * @param type
	 * @param equipId
	 * @return
	 */
	public static ChatResponse getTreasureChatResponse(String actorName,long actorId,int level,int vipLevel,long targetId, int targetVipLevel,String targetName, int targetLevel, int type, int equipId,int limitLevel){
		IconVO iconVO = getInstance().iconFacade.getIconVO(actorId);
		return new TreasureChatResponse(MsgType.TREASURE.getCode(), actorName, actorId, level, vipLevel, targetId, targetVipLevel, targetName, targetLevel, type, equipId, limitLevel,iconVO);
	}
	
	/**
	 * 获取种植消息
	 * @param actorName
	 * @param actorId
	 * @param level
	 * @param vipLevel
	 * @param plantId
	 * @param rewardObject
	 * @return
	 */
	public static ChatResponse getPlantChatResponse(String actorName,long actorId,int level,int vipLevel,int plantId,RewardObject rewardObject){
		IconVO iconVO = getInstance().iconFacade.getIconVO(actorId);
		return new PlantChatResponse(MsgType.PLANT.getCode(),actorName,actorId,level,vipLevel,plantId,rewardObject,iconVO);
	}
	
	/**
	 * 获取天宫寻宝消息
	 * @param actorName
	 * @param actorId
	 * @param level
	 * @param vipLevel
	 * @param type
	 * @param list
	 * @return
	 */
	public static ChatResponse getWelkinChatResponse(String actorName,long actorId,int level,int vipLevel,int type,List<RewardObject> list){
		IconVO iconVO = getInstance().iconFacade.getIconVO(actorId);
		return new WelkinChatResponse(MsgType.WELKIN.getCode(),actorName,actorId,level,vipLevel,type,list,iconVO);
	}

	/**
	 * 获取天梯消息
	 * @param actorName
	 * @param actorId
	 * @param level
	 * @param vipLevel
	 * @param type
	 * @return
	 */
	public static ChatResponse getLadderChatResponse(String actorName,
			long actorId, int level, int vipLevel, Integer type,int winNum) {
		IconVO iconVO = getInstance().iconFacade.getIconVO(actorId);
		return new LadderChatResponse(MsgType.LADDER.getCode(),actorName,actorId,level,vipLevel,type,winNum,iconVO);
	}
}
