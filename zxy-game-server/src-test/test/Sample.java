package test;

import com.jtang.core.context.SpringContext;
import com.jtang.gameserver.module.crossbattle.facade.CrossBattleRewardFacade;
import com.jtang.gameserver.module.crossbattle.facade.impl.CrossBattleRewardFacadeImpl;

public class Sample {
	
	public static String callTest() {
		return "fsdfsdf call test is ok !!!!";
	}

	public static void main(String[] args) {
		CrossBattleRewardFacade facade = (CrossBattleRewardFacade) SpringContext.getBean(CrossBattleRewardFacadeImpl.class);
		facade.getReward(88080389L);
	}
}
