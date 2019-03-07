package com.jtang.gameserver.dataconfig.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.jtang.core.dataconfig.ModelAdapter;
import com.jtang.core.dataconfig.annotation.DataFile;
import com.jtang.core.dataconfig.annotation.FieldIgnore;
import com.jtang.core.utility.Splitable;
import com.jtang.core.utility.StringUtils;
/**
 * 集众降魔配置
 * @author ludd
 *
 */
@DataFile(fileName = "demonCampConfig")
public class DemonCampConfig implements ModelAdapter {

	/**
	 * 难度id
	 */
	private int difficult;
	
	/**
	 * 蓝阵营
	 */
	private String blueCamp;
	
	/**
	 * 红阵营
	 */
	private String redCamp;
	
	@FieldIgnore
	private Set<Integer> blueCampSet = new HashSet<>();
	@FieldIgnore
	private Set<Integer> redCampSet = new HashSet<>();
	
	@Override
	public void initialize() {
		List<Integer> listBlue = StringUtils.delimiterString2IntList(blueCamp, Splitable.ATTRIBUTE_SPLIT);
		for (Integer integer : listBlue) {
			blueCampSet.add(integer);
		}
		List<Integer> listRed = StringUtils.delimiterString2IntList(redCamp, Splitable.ATTRIBUTE_SPLIT);
		for (Integer integer : listRed) {
			redCampSet.add(integer);
		}
		
		this.blueCamp = null;
		this.redCamp = null;
	}
	

	public int getDifficult() {
		return difficult;
	}
	
	public Set<Integer> getBlueCampSet() {
		return blueCampSet;
	}
	
	public Set<Integer> getRedCampSet() {
		return redCampSet;
	}

	
}
