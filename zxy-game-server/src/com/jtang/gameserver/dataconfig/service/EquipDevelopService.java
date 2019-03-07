package com.jtang.gameserver.dataconfig.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jiatang.common.model.EquipType;
import com.jtang.core.dataconfig.ServiceAdapter;
import com.jtang.core.utility.StringUtils;
import com.jtang.gameserver.dataconfig.model.ConvertBlackConfig;
import com.jtang.gameserver.dataconfig.model.ConvertConfig;
import com.jtang.gameserver.dataconfig.model.DevelopConfig;
import com.jtang.gameserver.dataconfig.model.DevelopNoticeConfig;
import com.jtang.gameserver.module.equipdevelop.model.ItemDTO;
import com.jtang.gameserver.module.equipdevelop.type.ConvertTypeEnum;
import com.jtang.gameserver.module.equipdevelop.util.Util;
import com.jtang.gameserver.module.equipdevelop.util.Util.Change;
import com.jtang.gameserver.module.equipdevelop.util.Util.ChangeListToMap;

/**
 * 装备、碎片提炼配置服务类
 * @author hezh
 *
 */
@Component
public class EquipDevelopService extends ServiceAdapter{
	
	/** 装备、碎片提炼配置 ConvertTypeEnum==>star==>ConvertConfig*/
	private static Map<ConvertTypeEnum, Map<Integer,ConvertConfig>> CONVERT_CONFIG_MAP = new HashMap<>();
	
	/** 装备、碎片提炼黑名单 ConvertTypeEnum==>idList*/
	private static Map<ConvertTypeEnum,List<Integer>> CONVERT_BLACK_CONFIG = new HashMap<>();
	
	/** 装备突破配置 EquipType==>equipStar==>num==>DevelopConfig*/
	private static Map<EquipType,Map<Integer,Map<Integer,DevelopConfig>>> DEVELOP_CONFIG_MAP = new HashMap<>();
	
	/** 装备突破公告配置*/
	private static DevelopNoticeConfig noticeConfig; 
	
	@Override
	public void clear() {
		CONVERT_CONFIG_MAP.clear();
		CONVERT_BLACK_CONFIG.clear();
		DEVELOP_CONFIG_MAP.clear();
	}
	
	@Override
	public void initialize() {
		//解析装备、碎片提炼配置表
		List<ConvertConfig> list1 = dataConfig.listAll(this, ConvertConfig.class);
		for (ConvertConfig config : list1) {
			ConvertTypeEnum type = ConvertTypeEnum.get(config.getType());
			if(!CONVERT_CONFIG_MAP.containsKey(type)){
				CONVERT_CONFIG_MAP.put(type,new HashMap<Integer,ConvertConfig>());
			}
			CONVERT_CONFIG_MAP.get(type).put(config.getStar(), config);
		}
		
		//解析装备、碎片提炼黑名单置表
		List<ConvertBlackConfig> list2 = dataConfig.listAll(this, ConvertBlackConfig.class);
		for (ConvertBlackConfig config : list2) {
			ConvertTypeEnum type = ConvertTypeEnum.get(config.getType());
			if(!CONVERT_BLACK_CONFIG.containsKey(type)){
				CONVERT_BLACK_CONFIG.put(type,new ArrayList<Integer>());
			}
			CONVERT_BLACK_CONFIG.get(type).add(config.getId());
		}
		
		//解析装备突破配置
		List<DevelopConfig> list3 = dataConfig.listAll(this, DevelopConfig.class);
		for (DevelopConfig config : list3) {
			EquipType type = EquipType.getType(config.getEquipType());
			if(!DEVELOP_CONFIG_MAP.containsKey(type)){
				DEVELOP_CONFIG_MAP.put(type, new HashMap<Integer, Map<Integer,DevelopConfig>>());
			}
			if(!DEVELOP_CONFIG_MAP.get(type).containsKey(config.getEquipStar())){
				DEVELOP_CONFIG_MAP.get(type).put(config.getEquipStar(),new HashMap<Integer, DevelopConfig>());
			}
			DEVELOP_CONFIG_MAP.get(type).get(config.getEquipStar()).put(config.getNum(), config);
		}
		
		//解析装备突破公告配置
		List<DevelopNoticeConfig> list4 = dataConfig.listAll(this, DevelopNoticeConfig.class);
		if(list4 != null && list4.size() > 0){
			noticeConfig = list4.get(0);
		}
	}
	
	/**
	 * 获取装备突破公告内容
	 * @param actorName
	 * @param equipName
	 * @param developNum
	 * @return
	 */
	public static String getNoticeConfig(String actorName,String equipName,int developNum){
		if(noticeConfig != null){
			if(noticeConfig.getNoticeList().contains(developNum)){
				return Util.stringFormat(noticeConfig.getSystemMsg(), actorName,equipName,developNum);
			}
		}
		return null;
	}
	
	/**
	 * 装备、碎片是否可分解
	 * @param type {@link ConvertTypeEnum}
	 * @param id
	 * @return
	 */
	public static boolean isCanConvert(ConvertTypeEnum type,int id){
		if(CONVERT_BLACK_CONFIG.containsKey(type) && CONVERT_BLACK_CONFIG.get(type).contains(id)){
			return false;
		}
		return true;
	}
	
	/**
	 * 分解装备、碎片获得物品List
	 * @param type
	 * @param star 
	 * @param num
	 * @return
	 */
	public static List<ItemDTO> getRewardGoods(ConvertTypeEnum type,int star,int num){
		ConvertConfig config = CONVERT_CONFIG_MAP.get(type).get(star);
		List<ItemDTO> rewards = Util.change(config.getRewardGoods(),new Change<ItemDTO, String>() {
			@Override
			public List<ItemDTO> changeTo(final String v) {
				//v（0-物品；1-装备；2-仙人魂魄）_ID_数量|...
				List<String[]> list = StringUtils.delimiterString2Array(v);
				List<ItemDTO> dtos = new ArrayList<ItemDTO>();
				for(String[] i : list){
					dtos.add(new ItemDTO(i[0],i[1],i[2]));
				}
				return dtos;
			}
		});
		if(num == 1){
			return rewards;
		}
		List<ItemDTO> rewards2 = new ArrayList<ItemDTO>();
		for(ItemDTO dto : rewards){
			rewards2.add(new ItemDTO(dto.getType(), dto.getId(), dto.getNum() * num));
		}
		return rewards2;
	}
	
	/**
	 * 分解装备、碎片获得物品Map
	 * @param type
	 * @param star
	 * @param num
	 * @return
	 */
	public static Map<Integer,Integer> getRewardGoodsMap(ConvertTypeEnum type,int star,int num){
		return Util.changeListToMap(getRewardGoods(type, star, num),new ChangeListToMap<Integer,Integer, ItemDTO>() {

			@Override
			public Map<Integer,Integer> changeTo(final List<ItemDTO> e) {
				Map<Integer,Integer> map = new HashMap<Integer, Integer>();
				for(ItemDTO dto : e){
					int value = dto.getNum();
					if(map.containsKey(dto.getId())){
						value += map.get(dto.getId());
					}
					map.put(dto.getId(), value);
				}
				return map;
			}
		});
	}
	
	/**
	 * 装备、碎片分解所需消耗
	 * @param type
	 * @param id
	 * @param num
	 * @return
	 */
	public static List<ItemDTO> getConsumes(ConvertTypeEnum type,int star,int num){
		ConvertConfig config = CONVERT_CONFIG_MAP.get(type).get(star);
		List<ItemDTO> consumes = Util.parserConsumes(config.getConsume());
		if(num == 1){
			return consumes;
		}
		List<ItemDTO> consumes2 = new ArrayList<ItemDTO>();
		for(ItemDTO dto : consumes){
			consumes2.add(new ItemDTO(dto.getType(), dto.getId(), dto.getNum() * num));
		}
		return consumes2;
	}
	
	/**
	 * 获取装备突破配置
	 * @param type 装备类型
	 * @param star 星级
	 * @param num 突破次数
	 * @return
	 */
	public static DevelopConfig getDevelopConfig(EquipType type,int star,int num){
		if(!DEVELOP_CONFIG_MAP.containsKey(type)){
			return null;
		}
		if(!DEVELOP_CONFIG_MAP.get(type).containsKey(star)){
			return null;
		}
		return DEVELOP_CONFIG_MAP.get(type).get(star).get(num);
	}
}
