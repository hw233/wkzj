package com.jtang.gameserver.module.battle.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 一个回合的战斗情况
 * @author vinceruan
 *
 */
public class RoundResult {
	/**
	 * 一个回合开始时的buffer处理列表
	 */
	public SequenceAction buffersBeforRound = new SequenceAction();
	
	/**
	 * 每个人的出手情况
	 */
	public List<Action> atkResults = new ArrayList<Action>();
	
	/**
	 * 一个回合结束时的buffer处理列表
	 */
	public SequenceAction buffersAfterRound = new SequenceAction();
	
	
	public String format(String indentStr) {
		String titleIndent = indentStr;
		String contentIndent = titleIndent + "++";
		String childInent = contentIndent + "++";
		
		StringBuilder sd = new StringBuilder();
		sd.append(String.format("%sRoundResult Start\r\n", titleIndent));
		sd.append(String.format("%sBuffersBeforeRound:\r\n", contentIndent));
		sd.append(buffersBeforRound.format(childInent));
		sd.append(String.format("%satkResults:\r\n", contentIndent));
		for (Action res : atkResults) {
			sd.append(res.format(childInent));
		}
		sd.append(String.format("%sBuffersAfterRound:\r\n", contentIndent));
		sd.append(buffersAfterRound.format(childInent));
		sd.append(String.format("%sRoundResult End\r\n", titleIndent));
		return sd.toString();
	}
}
