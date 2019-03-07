package com.jtang.gameserver.dbproxy.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.jtang.core.db.DBQueueType;
import com.jtang.core.db.Entity;
import com.jtang.core.db.annotation.Column;
import com.jtang.core.db.annotation.TableName;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
import com.jtang.core.utility.TimeUtils;
import com.jtang.gameserver.dataconfig.model.DailyTasksConfig;
import com.jtang.gameserver.dataconfig.service.DailyTaskService;
import com.jtang.gameserver.module.dailytask.model.DailyTaskVO;
import com.jtang.gameserver.module.dailytask.type.DailyTaskType;
@TableName(name="dailyTask", type=DBQueueType.IMPORTANT)
public class DailyTask extends Entity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6461949732741329310L;
	
	@Column(pk = true)
	private long actorId;
	
	/**
	 * 更新进度时间
	 */
	@Column
	private int progressTime;
	/**
	 * 任务记录
	 */
	@Column
	private String  dailyTaskRecord;
	
	
	private Map<Integer, DailyTaskVO> dailyTaskVOs = new ConcurrentHashMap<>();

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
		DailyTask dailyTask = new DailyTask();
		dailyTask.actorId = rs.getLong("actorId");
		dailyTask.progressTime = rs.getInt("progressTime");
		dailyTask.dailyTaskRecord = rs.getString("dailyTaskRecord");
		return dailyTask;
	}

	@Override
	protected void hasReadEvent() {
		if (StringUtils.isNotBlank(this.dailyTaskRecord)) {
			List<String[]> list = StringUtils.delimiterString2Array(this.dailyTaskRecord);
			for (String[] strs : list) {
				DailyTaskVO dailyTaskVO = new DailyTaskVO(strs);
				this.dailyTaskVOs.put(dailyTaskVO.getTaskId(), dailyTaskVO);
			}
		}
		
	}

	@Override
	protected ArrayList<Object> writeData(Boolean containsPK) {
		ArrayList<Object> value = new ArrayList<>();
		if (containsPK){
		    value.add(this.actorId);
		}
		value.add(this.progressTime);
	    value.add(this.dailyTaskRecord);
		return value;
	}

	@Override
	protected void beforeWritingEvent() {
		if (this.dailyTaskVOs.isEmpty()) {
			this.dailyTaskRecord = EMPTY_STRING;
			return;
		}
		List<String> list = new ArrayList<>();
		for (Entry<Integer, DailyTaskVO> entry : dailyTaskVOs.entrySet()) {
			list.add(entry.getValue().parser2String());
		}
		
		this.dailyTaskRecord = StringUtils.collection2SplitString(list, Splitable.ELEMENT_DELIMITER);
		
	}

	@Override
	protected void disposeBlob() {
		this.dailyTaskRecord = EMPTY_STRING;
	}
	
	/**
	 * 获取单个每日任务数据
	 * @param taskId
	 * @return
	 */
	public DailyTaskVO get(int taskId) {
		return dailyTaskVOs.get(taskId);
	}
	
	/**
	 * 初始化每日任务
	 * @param taskId
	 */
	public void initVO(int taskId, int vipLevel) {
		if (dailyTaskVOs.containsKey(taskId) == false) {
			DailyTaskVO vo = new DailyTaskVO(taskId);
			DailyTasksConfig cfg = DailyTaskService.get(taskId);
			if (cfg.getTaskParser() == DailyTaskType.VIP_COMPLETE.getCode() && vipLevel >= Integer.valueOf(cfg.getCompleteRule())){
				vo.setComplte((byte) 1);
			}
			dailyTaskVOs.put(taskId, vo);
		}
	}
	
	/**
	 * 获取每日任务列表
	 * @return
	 */
	public List<DailyTaskVO> getList() {
		List<DailyTaskVO> list = new ArrayList<>();
		for (DailyTaskVO dailyTaskVO : this.dailyTaskVOs.values()) {
			list.add(dailyTaskVO);
		}
		return list;
	}
	
	public static DailyTask valueOf(long actorId) {
		DailyTask  dailyTask = new DailyTask();
		dailyTask.actorId = actorId;
		dailyTask.progressTime = TimeUtils.getNow();
		return dailyTask;
	}
	/**
	 * 设置进度时间
	 * @param progressTime
	 */
	public void setProgressTime(int progressTime) {
		this.progressTime = progressTime;
	}
	public int getProgressTime() {
		return progressTime;
	}

	public void clear(int vipLevel) {
		this.dailyTaskVOs.clear();
		this.progressTime = TimeUtils.getNow();
		List<Integer> ids = DailyTaskService.getTaskIds();
		for (int taskId : ids) {
			this.initVO(taskId, vipLevel);
		}
	}

}
