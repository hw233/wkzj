package com.jtang.gameserver.module.extapp.questions.handler.response;

import java.util.Map;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.dbproxy.entity.Questions;

public class QuestionsRepsone extends IoBufferSerializer {

	/**
	 * 问题列表
	 * key:问题id
	 * value:问题状态 0.未回答 1.已回答
	 */
	public Map<Integer,Integer> questions;
	
	/**
	 * 已使用答题次数
	 * @param questions
	 */
	public int num;
	
	public QuestionsRepsone(Questions questions){
		this.questions = questions.recordMap;
		this.num = questions.useNum;
	}
	
	@Override
	public void write() {
		writeIntMap(questions);
		writeInt(num);
	}
	
}
