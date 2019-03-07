package com.jtang.gameserver.dataconfig.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.core.utility.RandomUtils;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.dataconfig.model.QuestionsConfig;
import com.jtang.gameserver.dataconfig.model.QuestionsPoolConfig;
import com.jtang.gameserver.dataconfig.model.QuestionsRewardConfig;
import com.jtang.gameserver.module.demon.model.OpenTime;

@Component
public class QuestionService extends ServiceAdapter {
	
	private static List<OpenTime> openTimes = new ArrayList<>();
	
	private static Map<Integer,QuestionsPoolConfig> QUESTIONS_MAP = new HashMap<>();
	
	private static QuestionsConfig GLOBAL_CONFIG = new QuestionsConfig();

	private static Map<Integer,QuestionsRewardConfig> REWARD_MAP = new HashMap<>();
	
	@Override
	public void clear() {
		QUESTIONS_MAP.clear();
		GLOBAL_CONFIG = new QuestionsConfig();
		openTimes.clear();
		REWARD_MAP.clear();
	}

	@Override
	public void initialize() {
		List<QuestionsPoolConfig> questionsList = dataConfig.listAll(this, QuestionsPoolConfig.class);
		for (QuestionsPoolConfig config : questionsList) {
			QUESTIONS_MAP.put(config.id, config);
		}
		
		List<QuestionsConfig> globalList = dataConfig.listAll(this, QuestionsConfig.class);
		for(QuestionsConfig config : globalList){
			List<String[]> timeList = StringUtils.delimiterString2Array(config.openTime);
			for (String[] timestr : timeList) {
				OpenTime openTime = new OpenTime(timestr);
				openTimes.add(openTime);
			}
			GLOBAL_CONFIG = config;
		}
		
		List<QuestionsRewardConfig> rewardList = dataConfig.listAll(this, QuestionsRewardConfig.class);
		for (QuestionsRewardConfig config : rewardList) {
			REWARD_MAP.put(config.count, config);
		}
	}
	
	public static List<OpenTime> getOpenTimes() {
		return openTimes;
	}
	
	public static QuestionsConfig getGlobalConfig(){
		return GLOBAL_CONFIG;
	}
	
	public static QuestionsPoolConfig getQuestionsConfig(int questionsId){
		return QUESTIONS_MAP.get(questionsId);
	}

	public static Map<Integer, Integer> initQuestion() {
		QuestionsConfig globalConfig = getGlobalConfig();
		Map<Integer,Integer> map = new HashMap<>();
		int index[] =  RandomUtils.uniqueRandom(globalConfig.questionsNum, 0, QUESTIONS_MAP.size() - 1);
		List<QuestionsPoolConfig> list = new ArrayList<>(QUESTIONS_MAP.values());
		for(Integer i:index){
			QuestionsPoolConfig config = list.get(i);
			map.put(config.id, 0);
		}
		return map;
	}

	public static QuestionsRewardConfig getReward(int i) {
		return REWARD_MAP.get(i);
	}
	
}
