package com.jtang.gameserver.module.battle.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.core.utility.ZipUtil;
import com.jtang.gameserver.module.battle.constant.WinLevel;

/**
 * 战斗数据传输对象(用在模块之间数据传输)
 * @author vinceruan
 *
 */
public class FightData extends IoBufferSerializer {
	private static final Logger LOGGER = LoggerFactory.getLogger(FightData.class);
	
	private static final int ZIP_SIZE = 2048;
	
	public short zipLength = -1;
	/**
	 * 地图id
	 */
	public byte mapId = 1;
	
	/**
	 * 玩家自己的战斗队伍
	 */
	public AttackTeam myTeam;
	
	/**
	 * 对方的队伍
	 */
	public AttackTeam enemyTeam;
	
	/**
	 * 开战时处理的buffer处理列表
	 */
	public SequenceAction buffersBeforFight = new SequenceAction();
	
	/**
	 * 每一轮的战斗情况
	 */
	public List<RoundResult> roundResults = new ArrayList<RoundResult>();
	
	/**
	 * 攻击方的总伤害量
	 */
	public int atkTeamHitPoints;
	
	/**
	 * 防御方的总伤害量
	 */
	public int defTeamHitPoints;
	
	/**
	 * 战斗结果,实际下发到客户端的是WinLevel的code字段
	 */
	public WinLevel result;
	
	/**
	 * PVE战斗时,是否可以略过战斗
	 * 0:不可以 1:可以 2.PVP战斗，客户端待定
	 * PVP的是否可以略过战斗客户端自己去判断
	 * 具体见:{@code BattleSkipPlayType}
	 */
	private byte canSkipPlay = 0;
	
	/**
	 * 先手方
	 * 1.上面
	 * 2.下面
	 */
	public byte firstAtkTeam;
	
	public static Map<String, Tile> transform = new HashMap<>();
	static {
		
		transform.put("0-0", new Tile(0,5));
		transform.put("1-0", new Tile(0,4));
		transform.put("2-0", new Tile(0,3));
		transform.put("3-0", new Tile(0,2));
		transform.put("4-0", new Tile(0,1));
		transform.put("5-0", new Tile(0,0));
		transform.put("0-1", new Tile(1,5));
		transform.put("1-1", new Tile(1,4));
		transform.put("2-1", new Tile(1,3));
		transform.put("3-1", new Tile(1,2));
		transform.put("4-1", new Tile(1,1));
		transform.put("5-1", new Tile(1,0));
		transform.put("0-2", new Tile(2,5));
		transform.put("1-2", new Tile(2,4));
		transform.put("2-2", new Tile(2,3));
		transform.put("3-2", new Tile(2,2));
		transform.put("4-2", new Tile(2,1));
		transform.put("5-2", new Tile(2,0));
		
		transform.put("0-3", new Tile(3,5));
		transform.put("1-3", new Tile(3,4));
		transform.put("2-3", new Tile(3,3));
		transform.put("3-3", new Tile(3,2));
		transform.put("4-3", new Tile(3,1));
		transform.put("5-3", new Tile(3,0));
		transform.put("0-4", new Tile(4,5));
		transform.put("1-4", new Tile(4,4));
		transform.put("2-4", new Tile(4,3));
		transform.put("3-4", new Tile(4,2));
		transform.put("4-4", new Tile(4,1));
		transform.put("5-4", new Tile(4,0));
		transform.put("0-5", new Tile(5,5));
		transform.put("1-5", new Tile(5,4));
		transform.put("2-5", new Tile(5,3));
		transform.put("3-5", new Tile(5,2));
		transform.put("4-5", new Tile(5,1));
		transform.put("5-5", new Tile(5,0));


		transform.put("0-6", new Tile(6,5));
		transform.put("1-6", new Tile(6,4));
		transform.put("2-6", new Tile(6,3));
		transform.put("3-6", new Tile(6,2));
		transform.put("4-6", new Tile(6,1));
		transform.put("5-6", new Tile(6,0));
		transform.put("0-7", new Tile(7,5));
		transform.put("1-7", new Tile(7,4));
		transform.put("2-7", new Tile(7,3));
		transform.put("3-7", new Tile(7,2));
		transform.put("4-7", new Tile(7,1));
		transform.put("5-7", new Tile(7,0));
	}
	
	/**
	 * 返回buffer数组
	 * 
	 * @return
	 */
	public byte[] getBytes() {
		write();
		byte[] bytes = null;
		if (writeBuffer.limit() == 0) {
			bytes = writeBuffer.array();
		} else {
			bytes = new byte[writeBuffer.position()];
			writeBuffer.flip();
			writeBuffer.get(bytes);
		}
		writeBuffer.clear();
		//压缩处理
		// 首先放入压缩后的大小，不压缩放-1
		// 再放入原始数据大小
 		byte[] temp;
 		short len = (short) (bytes.length);
		if (bytes.length >= ZIP_SIZE) {
			temp = ZipUtil.compress(bytes);
			writeBuffer.putShort((short) temp.length);
			writeBuffer.putShort(len);
			writeBuffer.put(temp);
		} else {
			writeBuffer.putShort(len);
			writeBuffer.putShort((short) -1);
			writeBuffer.put(bytes);
		}
		bytes = new byte[writeBuffer.position()];
		writeBuffer.flip();
		writeBuffer.get(bytes);
		writeBuffer.clear();
		return bytes;
	}
	
	
	@Override
	public void write() {
		writeByte(mapId);
		writeAttackTeam(myTeam);
		writeAttackTeam(enemyTeam);
		writeSequence(buffersBeforFight);
		writeRoundReports(roundResults);
		writeInt(atkTeamHitPoints);
		writeInt(defTeamHitPoints);
		writeByte(result.getCode());
		writeByte(canSkipPlay);
		writeByte(firstAtkTeam);
	}
	
	private void writeAttackTeam(AttackTeam team) {
		writeByte(team.face);
		writeAttackList(team.attackerList);
		writeInt(team.morale);
	}
	
	private void writeAttackList(List<Attacker> attackerList) {
		writeShort((short)attackerList.size());
		for(Attacker atk : attackerList) {
			writeAttacker(atk);
		}
	}
	
	private void writeAttacker(Attacker atk) {
		writeByte(atk.id);
		writeInt(atk.heroId);
		writeShort(atk.level);
		Tile tile = transform.get(atk.x + "-" + atk.y);
		writeByte((byte)tile.getX());
		writeByte((byte)tile.getY());
		writeInt(atk.atk);
		writeInt(atk.defense);
		writeInt(atk.hp);
		writeInt(atk.exp);
		writeByte(atk.atkScope);
		writeInt(atk.hp_max);
		writeByte(atk.type);
		writeInt(atk.weaponId);//武器ID
		writeShort(atk.weaponLevel);//武器等级
		writeInt(atk.armorId);//防具id
		writeShort(atk.armorLevel);//防具等级
		writeInt(atk.ornamentsId);//饰品id
		writeShort(atk.ornamentsLevel);//饰品等级
		writeInt(atk.skillId);//主动技能
		writeIntList(atk.passiveSkills);
		writeByte(atk.campId);
		writeInt(atk.breakThroughCount);
	}

		
	/**
	 * 下发所有的回合信息
	 */
	private void writeRoundReports(List<RoundResult> roundResults) {
		writeShort((short)(roundResults.size()));
		for (RoundResult roundReport : roundResults) {
			writeRoundReport(roundReport);
		}
	}
	
	/**
	 * 下发一个回合的信息
	 * @param report
	 */
	private void writeRoundReport(RoundResult report) {
		//回合前的buffer变动
		writeSequence(report.buffersBeforRound);
		
		//每个人的出手情况
		writeShort((short)(report.atkResults.size()));
		for (Action action : report.atkResults) {
			writeAction(action);
		}
		
		//回合后的buffer变动
		writeSequence(report.buffersAfterRound);
	}
	
	private void writeAction(Action action) {		
		if (action instanceof BufferAction) {
			writeBufferAction((BufferAction)action);
		} else if (action instanceof SkillAction) {				
			writeSkillAction((SkillAction)action);
		} else if (action instanceof MoveAction) {				
			writeMoveAction((MoveAction)action);
		} else if (action instanceof PositionAction) {				
			writePositionAction((PositionAction)action);
		} else if(action instanceof DropGoodsAction){
			writeDropGoodsAction((DropGoodsAction)action);
		} else if (action instanceof SpawnAction) {
			writeSpawnAction((SpawnAction)action);
		} else if(action instanceof SequenceAction) {
			writeSequence((SequenceAction)action);
		} else if (action instanceof DropActorPropertyAction) {
			writeDropActorPropertyAction((DropActorPropertyAction)action);
		} else if (action instanceof ImmunityAction) {
			writeImmunityAction((ImmunityAction)action);
		} else if (action instanceof DodgeAction) {
			writeDodgeAction((DodgeAction)action);
		} else if (action instanceof TeleportAction) {
			writeTeleportAction((TeleportAction)action);
		} else {
			//不应该运行到这里
			LOGGER.error("未知的类型，不可序列化");
		}
	}
	
	private void writeDropActorPropertyAction(DropActorPropertyAction action) {
		writeBytes(action.getBytes());
	}
	private void writeImmunityAction(ImmunityAction action) {
		writeBytes(action.getBytes());
	}
	private void writeDodgeAction(DodgeAction action) {
		writeBytes(action.getBytes());
	}
	
	private void writeSpawnAction(SpawnAction action) {
		writeBytes(action.getBytes());
	}
	
	private void writeSequence(SequenceAction action) {
		writeBytes(action.getBytes());
	}
	
	/**
	 * 写入物品掉落
	 * @param action
	 */
	private void writeDropGoodsAction(DropGoodsAction action) {
		writeBytes(action.getBytes());
	}
	
	
	/**
	 * 写入一个buffer信息
	 * @param action
	 */
	private void writeBufferAction(BufferAction action) {
		writeBytes(action.getBytes());
	}
	
	/**
	 * 写入一个移动数据包
	 * @param action
	 */
	private void writeMoveAction(MoveAction action) {
		writeBytes(action.getBytes());
	}
	/**
	 * 写入一个移动数据包
	 * @param action
	 */
	private void writeTeleportAction(TeleportAction action) {
		writeBytes(action.getBytes());
	}
	/**
	 * 写入一个移动数据包
	 * @param action
	 */
	private void writePositionAction(PositionAction action) {
		writeBytes(action.getBytes());
	}
	
	
	/**
	 * 写入一个攻击数据包
	 * @param action
	 */
	private void writeSkillAction(SkillAction action) {
		writeBytes(action.getBytes());
	}

	
	
	/**
	 * 将对象打印
	 * @param indentStr
	 * @return
	 */
	public String format(String indentStr) {
		String lv1Indent = indentStr; //一级缩进
		String lv2Indent = lv1Indent + "++"; //二级缩进
		
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%smapId=%s\r\n", lv1Indent, mapId));
		
		sb.append(String.format("%smyTeam:\r\n", lv1Indent));
		sb.append(myTeam.format(lv2Indent));
		
		sb.append(String.format("%senemyTeam:\r\n", lv1Indent));
		sb.append(enemyTeam.format(lv2Indent));
		
		sb.append(String.format("%sBufferBeforeFight Start:\r\n", lv1Indent));
		sb.append(buffersBeforFight.format(lv2Indent));
		sb.append(String.format("%sBufferBeforeFight End:\r\n", lv1Indent));
		int i = 1;
		for(RoundResult res : roundResults) {
			sb.append(String.format("RoundIndex:%d\r\n", i++));
			sb.append(res.format(lv1Indent));
		}
		sb.append(String.format("points:【atkTeamHitPoints=%d, defTeamHitPoints=%d】\r\n",
				atkTeamHitPoints, defTeamHitPoints));
		sb.append("result:" + result + "\r\n");
		sb.append("canSkipPlay:" + canSkipPlay + "\r\n");
		return sb.toString();
	}

	public void setCanSkipPlay(byte canSkipPlay) {
		this.canSkipPlay = canSkipPlay;
	}
}
