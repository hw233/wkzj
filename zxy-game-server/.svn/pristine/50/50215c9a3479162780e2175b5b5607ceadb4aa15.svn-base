package com.jtang.gameserver.module.applog.facade.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.dbproxy.entity.AppLog;
import com.jtang.gameserver.module.applog.dao.LogDao;
import com.jtang.gameserver.module.applog.facade.LogFacade;
import com.jtang.gameserver.module.applog.type.LogType;
import com.jtang.gameserver.module.extapp.welkin.module.WelkinRankVO;

@Component
public class LogFacadeImpl implements LogFacade {

	@Autowired
	LogDao logDao;

	@Override
	public void saveWelkin(List<WelkinRankVO> list) {
		if(list.size() > 0){
			StringBuffer sb = new StringBuffer();
			for (WelkinRankVO rankVO : list) {
				sb.append(rankVO.parseToString());
				sb.append(Splitable.ELEMENT_DELIMITER);
			}
			sb.deleteCharAt(sb.length() - 1);
			AppLog log = AppLog.valueOf(LogType.WELKIN,sb.toString(),TimeUtils.getYesterDayZero(),TimeUtils.getNow());
			logDao.save(log);
		}
	}

}
