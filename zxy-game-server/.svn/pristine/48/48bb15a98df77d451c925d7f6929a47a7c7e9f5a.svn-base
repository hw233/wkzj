package com.jtang.gameserver.dataconfig.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.rhino.FormulaHelper;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
/**
 * 年兽boss配置
 * @author copy ludd
 *
 */
@DataFile(fileName = "beastMonsterConfig")
public class BeastMonsterConfig implements ModelAdapter {

	/**
	 * 配置id
	 */
	private int id;
	/**
	 * 怪物
	 */
	private String monster;
	/**
	 * 攻击力表达式
	 */
	private String monsterAttack;
	/**
	 * 防御力表达式
	 */
	private String monsterDeffends;
	/**
	 * 血值表达式
	 */
	private String monsterHp;
	
	/**
	 * 气势
	 */
	public int monsterMinHp;
	
	/**
	 * 气势
	 */
	private int morale;
	
	/**
	 * 附加血系数（格式:时间最小值_时间最大值_系数|...)
	 */
	private String extraArgs;
	
	@FieldIgnore
	private Map<Integer, Integer> monsterList = new HashMap<Integer, Integer>();
	
	@FieldIgnore
	private List<String> args = new ArrayList<>();
	
	
	@Override
	public void initialize() {
		monsterList.clear();
		List<String> list = StringUtils.delimiterString2List(monster, Splitable.ELEMENT_SPLIT);
		for (String item : list) {
			List<String> attrs = StringUtils.delimiterString2List(item, Splitable.ATTRIBUTE_SPLIT);
			int monsterId = Integer.valueOf(attrs.get(0));
			int gridIndex = Integer.valueOf(attrs.get(1));
			monsterList.put(monsterId, gridIndex);
		}
		args.clear();
		String[] arg = StringUtils.split(extraArgs, Splitable.ELEMENT_SPLIT);
		for (String string : arg) {
			args.add(string);
		}
		
		this.monster = null;
		this.extraArgs = null;
	}
	
	public int getMonsterAttack(int totalLevel) {
		return FormulaHelper.executeInt(monsterAttack, totalLevel);
	}
	
	public int getMonsterDeffends(int totalLevel) {
		return FormulaHelper.executeInt(monsterDeffends, totalLevel);
	}
	
	public int getMonsterHp(int totalLevel) {
		return FormulaHelper.executeInt(monsterHp, totalLevel);
	}
	
	public int getId() {
		return id;
	}
	
	public Map<Integer, Integer> getMonsterList() {
		return monsterList;
	}
	
	public int getMorale() {
		return morale;
	}
	
	/**
	 * 获取本次Boss血
	 * @param totalHert 上次总伤害
	 * @param time 上次杀死boss耗时（秒）
	 * @return
	 */
	public int getExtraHp(int totalHert, int time) {
		Double minutes = Math.ceil(time * 1.0 / 60);
		int minute = minutes.intValue();
		double num = 0.0;
		for (String arg : args) {
			String[] strs = StringUtils.split(arg, Splitable.ATTRIBUTE_SPLIT);
			strs = StringUtils.fillStringArray(strs, 3, "0");
			int start = Integer.valueOf(strs[0]);
			int end = Integer.valueOf(strs[1]);
			if (start <= minute && minute <= end) {
				num = Double.valueOf(strs[2]);
				break;
			}
		}
		Double result = num * totalHert;
		return result.intValue();
	}
	

}
