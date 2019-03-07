package com.jtang.gameserver.dataconfig.model;


import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
/**
 * 福神眷顾特权配置
 * @author ludd
 *
 */
@DataFile(fileName = "favorRightConfig")
public class FavorRightConfig implements ModelAdapter{
	/**
	 * 特权id（标识唯一性）
	 */
	public int id;
	/**
	 * 解析器id
	 */
	public int parseId;
	/**
	 * 当id=1时，parseValue->补满精力活力次数
	 * 当id=2时，parseValue->补满精力活力次数
	 * 当id=3时，parseValue->每天领取点券数
	 */
	public String parseValue;
	/**
	 * 当id=1时候，useCondition->无
	 * 当id=2时候，useCondition->充值点券大于10（充值超过1元）
	 * 当id=3时候，useCondition->充值点券大于10（充值超过1元）
	 */
	public int useCondition;
	/**
	 *  useTarget:使用对象（0未充值，1已充值）
	 */
	public int useTarget;
	@Override
	public void initialize() {
	}
	
	
}
