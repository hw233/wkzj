package com.jtang.gameserver.module.hero.helper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jiatang.common.model.BufferVO;
import com.jiatang.common.model.HeroVO;
import com.jiatang.common.model.HeroVOAttributeKey;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.ObjectReference;
import com.jtang.core.utility.CollectionUtils;
import com.jtang.gameserver.module.hero.handler.HeroCmd;
import com.jtang.gameserver.module.hero.handler.response.HeroListResponse;
import com.jtang.gameserver.module.hero.handler.response.HeroRemoveListResponse;
import com.jtang.gameserver.module.hero.handler.response.HeroSoulListResponse;
import com.jtang.gameserver.module.hero.handler.response.UpdateHeroResponse;
import com.jtang.gameserver.module.hero.handler.response.UpdateHerosBuffResponse;
import com.jtang.gameserver.module.hero.model.HeroBufferVO;
import com.jtang.gameserver.module.hero.model.HeroSoulVO;
import com.jtang.gameserver.server.session.PlayerSession;

/**
 * 仙人信息推送
 * @author vinceruan
 *
 */
@Component
public class HeroPushHelper {
	
	@Autowired
	PlayerSession playerSession;
	
	private static ObjectReference<HeroPushHelper> ref = new ObjectReference<HeroPushHelper>();
	@PostConstruct
	protected void init() {
		ref.set(this);
	}
	
	private static HeroPushHelper getInstance() {
		return ref.get();
	}
		
	/**
	 * 推送信息添加仙人
	 * @param actorId
	 * @param hero
	 */
	public static void pushAddHero(long actorId, HeroVO hero, int composeNum) {
		HeroListResponse heroResponse = HeroListResponse.valueOf(hero, new ArrayList<BufferVO>(), composeNum);
		Response rsp = Response.valueOf(ModuleName.HERO, HeroCmd.PUSH_ADD_HERO, heroResponse.getBytes());
		getInstance().playerSession.push(actorId, rsp);
	}
	
	/**
	 * 推送多个新添加仙人
	 * @param actorId
	 * @param hero
	 */
	public static void pushAddheroList(long actorId, List<HeroVO> heroList, int composeNum) {
		HeroListResponse heroResponse = HeroListResponse.valueOfList(heroList, composeNum);
		Response rsp = Response.valueOf(ModuleName.HERO, HeroCmd.PUSH_ADD_HERO, heroResponse.getBytes());
		getInstance().playerSession.push(actorId, rsp);
	}
	
	
	/**
	 * 推送仙人属性变更
	 * @param actorId
	 * @param heroId
	 * @param attrs
	 */
	public static void pushUpdateHero(long actorId, int heroId, Set<HeroVOAttributeKey> attrs) {
		if (CollectionUtils.isEmpty(attrs)) {
			return;
		}
		
		Map<HeroVOAttributeKey, Object> val = HeroHelper.getInstance().getHeroAttributeVals(actorId, heroId, attrs);
		UpdateHeroResponse response = new UpdateHeroResponse(heroId, val);
		Response rsp = Response.valueOf(ModuleName.HERO, HeroCmd.PUSH_UPDATE_HERO, response.getBytes());
		getInstance().playerSession.push(actorId, rsp);
	}
	
	/**
	 * 推送仙人的属性变动
	 * @param actorId
	 * @param heroId
	 * @param key
	 */
	public static void pushUpdateHero(long actorId, int heroId, HeroVOAttributeKey key) {		
		Set<HeroVOAttributeKey> attrs = new HashSet<>();
		attrs.add(key);
		pushUpdateHero(actorId, heroId, attrs);
	}
	
	/**
	 * 推送仙人魂魄信息
	 * @param actorId
	 * @param heroSoulId
	 * @param count
	 */
	public static void pushHeroSoul(long actorId, int heroSoulId, int count) {
		HeroSoulListResponse soulResponse = HeroSoulListResponse.valueOf(HeroSoulVO.valueOf(heroSoulId, count));
		Response rsp = Response.valueOf(ModuleName.HERO, HeroCmd.PUSH_HERO_SOUL, soulResponse.getBytes());
		getInstance().playerSession.push(actorId, rsp);
	}
	
	/**
	 * 推送仙人属性
	 * @param actorId
	 * @param heroIds
	 */
	public static void pushHeroBuffers(long actorId, Map<Integer, List<BufferVO>> heroBuffVOs) {		
		List<HeroBufferVO> heroBuffs = new ArrayList<>();
		for (int heroId : heroBuffVOs.keySet()) {
			HeroBufferVO vo = new HeroBufferVO();
			vo.heroId = heroId;
			vo.buffList = heroBuffVOs.get(heroId);
			heroBuffs.add(vo);
		}
		UpdateHerosBuffResponse res = new UpdateHerosBuffResponse(heroBuffs);
		Response response = Response.valueOf(ModuleName.HERO, HeroCmd.PUSH_HERO_BUFFER, res.getBytes());
		getInstance().playerSession.push(actorId, response);
	}
	
	/**
	 * 推送仙人属性
	 * @param actorId
	 * @param heroIds
	 */
	public static void pushHeroBuffers(long actorId, int heroId, List<BufferVO> heroBuffVOs) {
		List<HeroBufferVO> heroBuffs = new ArrayList<>();
		HeroBufferVO vo = new HeroBufferVO();
		vo.heroId = heroId;
		vo.buffList = heroBuffVOs;
		heroBuffs.add(vo);

		UpdateHerosBuffResponse packet = new UpdateHerosBuffResponse(heroBuffs);
		Response response = Response.valueOf(ModuleName.HERO, HeroCmd.PUSH_HERO_BUFFER, packet.getBytes());
		getInstance().playerSession.push(actorId, response);
	}
	
	/**
	 * 推送删除的仙人
	 * @param actorId
	 * @param heroIdList
	 */
	public static void pushHeroRemove(long actorId, List<Integer> heroIdList) {
		HeroRemoveListResponse packet = HeroRemoveListResponse.valueOf(heroIdList);
		Response response = Response.valueOf(ModuleName.HERO, HeroCmd.PUSH_HERO_REMOVE, packet.getBytes());
		getInstance().playerSession.push(actorId, response);
	}
	
}
