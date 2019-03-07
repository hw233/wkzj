package com.jtang.gameserver.module.extapp.questions.facade.impl;

import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.jiatang.common.GameStatusCodeConstant;
import com.jtang.core.model.RewardObject;
import com.jtang.core.result.TResult;
import com.jtang.core.schedule.Schedule;
import com.jtang.core.utility.DateUtils;
import com.jtang.gameserver.component.listener.ActorLoginListener;
import com.jtang.gameserver.component.oss.GameOssLogger;
import com.jtang.gameserver.dataconfig.model.QuestionsConfig;
import com.jtang.gameserver.dataconfig.model.QuestionsPoolConfig;
import com.jtang.gameserver.dataconfig.model.QuestionsRewardConfig;
import com.jtang.gameserver.dataconfig.service.QuestionService;
import com.jtang.gameserver.dbproxy.entity.Actor;
import com.jtang.gameserver.dbproxy.entity.Questions;
import com.jtang.gameserver.module.demon.model.OpenTime;
import com.jtang.gameserver.module.extapp.questions.dao.QuestionsDao;
import com.jtang.gameserver.module.extapp.questions.facade.QuestionsFacade;
import com.jtang.gameserver.module.extapp.questions.handler.response.AnswerResponse;
import com.jtang.gameserver.module.extapp.questions.handler.response.AnswerStateResponse;
import com.jtang.gameserver.module.extapp.questions.handler.response.QuestionsRepsone;
import com.jtang.gameserver.module.extapp.questions.helper.QuestionsPushHelper;
import com.jtang.gameserver.module.goods.facade.GoodsFacade;
import com.jtang.gameserver.module.goods.type.GoodsAddType;
import com.jtang.gameserver.module.hero.facade.HeroSoulFacade;
import com.jtang.gameserver.module.hero.type.HeroSoulAddType;
import com.jtang.gameserver.module.user.facade.ActorFacade;
import com.jtang.gameserver.module.user.facade.VipFacade;
import com.jtang.gameserver.server.session.PlayerSession;

@Component
public class QuestionsFacadeImpl implements QuestionsFacade,ActorLoginListener,ApplicationListener<ContextRefreshedEvent> {
	
	@Autowired
	public QuestionsDao questionsDao;
	@Autowired
	private Schedule schedule;
	@Autowired
	private GoodsFacade goodsFacade;
	@Autowired
	private VipFacade vipFacade;
	@Autowired
	private PlayerSession playerSession;
	@Autowired
	private HeroSoulFacade heroSoulFacade;
	@Autowired
	private ActorFacade actorFacade;
	
	/**
	 * 是否开启
	 */
	private boolean isOpen = false;
	
	/**
	 * 正在使用的时间
	 */
	private OpenTime openTime;
	
	@Override
	public TResult<QuestionsRepsone> getInfo(long actorId) {
		if(isStart() == false){
			return TResult.valueOf(GameStatusCodeConstant.QUESTIONS_NOT_OPEN);
		}
		Questions questions = questionsDao.get(actorId);
		if(questions.recordMap.isEmpty() || DateUtils.isToday(questions.operationTime) == false){//初始化题目
			questions.recordMap = QuestionService.initQuestion();
			questions.useNum = 0;
			questionsDao.update(questions);
		}
		QuestionsRepsone response = new QuestionsRepsone(questions);
		return TResult.sucess(response);
	}

	@Override
	public TResult<AnswerResponse> answer(long actorId, int questionId, int option) {
		if(isStart() == false){
			return TResult.valueOf(GameStatusCodeConstant.QUESTIONS_NOT_OPEN);
		}
		AnswerResponse response = new AnswerResponse();
		QuestionsConfig globalConfig = QuestionService.getGlobalConfig();
		Questions questions = questionsDao.get(actorId);
		if(questions.useNum >= globalConfig.questionsNum){
			return TResult.valueOf(GameStatusCodeConstant.QUESTIONS_NOT_ANSWER_NUM);
		}
		if(questions.recordMap.containsKey(questionId) == false){
			return TResult.valueOf(GameStatusCodeConstant.QUESTIONS_NOT_EXSIT);
		}else{
			if(questions.recordMap.get(questionId) == 1){
				return TResult.valueOf(GameStatusCodeConstant.QUESTIONS_ANSWER);
			}
		}
		QuestionsPoolConfig questionConfig = QuestionService.getQuestionsConfig(questionId);
		if(questionConfig.answer != option){
			response.setState(0);
			questions.useNum += 1;
			questionsDao.update(questions);
			return TResult.sucess(response);
		}
		questions.useNum += 1;
		questions.recordMap.put(questionId, 1);
		int i = 0;
		for(Entry<Integer,Integer> entry:questions.recordMap.entrySet()){
			if(entry.getValue() == 1){
				i++;
			}
		}
		QuestionsRewardConfig config = QuestionService.getReward(i);
		if(config != null){
			sendReward(actorId, config.rewardList);
		}
		boolean isBigReward = false;
		for(Entry<Integer,Integer> entry:questions.recordMap.entrySet()){
			if(entry.getValue() == 1){
				isBigReward = true;
			}else{
				isBigReward = false;
				break;
			}
		}
		if(isBigReward){
			sendReward(actorId, globalConfig.rewardList);
		}
		questionsDao.update(questions);
		response.setState(1);
		Actor actor = actorFacade.getActor(actorId);
		GameOssLogger.answerQuestions(actor.uid, actor.platformType, actor.channelId, actor.serverId, actorId, questionId);
		return TResult.sucess(response);
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		schedule.addEverySecond(new Runnable() {
			@Override
			public void run() {
				if (isOpen == false && openTime == null) { // 未开放时才可开放
					List<OpenTime> list = QuestionService.getOpenTimes();
					for (OpenTime time : list) {// 取一个开放时间
						if (time.isStart()) {
							Set<Long> actorIds = playerSession.onlineActorList();
							for (Long actorId : actorIds) {
								// 推送活动开启状态
								QuestionsPushHelper.pushQuestionState(actorId, 1);
							}
							openTime = time;
							isOpen = true;
						}
					}
				}

				if (isOpen == true && openTime != null) {// 开放时才可关闭
					if (openTime.isStart() == false) {
						Set<Long> actorIds = playerSession.onlineActorList();
						for (Long actorId : actorIds) {
							// 推送活动关闭状态
							QuestionsPushHelper.pushQuestionState(actorId, 0);
						}
						openTime = null;
						isOpen = false;
					}
				}
			}
		}, 1);
	}

	@Override
	public void onLogin(long actorId) {
		Questions questions = questionsDao.get(actorId);
		if(questions.recordMap.isEmpty() || DateUtils.isToday(questions.operationTime) == false){//初始化题目
			questions.recordMap = QuestionService.initQuestion();
			questions.useNum = 0;
			questionsDao.update(questions);
		}
	}
	
	/**
	 * 问答活动是否已经开启
	 * 
	 * @return
	 */
	private boolean isStart() {
		if (isOpen && openTime != null && openTime.isStart()) {
			return true;
		}
		return false;
	}

	@Override
	public TResult<AnswerStateResponse> getState() {
		AnswerStateResponse response = new AnswerStateResponse(isStart() ? 1 : 0);
		return TResult.sucess(response);
	}
	
	private void sendReward(long actorId, List<RewardObject> rewardList) {
		for(RewardObject reward:rewardList){
			sendReward(actorId, reward);
		}
	}
	
	private void sendReward(long actorId,RewardObject rewardObject){
		switch(rewardObject.rewardType){
		case GOODS:
			goodsFacade.addGoodsVO(actorId, GoodsAddType.QUESTIONS, rewardObject.id ,rewardObject.num);
			break;
		case HEROSOUL:
			heroSoulFacade.addSoul(actorId, HeroSoulAddType.QUESTIONS, rewardObject.id, rewardObject.num);
		default:
			break;
		
		}
	}

}
