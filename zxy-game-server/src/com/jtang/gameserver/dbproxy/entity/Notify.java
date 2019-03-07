package com.jtang.gameserver.dbproxy.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jtang.core.db.DBQueueType;
import com.jtang.core.db.Entity;
import com.jtang.core.db.annotation.Column;
import com.jtang.core.db.annotation.TableName;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.module.notify.model.AbstractNotifyVO;
import com.jtang.gameserver.module.notify.model.impl.AllyFightNotifyVO;
import com.jtang.gameserver.module.notify.model.impl.AllyNotifyVO;
import com.jtang.gameserver.module.notify.model.impl.GiveEquipNotifyVO;
import com.jtang.gameserver.module.notify.model.impl.HoleInviteNotifyVO;
import com.jtang.gameserver.module.notify.model.impl.PowerNotifyVO;
import com.jtang.gameserver.module.notify.model.impl.SnatchAllyNotifyVO;
import com.jtang.gameserver.module.notify.model.impl.SnatchNotifyVO;
import com.jtang.gameserver.module.notify.model.impl.StoryNotifyVO;
import com.jtang.gameserver.module.notify.model.impl.TrialNotifyVO;
import com.jtang.gameserver.module.notify.type.NotifyType;
//import com.jtang.sm2.module.notify.model.impl.PowerRewardNotifyVO;

/**
 * 系统通知(新设计）
 * <pre>
 * 
 * --以下为db说明---------------------------
 * 主键为actorId,每个玩家一条记录。
 * 玩家收发的多条通知分别存储于sendBlob,receiveBlob的字段。
 * sendBlob,receiveBlob字段中的每一条通知由atomicId来管理自增id
 * </pre>
 * @author 0x737263
 *
 */
@TableName(name = "notify", type = DBQueueType.IMPORTANT)
public class Notify extends Entity<Long> {
	private static final long serialVersionUID = 7133832430521447246L;
	private static final Logger LOGGER = LoggerFactory.getLogger(Notify.class);
	
	/**
	 * 角色id  主键
	 */
	@Column(pk = true)
	private long actorId;
	
	/**
	 * 通知自增加id管理
	 */
	@Column
	private AtomicLong atomId = new AtomicLong();
	
	/**
	 * 已发的通知(blob存储)
	 * 格式:通知对象|通知对象|通知对象|通知对象
	 */
	@Column
	private String sendBlob = "";

	/**
	 * 已收的通知（blob存储)
	 * 格式:通知对象|通知对象|通知对象|通知对象
	 */
	@Column
	private String receiveBlob = "";

	/**
	 * 已发通知序列化集合
	 */
	private List<AbstractNotifyVO> sendNotifyList = Collections.synchronizedList(new ArrayList<AbstractNotifyVO>());

	/**
	 * 已收通知序列化集合
	 */
	private List<AbstractNotifyVO> receiveNotifyList = Collections.synchronizedList(new ArrayList<AbstractNotifyVO>());

	@Override
	public Long getPkId() {
		return actorId;
	}

	@Override
	public void setPkId(Long pk) {
		this.actorId = pk;
	}

	@Override
	protected Entity<Long> readData(ResultSet rs, int rowNum) throws SQLException {
		Notify notify = new Notify();
		notify.setPkId(rs.getLong("actorId"));
		notify.atomId.set(rs.getLong("atomId"));
		notify.sendBlob = rs.getString("sendBlob");
		notify.receiveBlob = rs.getString("receiveBlob");
		return notify;
	}

	@Override
	protected void hasReadEvent() {
		// 序列化收发通知对象
		readVO(sendNotifyList, this.sendBlob);
		readVO(receiveNotifyList, this.receiveBlob);
	}
	
	private void readVO(List<AbstractNotifyVO> notifyList, String blobString) {
		List<String[]> list = StringUtils.delimiterString2Array(blobString);
		for (String[] item : list) {
			AbstractNotifyVO vo = parseNotifyVO(item);
			if (vo != null) {
				notifyList.add(vo);
			}
		}
	}
	
	/**
	 * 序列化vo
	 * @param blobString
	 * @return
	 */
	private AbstractNotifyVO parseNotifyVO(String[] blobArray) {
		if (blobArray == null || blobArray.length < 1) {
			return null;
		}

		// TODO ...取出String[0]，看是什么类型。则直接new xx();进行处理
		NotifyType notifyType = NotifyType.get(Integer.valueOf(blobArray[0]));
		if (notifyType == NotifyType.ALLY_FIGHT) {
			AllyFightNotifyVO vo = new AllyFightNotifyVO();
			vo.parse2VO(blobArray);
			return vo;
		}

		if (notifyType == NotifyType.SNATCH) {
			SnatchNotifyVO vo = new SnatchNotifyVO();
			vo.parse2VO(blobArray);
			return vo;
		}
		if (notifyType == NotifyType.SNATCH_ALLY) {
			SnatchAllyNotifyVO vo = new SnatchAllyNotifyVO();
			vo.parse2VO(blobArray);
			return vo;
		}

		if (notifyType == NotifyType.TRIAL) {
			TrialNotifyVO vo = new TrialNotifyVO();
			vo.parse2VO(blobArray);
			return vo;
		}

		if (notifyType == NotifyType.STORY) {
			StoryNotifyVO vo = new StoryNotifyVO();
			vo.parse2VO(blobArray);
			return vo;
		}

		if (notifyType == NotifyType.ADD_ALLY) {
			AllyNotifyVO vo = new AllyNotifyVO();
			vo.parse2VO(blobArray);
			return vo;
		}

		if (notifyType == NotifyType.REMOVE_ALLY) {
			AllyNotifyVO vo = new AllyNotifyVO();
			vo.parse2VO(blobArray);
			return vo;
		}

		if (notifyType == NotifyType.POWER_RANK_CHALLENGE) {
			PowerNotifyVO vo = new PowerNotifyVO();
			vo.parse2VO(blobArray);
			return vo;
		}

		if (notifyType == NotifyType.HOLE_INVITE_ALLY) {
			HoleInviteNotifyVO vo = new HoleInviteNotifyVO();
			vo.parse2VO(blobArray);
			return vo;
		}

//		if (notifyType == NotifyType.THANK_ALLY) {
//			ThankAllyNotifyVO vo = new ThankAllyNotifyVO();
//			vo.parse2VO(blobArray);
//			return vo;
//		}

//		if (notifyType == NotifyType.GIVE_GIFT) {
//			GiveGiftNotifyVO vo = new GiveGiftNotifyVO();
//			vo.parse2VO(blobArray);
//			return vo;
//		}

//		if (notifyType == NotifyType.SNATCH_REVENGE) {
//			SnatchRevengeNotifyVO vo = new SnatchRevengeNotifyVO();
//			vo.parse2VO(blobArray);
//			return vo;
//		}

//		if (notifyType == NotifyType.POWER_REWARD) {
//			PowerRewardNotifyVO vo = new PowerRewardNotifyVO();
//			vo.parse2VO(blobArray);
//			return vo;
//		}

		if (notifyType == NotifyType.VIP_GIVE_EQUIP) {
			GiveEquipNotifyVO vo = new GiveEquipNotifyVO();
			vo.parse2VO(blobArray);
			return vo;
		}

		return null;
	}

	@Override
	protected ArrayList<Object> writeData(Boolean containsPK) {
		ArrayList<Object> value = new ArrayList<>();
		if (containsPK) {
			value.add(this.actorId);
		}
		value.add(atomId.get());
		value.add(sendBlob);
		value.add(receiveBlob);
		return value;
	}

	@Override
	protected void beforeWritingEvent() {
		this.sendBlob = writeVO(sendNotifyList);
		this.receiveBlob = writeVO(receiveNotifyList);
	}
	
	private String writeVO(List<AbstractNotifyVO> notifyList) {
		List<String> stringList = new ArrayList<String>();
		synchronized (notifyList) {
			for (AbstractNotifyVO vo : notifyList) {
				stringList.add(vo.parse2String());
			}			
		}
		return StringUtils.collection2SplitString(stringList, Splitable.ELEMENT_DELIMITER);
	}
	
	public static Notify valueOf(long actorId) {
		Notify entity = new Notify();
		entity.setPkId(actorId);
		return entity;
	}
	
	
	/**
	 * 获取一个自增加id
	 * @return
	 */
	public long incrementId() {
		return atomId.incrementAndGet();
	}

	/**
	 * 获取已发列表
	 * @return
	 */
	public List<AbstractNotifyVO> getSendNotifyList() {
		return sendNotifyList;
	}

	/**
	 * 获取已收列表
	 * @return
	 */
	public List<AbstractNotifyVO> getReceiveNotifyList() {
		return receiveNotifyList;
	}
	
	/**
	 * 添加即将创建通知的集合
	 * @return
	 */
	public List<AbstractNotifyVO> getCreateList(AbstractNotifyVO notifyVO) {
		if (notifyVO.ownerActorId == notifyVO.fromActorId) {
			return getSendNotifyList();
		} else if (notifyVO.ownerActorId == notifyVO.toActorId) {
			return getReceiveNotifyList();
		}
		return null;
	}
	
	/**
	 * 删除邮件
	 * @param actorId
	 * @param nId
	 */
	public boolean removeNotify(List<Long> nIds) {		
		boolean flag = false;
		List<AbstractNotifyVO> sendList = getSendNotifyList();
		synchronized (sendList) { 
			try {
				Iterator<AbstractNotifyVO> iterator = sendList.iterator();
				while (iterator.hasNext()) {
					AbstractNotifyVO vo = iterator.next();
					if (nIds.contains(vo.nId)) {
						iterator.remove();
						if (flag == false) {
							flag = true;
						}
					}
				}
			} catch (Exception ex) {
				LOGGER.error("", ex);
			}
		}
		
		if(flag) {
			return flag;
		}
		
		List<AbstractNotifyVO> receiveList = getReceiveNotifyList();
		synchronized (receiveList) {
			try {
				Iterator<AbstractNotifyVO> iterator = receiveList.iterator();
				while (iterator.hasNext()) {
					AbstractNotifyVO vo = iterator.next();
					if (nIds.contains(vo.nId)) {
						iterator.remove();
						if (flag == false) {
							flag = true;
						}
					}
				}	
			} catch(Exception ex) {
				LOGGER.error("", ex);
			}
		}
		
		return flag;
	}

	@Override
	protected void disposeBlob() {
		sendBlob = EMPTY_STRING;
		receiveBlob = EMPTY_STRING;
	}
	
}
