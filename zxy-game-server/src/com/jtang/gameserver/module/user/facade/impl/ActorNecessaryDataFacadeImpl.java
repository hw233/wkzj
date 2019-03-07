package com.jtang.gameserver.module.user.facade.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.model.BufferVO;
import com.jiatang.common.model.EquipVO;
import com.jiatang.common.model.HeroVO;
import com.jtang.core.result.Result;
import com.jtang.core.result.TResult;
import com.jtang.core.utility.DateUtils;
import com.jtang.gameserver.dataconfig.service.AppRuleService;
import com.jtang.gameserver.dataconfig.service.CraftsmanService;
import com.jtang.gameserver.dataconfig.service.DemonGlobalService;
import com.jtang.gameserver.dataconfig.service.TreasureService;
import com.jtang.gameserver.dbproxy.entity.Gift;
import com.jtang.gameserver.dbproxy.entity.Lineup;
import com.jtang.gameserver.dbproxy.entity.Message;
import com.jtang.gameserver.dbproxy.entity.Snatch;
import com.jtang.gameserver.dbproxy.entity.Stories;
import com.jtang.gameserver.dbproxy.entity.TrialCave;
import com.jtang.gameserver.module.adventures.achievement.facade.AchieveFacade;
import com.jtang.gameserver.module.adventures.achievement.handler.response.AchieveListResponse;
import com.jtang.gameserver.module.adventures.achievement.model.AchieveVO;
import com.jtang.gameserver.module.adventures.bable.facade.BableFacade;
import com.jtang.gameserver.module.adventures.bable.handler.response.BableDataResponse;
import com.jtang.gameserver.module.adventures.shop.shop.facade.BlackShopFacade;
import com.jtang.gameserver.module.adventures.shop.trader.facade.TraderFacade;
import com.jtang.gameserver.module.adventures.shop.trader.handler.response.ShopOpenResponse;
import com.jtang.gameserver.module.adventures.shop.vipshop.facade.VipShopFacade;
import com.jtang.gameserver.module.adventures.shop.vipshop.handler.response.VipShopResponse;
import com.jtang.gameserver.module.adventures.vipactivity.facade.MainHeroFacade;
import com.jtang.gameserver.module.adventures.vipactivity.handler.response.MainHeroResponse;
import com.jtang.gameserver.module.adventures.vipactivity.handler.response.VipActivityInfoResponse;
import com.jtang.gameserver.module.ally.facade.AllyFacade;
import com.jtang.gameserver.module.ally.handler.response.AllyListResponse;
import com.jtang.gameserver.module.ally.model.AllyVO;
import com.jtang.gameserver.module.app.facade.AppFacade;
import com.jtang.gameserver.module.app.handler.response.GetAppGlobalResponse;
import com.jtang.gameserver.module.app.handler.response.GetAppRecordResponse;
import com.jtang.gameserver.module.app.model.AppGlobalVO;
import com.jtang.gameserver.module.app.model.AppRecordVO;
import com.jtang.gameserver.module.buffer.facade.BufferFacade;
import com.jtang.gameserver.module.buffer.model.HeroBuffer;
import com.jtang.gameserver.module.chat.facade.ChatFacade;
import com.jtang.gameserver.module.chat.handler.response.ChatResponse;
import com.jtang.gameserver.module.chat.handler.response.HistoryChatResponse;
import com.jtang.gameserver.module.dailytask.facade.DailyTaskFacade;
import com.jtang.gameserver.module.dailytask.handler.response.DailyTaskInfoResponse;
import com.jtang.gameserver.module.demon.facade.DemonFacade;
import com.jtang.gameserver.module.demon.handler.response.DemonTimeResponse;
import com.jtang.gameserver.module.equip.facade.EquipFacade;
import com.jtang.gameserver.module.equip.handler.response.EquipListResponse;
import com.jtang.gameserver.module.extapp.basin.facade.BasinFacade;
import com.jtang.gameserver.module.extapp.basin.handler.response.BasinStateResponse;
import com.jtang.gameserver.module.extapp.beast.facade.BeastFacade;
import com.jtang.gameserver.module.extapp.beast.handler.response.BeastStatusResponse;
import com.jtang.gameserver.module.extapp.craftsman.facade.CraftsmanFacade;
import com.jtang.gameserver.module.extapp.craftsman.handler.response.CraftsmanStatusResponse;
import com.jtang.gameserver.module.extapp.deitydesc.facade.DeityDescendFacade;
import com.jtang.gameserver.module.extapp.deitydesc.handler.response.DeityDescendStatusResponse;
import com.jtang.gameserver.module.extapp.ernie.facade.ErnieFacade;
import com.jtang.gameserver.module.extapp.ernie.handler.response.ErnieStatusResponse;
import com.jtang.gameserver.module.extapp.invite.facade.InviteFacade;
import com.jtang.gameserver.module.extapp.invite.handler.response.InviteResponse;
import com.jtang.gameserver.module.extapp.monthcard.facade.MonthCardFacade;
import com.jtang.gameserver.module.extapp.monthcard.handler.response.MonthCardResponse;
import com.jtang.gameserver.module.extapp.onlinegifts.facade.OnlineGiftsFacade;
import com.jtang.gameserver.module.extapp.onlinegifts.handler.response.OnlineGiftsInfoResponse;
import com.jtang.gameserver.module.extapp.questions.facade.QuestionsFacade;
import com.jtang.gameserver.module.extapp.questions.handler.response.AnswerStateResponse;
import com.jtang.gameserver.module.extapp.randomreward.constant.RandomRewardRule;
import com.jtang.gameserver.module.extapp.randomreward.facade.RandomRewardFacade;
import com.jtang.gameserver.module.extapp.randomreward.handler.response.RandomRewardResponse;
import com.jtang.gameserver.module.extapp.rechargeapp.facade.RechargeAppFacade;
import com.jtang.gameserver.module.extapp.vipbox.facade.VipBoxFacade;
import com.jtang.gameserver.module.extapp.vipbox.handler.response.VipBoxConfigResponse;
import com.jtang.gameserver.module.extapp.vipbox.handler.response.VipBoxResponse;
import com.jtang.gameserver.module.gift.facade.GiftFacade;
import com.jtang.gameserver.module.gift.handler.response.GiftInfoResponse;
import com.jtang.gameserver.module.goods.facade.GoodsFacade;
import com.jtang.gameserver.module.goods.handler.response.GoodsListResponse;
import com.jtang.gameserver.module.hero.facade.HeroFacade;
import com.jtang.gameserver.module.hero.facade.HeroSoulFacade;
import com.jtang.gameserver.module.hero.handler.response.HeroListResponse;
import com.jtang.gameserver.module.hero.handler.response.HeroResponse;
import com.jtang.gameserver.module.hero.handler.response.HeroSoulListResponse;
import com.jtang.gameserver.module.hole.facade.HoleFacade;
import com.jtang.gameserver.module.hole.handler.response.HoleResponse;
import com.jtang.gameserver.module.icon.facade.IconFacade;
import com.jtang.gameserver.module.icon.hander.response.IconResponse;
import com.jtang.gameserver.module.lineup.facade.LineupFacade;
import com.jtang.gameserver.module.lineup.handler.response.LineupInfoResponse;
import com.jtang.gameserver.module.love.facade.LoveFacade;
import com.jtang.gameserver.module.love.handler.response.LoveInfoResponse;
import com.jtang.gameserver.module.msg.facade.MsgFacade;
import com.jtang.gameserver.module.msg.handler.response.MsgListResponse;
import com.jtang.gameserver.module.notify.facade.NotifyFacade;
import com.jtang.gameserver.module.notify.handler.response.NotifyListResponse;
import com.jtang.gameserver.module.recruit.facade.RecruitFacade;
import com.jtang.gameserver.module.recruit.handler.response.GetInfoResponse;
import com.jtang.gameserver.module.sign.facade.SignFacade;
import com.jtang.gameserver.module.sign.handler.response.SignInfoResponse;
import com.jtang.gameserver.module.snatch.facade.SnatchEnemyFacade;
import com.jtang.gameserver.module.snatch.facade.SnatchFacade;
import com.jtang.gameserver.module.snatch.handler.response.SnatchInfoResponse;
import com.jtang.gameserver.module.sprintgift.facade.SprintGiftFacade;
import com.jtang.gameserver.module.sprintgift.handler.response.SprintGiftStatusListResponse;
import com.jtang.gameserver.module.story.facade.StoryFacade;
import com.jtang.gameserver.module.story.handler.response.StoryResponse;
import com.jtang.gameserver.module.sysmail.facade.SysmailFacade;
import com.jtang.gameserver.module.sysmail.handler.response.SysmailListResponse;
import com.jtang.gameserver.module.treasure.facade.TreasureFacade;
import com.jtang.gameserver.module.treasure.handler.response.TreasureGoodsResponse;
import com.jtang.gameserver.module.treasure.handler.response.TreasureResponse;
import com.jtang.gameserver.module.treasure.model.TreasureVO;
import com.jtang.gameserver.module.trialcave.facade.TrialCaveFacade;
import com.jtang.gameserver.module.trialcave.handler.response.TrialCaveInfoResponse;
import com.jtang.gameserver.module.user.facade.ActorFacade;
import com.jtang.gameserver.module.user.facade.ActorNecessaryDataFacade;
import com.jtang.gameserver.module.user.handler.response.ActorBuyResponse;
import com.jtang.gameserver.module.user.handler.response.ActorNecessaryDataResponse;
import com.jtang.gameserver.module.user.helper.ActorHelper;

@Component
public class ActorNecessaryDataFacadeImpl implements ActorNecessaryDataFacade {

	@Autowired
	private RecruitFacade recruitFacade;
	@Autowired
	private StoryFacade storyFacade;
	@Autowired
	private HeroFacade heroFacade;
	@Autowired
	private HeroSoulFacade heroSoulFacade;
	@Autowired
	private BufferFacade bufferFacade;
	@Autowired
	private LineupFacade lineupFacade;
	@Autowired
	private SnatchEnemyFacade snatchEnemyFacade;
	@Autowired
	private EquipFacade equipFacade;
	@Autowired
	private SnatchFacade snatchFacade;
	@Autowired
	private AllyFacade allyFacade;
	@Autowired
	private GiftFacade giftFacade;
	@Autowired
	private GoodsFacade goodsFacade;
	@Autowired
	private MsgFacade msgFacade;
	@Autowired
	private NotifyFacade notifyFacade;
	@Autowired
	private SysmailFacade sysmailFacade;
	@Autowired
	private AchieveFacade achievementFacade;
	@Autowired
	private TrialCaveFacade trialCaveFacade;
	@Autowired
	private BableFacade bableFacade;
	@Autowired
	private SignFacade signFacade;
	@Autowired
	private DailyTaskFacade dailyTaskFacade;
	@Autowired
	private MonthCardFacade monthCardFacade;
	@Autowired
	private SprintGiftFacade sprintGiftFacade;
	@Autowired
	private ActorFacade actorFacade;
	@Autowired
	private MainHeroFacade mainHeroFacade;
	@Autowired
	private BlackShopFacade blackShopFacade;
	@Autowired
	private RechargeAppFacade rechargeAppFacade;
	@Autowired
	private AppFacade appFacade;
	@Autowired
	private IconFacade iconFacade;
	@Autowired
	private TraderFacade traderFacade;
	@Autowired
	private QuestionsFacade questionsFacade;
	@Autowired
	private BasinFacade basinFacade;
	@Autowired
	private DeityDescendFacade deityDescendFacade;
	@Autowired
	private LoveFacade loveFacde;
	@Autowired
	private ChatFacade chatFacade;
	@Autowired
	private VipShopFacade vipShopFacade;
	@Autowired
	private TreasureFacade treasureFacade;
	@Autowired
	private InviteFacade inviteFacade;
	@Autowired
	private DemonFacade demonFacade;
	@Autowired
	private RandomRewardFacade randomRewardFacade;
	@Autowired
	private CraftsmanFacade craftsmanFacade;
	@Autowired
	private OnlineGiftsFacade onlineGiftsFacade;
	@Autowired
	private ErnieFacade ernieFacade;
	@Autowired
	private BeastFacade beastFacade;
	@Autowired
	private HoleFacade holeFacade;
	@Autowired
	private VipBoxFacade vipBoxFacade;

	@Override
	public ActorNecessaryDataResponse get(long actorId) {
		GetInfoResponse getRecruitInfoResponse = recruitFacade.getInfo(actorId);
		Stories story = storyFacade.get(actorId);
		StoryResponse storyResponse = new StoryResponse(story);
		Collection<HeroVO> list = heroFacade.getList(actorId);

		List<HeroResponse> resList = new ArrayList<>();
		for (HeroVO hero : list) {
			List<BufferVO> heroBufferList = new ArrayList<>();
			HeroBuffer buf = bufferFacade.getHeroBuffer(actorId, hero.getHeroId());
			if (buf != null) {
				for (List<BufferVO> lt : buf.bufferTypeMap.values()) {
					heroBufferList.addAll(lt);
				}
			}
			resList.add(HeroResponse.valueOf(hero, heroBufferList));
		}
		int composeNum = heroFacade.getComposeNum(actorId);
		HeroListResponse heroListResponse = HeroListResponse.valueOf(resList, composeNum);
		HeroSoulListResponse heroSoulListResponse = HeroSoulListResponse.valueOf(heroSoulFacade.getList(actorId));
	
		Lineup lineup = lineupFacade.getLineup(actorId);
		LineupInfoResponse lineupInfoResponse = new LineupInfoResponse(lineup);

		Collection<EquipVO> equipList = equipFacade.getList(actorId);
		int num = equipFacade.getComposeNum(actorId);
		EquipListResponse equipListResponse = new EquipListResponse(equipList, num);
		Snatch snatch = snatchFacade.get(actorId);
		SnatchInfoResponse snatchInfoResponse = new SnatchInfoResponse(snatch, snatchEnemyFacade.getEnemyList(actorId, false));

		Collection<AllyVO> allyVOList = allyFacade.getAlly(actorId);
		int countDownSeconds = allyFacade.getCountDown(actorId);
		int dayFightCount = allyFacade.getDayFightCount(actorId);
		AllyListResponse allyListResponse = new AllyListResponse(allyVOList, countDownSeconds, dayFightCount);

		Gift gift = this.giftFacade.get(actorId);
		GiftInfoResponse giftInfoResponse = new GiftInfoResponse(gift);
		GoodsListResponse goodsListResponse = GoodsListResponse.valueOf(goodsFacade.getList(actorId));

		List<Message> msgList = msgFacade.get(actorId);
		MsgListResponse msgListResponse = new MsgListResponse(msgList);

		NotifyListResponse notifyListResponse = new NotifyListResponse(notifyFacade.getList(actorId));

		SysmailListResponse sysmailListResponse = new SysmailListResponse(sysmailFacade.getList(actorId));

		List<AchieveVO> achievementList = achievementFacade.getAchieve(actorId);
		AchieveListResponse achieveListResponse = new AchieveListResponse(achievementList);
		
		TResult<BableDataResponse> result = bableFacade.getBableInfo(actorId, -1);
		
		TResult<SignInfoResponse> signInfoResponse = signFacade.getInfo(actorId);
		
		DailyTaskInfoResponse dailyTaskInfoResponse = dailyTaskFacade.getDailyTask(actorId);
		
		TResult<MonthCardResponse> monthCardResponse = monthCardFacade.getInfo(actorId);
		
		SprintGiftStatusListResponse sprintGiftStatusListResponse = new SprintGiftStatusListResponse();
		sprintGiftStatusListResponse.levelStatusMap = sprintGiftFacade.getSprintGiftStatusList(actorId);

		TResult<ActorBuyResponse> actorBuyResponse = actorFacade.getActorBuy(actorId);
		
		TResult<MainHeroResponse> mainHeroResponse = mainHeroFacade.getMainHeroInfo(actorId);
		

		List<AppGlobalVO> cfgs = new ArrayList<>();
		for (long id : AppRuleService.getAllApp()) {
			Result r = appFacade.appEnable(actorId, id);
			if (r.isFail()) {
				continue;
			}
			AppGlobalVO appGlobalVO = appFacade.getAppGlobalVO(actorId, id);
			if (appGlobalVO != null) {
				cfgs.add(appGlobalVO);
			}
		}
		GetAppGlobalResponse getAppGlobalResponse = new GetAppGlobalResponse(cfgs);

		List<AppRecordVO> listApp = new ArrayList<>();
		for (long id : AppRuleService.getAllApp()) {
			Result rr = appFacade.appEnable(actorId, id);
			if (rr.isFail()) {
				continue;
			}
			AppRecordVO appRecordVO = appFacade.getAppRecord(actorId, id);
			if (appRecordVO != null) {
				listApp.add(appRecordVO);
			}
		}

		GetAppRecordResponse getAppRecordResponse = new GetAppRecordResponse(listApp);

		TResult<IconResponse> iconResponse = iconFacade.getIconInfo(actorId);

		int heroComposeNum = heroFacade.getComposeNum(actorId);
		int equipComposeNum = equipFacade.getComposeNum(actorId);
		int heroResetNum = heroFacade.getResetNum(actorId);
		int equipResetNum = equipFacade.getResetNum(actorId);
		VipActivityInfoResponse vipActivityInfoResponse = new VipActivityInfoResponse(equipComposeNum, heroComposeNum, heroResetNum + equipResetNum);

		TResult<Map<Integer, Integer>> tradershop = traderFacade.getOpenInfo(actorId);
		ShopOpenResponse shopOpenResponse = new ShopOpenResponse(tradershop.item);
		TResult<AnswerStateResponse> answerStateResponse = questionsFacade.getState();
		TResult<BasinStateResponse> basinStateResponse = basinFacade.getState();

		Result r = deityDescendFacade.getStatus(actorId);
		byte status = r.isOk() ? (byte) 1 : (byte) 0;
		DeityDescendStatusResponse deityDescendStatusResponse = new DeityDescendStatusResponse(status);

		LoveInfoResponse loveInfoResponse = loveFacde.getLoveInfo(actorId);

		List<ChatResponse> historyMsg = chatFacade.getChat();
		HistoryChatResponse histChatResponse = new HistoryChatResponse(historyMsg);
		
		TResult<VipShopResponse> vipShopResponse = vipShopFacade.getInfo(actorId);
		
		TreasureGoodsResponse treasureGoodsResponse = new TreasureGoodsResponse(treasureFacade.exchangeGoods(actorId).item);
		
		Long openTime = CraftsmanService.CRAFTSMAN_GLOBAL_CONFIG.openDateTime.getTime() / 1000;
		Long endTime = CraftsmanService.CRAFTSMAN_GLOBAL_CONFIG.closeDateTime.getTime() / 1000;
		int time = 0;
		int buildNum = CraftsmanService.getCraftsmanGlobalConfig().buildCount;
		if (DateUtils.isActiveTime(openTime.intValue(), endTime.intValue())) {
			int now = DateUtils.getNowInSecondes();
			time = endTime.intValue() - now;
			buildNum = buildNum - craftsmanFacade.getBuildNum(actorId);
		}
		CraftsmanStatusResponse craftsmanStatusResponse = new CraftsmanStatusResponse(time, buildNum);
		Long demontime = DemonGlobalService.getOpenDate().getTime() / 1000;
		DemonTimeResponse demonTimeResponse = new DemonTimeResponse(demontime.intValue(), DemonGlobalService.getOpenTimes(),
				DemonGlobalService.getExchangeWeek(), DemonGlobalService.getExchangeTime());
		
		ErnieStatusResponse ernieStatusResponse = ernieFacade.getStatus().item;
		
		BeastStatusResponse beastStatusResponse = beastFacade.getStatus(actorId).item;
		
		HoleResponse holeResponse = holeFacade.getHoleResponse(actorId);
		
		VipBoxConfigResponse response = vipBoxFacade.getConfig().item;
		
		
		
		ActorNecessaryDataResponse actorNecessaryDataResponse = new ActorNecessaryDataResponse(getRecruitInfoResponse, storyResponse,
				heroListResponse, heroSoulListResponse, lineupInfoResponse, equipListResponse, snatchInfoResponse, allyListResponse,
				giftInfoResponse, goodsListResponse, msgListResponse, notifyListResponse, sysmailListResponse, achieveListResponse, result.item,
				signInfoResponse.item, dailyTaskInfoResponse, monthCardResponse.item, sprintGiftStatusListResponse, actorBuyResponse.item,
				mainHeroResponse.item, getAppGlobalResponse, getAppRecordResponse,
				iconResponse.item, vipActivityInfoResponse, shopOpenResponse, answerStateResponse.item, basinStateResponse.item,
				deityDescendStatusResponse, loveInfoResponse, histChatResponse, vipShopResponse.item, treasureGoodsResponse,
				craftsmanStatusResponse, demonTimeResponse, ernieStatusResponse,beastStatusResponse, holeResponse,response);
		
		TResult<TrialCave> trialCave = this.trialCaveFacade.getTrialCaveInfo(actorId);
		if (trialCave.isOk()) {
			TrialCaveInfoResponse res = new TrialCaveInfoResponse(trialCave.item);
			actorNecessaryDataResponse.setTrialCaveInfoResponse(res);
		}
		
		TResult<TreasureVO> treasureVO = treasureFacade.getTreasure(actorId);
		if (treasureVO.isOk()) {
			TreasureResponse treasureResponse = new TreasureResponse(treasureVO.item, TreasureService.getOpenTimes());
			actorNecessaryDataResponse.setTreasureResponse(treasureResponse);
		}
		
		TResult<InviteResponse> inviteResponse = inviteFacade.getInfo(actorId);
		if (inviteResponse.isOk()) {
			actorNecessaryDataResponse.setInviteResponse(inviteResponse.item);
		}
		
		int level = ActorHelper.getActorLevel(actorId);
		if(level >= RandomRewardRule.RANDOM_REWARD_OPEN_LEVEL){
			TResult<RandomRewardResponse> randomRewardResponse = randomRewardFacade.getInfo(actorId);
			actorNecessaryDataResponse.setRandomRewardResponse(randomRewardResponse.item);
		}
		
		TResult<OnlineGiftsInfoResponse> onlineResult= onlineGiftsFacade.getOnlineInfo(actorId);
		if (onlineResult.isOk()) {
			actorNecessaryDataResponse.setOnlineGiftsInfoResponse(onlineResult.item);
		}
		
		TResult<VipBoxResponse> vipBoxResult = vipBoxFacade.getInfo(actorId);
		if(vipBoxResult.isOk()){
			actorNecessaryDataResponse.setVipBoxResponse(vipBoxResult.item);
		}
		
		return actorNecessaryDataResponse;
	}

}
