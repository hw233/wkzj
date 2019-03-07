package com.jtang.gameserver.module.sysmail.help;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jiatang.common.ModuleName;
import com.jtang.core.protocol.Response;
import com.jtang.core.result.ObjectReference;
import com.jtang.gameserver.dbproxy.entity.Sysmail;
import com.jtang.gameserver.module.sysmail.handler.SysmailCmd;
import com.jtang.gameserver.module.sysmail.handler.response.SysmailListResponse;
import com.jtang.gameserver.server.session.PlayerSession;

@Component
public class SysmailPushHelper {
	@Autowired
	PlayerSession playerSession;

	private static ObjectReference<SysmailPushHelper> ref = new ObjectReference<SysmailPushHelper>();

	@PostConstruct
	protected void init() {
		ref.set(this);
	}

	private static SysmailPushHelper getInstance() {
		return ref.get();
	}

	/**
	 * 推送邮件列表
	 * @param actorId
	 * @param sysmailList
	 */
	public static void pushNewSysmailList(long actorId, List<Sysmail> sysmailList) {
		SysmailListResponse response = new SysmailListResponse(sysmailList);
		Response rsp = Response.valueOf(ModuleName.SYSMAIL, SysmailCmd.PUSH_SYSMAIL, response.getBytes());
		getInstance().playerSession.push(actorId, rsp);
	}

	/**
	 * 推送单个系统邮件
	 * @param actorId
	 * @param sysmail
	 */
	public static void pushNewSysmail(long actorId, Sysmail sysmail) {
		List<Sysmail> list = new ArrayList<>();
		list.add(sysmail);
		pushNewSysmailList(actorId, list);
	}
}
