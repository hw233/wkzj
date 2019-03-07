package test;

import com.jtang.core.context.SpringContext;
import com.jtang.gameserver.module.sprintgift.facade.SprintGiftFacade;
import com.jtang.gameserver.module.sprintgift.facade.impl.SprintGiftFacadeImpl;

public class SprintGiftTest {

	public static void main(String[] args) {
		SprintGiftFacade sprintGiftFacade = (SprintGiftFacade) SpringContext.getBean(SprintGiftFacadeImpl.class);
		
		sprintGiftFacade.getSprintGiftStatusList(41980789738L);
	}

}
