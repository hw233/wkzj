package com.jtang.gameserver.module.dailytask.facade.impl.update;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.jtang.gameserver.component.event.EventKey;
import com.jtang.gameserver.module.dailytask.facade.impl.BaseTaskUpdate;
import com.jtang.gameserver.module.dailytask.type.DailyTaskType;

/**
 * 收礼任务实现
 * @author ludd
 *
 */
@Component
public class ReceiveGiftTaskUpdateImpl extends BaseTaskUpdate{
	@PostConstruct
	private void init() {
		eventBus.register(EventKey.RECEIVE_GIFT, this);
	}
	@Override
	protected DailyTaskType getDailyTaskType() {
		return DailyTaskType.RECEIVE_GIFT;
	}
	
	
}
