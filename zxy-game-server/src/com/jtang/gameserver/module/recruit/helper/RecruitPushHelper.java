//package com.jtang.gameserver.module.recruit.helper;
//
//import javax.annotation.PostConstruct;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import com.jtang.core.result.ObjectReference;
//import com.jtang.gameserver.module.recruit.facade.RecruitFacade;
//import com.jtang.gameserver.server.broadcast.Broadcast;
//
///**
// * 聚仙阵属性推送帮助类
// * @author 0x737263
// *
// */
//@Component
//public class RecruitPushHelper {
//
//	@Autowired
//	private Broadcast broadcast;
//	
//	@Autowired
//	private RecruitFacade recruitFacade;
//	
//	private static ObjectReference<RecruitPushHelper> ref = new ObjectReference<RecruitPushHelper>();
//	
//	@PostConstruct
//	protected void init() {
//		ref.set(this);
//	}
//	
//	private static RecruitPushHelper getInstance() {
//		return ref.get();
//	}
//	
//	private static RecruitFacade recruitFacade() {
//		return getInstance().recruitFacade;
//	}
//	
//	
//}
