package com.jtang.gameserver.module.extapp.questions.dao;

import com.jtang.gameserver.dbproxy.entity.Questions;

public interface QuestionsDao {

	public Questions get(long actorId);

	public void update(Questions questions);

}
