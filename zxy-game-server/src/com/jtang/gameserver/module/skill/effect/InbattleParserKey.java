package com.jtang.gameserver.module.skill.effect;

/**
 * 战斗中的效果解析器
 * @author 0x737263
 *
 */
public interface InbattleParserKey {
	//-----------------------  加减血量    --------------------------
	/**
	 * 伤害
	 * 根据攻击者的当前攻击力计算伤害
	 * <pre>
	 * x1*1
	 * x1:攻击方的当前攻击力
	 * </pre>
	 * 
	 */
	public static final int Parser1100 = 1100;
	
	/**
	 * 反击(根据被伤害值计算反击的伤害值)
	 * <pre>
	 * x1*0.5
	 * x1:被伤害值
	 * </pre>
	 */
	public static final int Parser1101 = 1101;
	

	/**
	 * 加生命
	 * 恢复目标一定量的的血(根据目标生命上限计算回复值)
	 * <pre>
	 * x1*0.05
	 * x1:目标的生命上限(基础值)
	 * </pre>
	 */
	public static final int Parser1102 = 1102;
	
	/**
	 * 加生命
	 * 回复目标一定量的血(根据伤害值[被伤害值]计算吸血量) 例如：吸血,守山大神
	 * <pre>
	 * x1*0.3
	 * x1:伤害值
	 * </pre>
	 */
	public static final int Parser1103 = 1103;
	
	/**
	 * 加生命
	 * 回复目标血量(根据施法者的基础攻击力、治疗目标的仙人ID计算回复量)
	 * <pre>
	 * x2==100?x1*1.5:x1*0.2 [多心经]
	 * x1:施法者的基础攻击力
	 * x2:治疗目标的仙人ID
	 * </pre>
	 */
	public static final int Parser1104 = 1104;
	
	/**
	 * 加生命
	 * 回复目标血量(根据施法者的当前攻击力、治疗目标的仙人ID计算回复量)
	 * <pre>
	 * x2==100?x1*1.5:x1*0.2 [多心经]
	 * x1:施法者的当前攻击力
	 * x2:治疗目标的仙人ID
	 * </pre>
	 */
	public static final int Parser11040 = 11040;
	
	/**
	 * 加生命
	 * 回复目标血量(根据施法者的生命上限计算回复量)
	 * <pre>
	 * x1*0.5
	 * x1:施法者的生命上限值(基础值)
	 * </pre>
	 */
	public static final int Parser1105 = 1105;
	
	/**
	 * 伤害
	 * 根据施法者的当前攻击力和受害者的当前防御值计算伤害值
	 * <pre>
	 * Math.abs(x1-x2/x3)*2
	 * x1:施法者的基础攻击力
	 * x2:受害者的基础防御值
	 * x3:防御系数
	 * </pre>
	 */
	public static final int Parser1106 = 1106;
	
	/**
	 * 伤害
	 * 根据目标的血量计算伤害值,例如秒杀
	 * <pre>
	 * x1
	 * x1:目标的血量
	 * </pre>
	 */
	public static final int Parser1107 = 1107;
	
	/**
	 * 伤害
	 * 根据攻击者当前攻击力、被攻击者的当前防御力、被攻击者的星级计算伤害
	 * <pre>
	 * 	(x1-x2/x3)*x4
	 *  x1:攻击者的当前攻击力
	 *  x2:被攻击者的当前防御力
	 *  x3:防御系数
	 *  x4:被攻击者的星级
	 * </pre>
	 */
	public static final int Parser1108 = 1108;
	
	/**
	 * 伤害
	 * 根据攻击者的当前攻击力、被攻击者的当前防御力、攻击者和被击者的距离计算伤害
	 * <pre>
	 * 	(x1-x2/x3)*(1+x4*0.1)
	 *  x1:攻击者的当前攻击力
	 *  x2:被攻击者的当前防御力
	 *  x3:防御系数
	 *  x4:距离
	 * </pre>
	 */
	public static final int Parser1109 = 1109;
	
	//-----------------------  加生命上限    --------------------------
	
	/**
	 * 加生命上限
	 * 提高目标的生命上限值(根据目标的基础生命上限值计算加成)
	 * <pre>
	 * x1*0.2
	 * x1:目标的基础生命上限
	 * </pre>
	 */
	public static final int Parser1110 = 1110;
	
	//-----------------------  加经验   --------------------------
	
	/**
	 * 加经验
	 * 提高仙人的经验收获(在基础经验上面加成)
	 * <pre>
	 * x1*0.1
	 * x1:副本经验
	 * </pre>
	 */
	public static final int Parser1120 = 1120;
	
	//-----------------------  加技能触发几率    --------------------------
	
	/**
	 * 加技能触发几率
	 * 提高技能的触发几率(注意:是提高了多少,而不是提高到多少)
	 * <pre>
	 * 1001|400
	 * 表示将技能1001的触发几率提高400(如果原来的几率是200，那么现在的触发几率就是60%)
	 * </pre>
	 */
	public static final int Parser1130 = 1130;
	
	//-----------------------  加减防御值    --------------------------
	
	/**
	 * 加防御力
	 * 提高目标的防御力(在目标的基础防御力的基础上面加成)
	 * <pre>
	 * x1*0.1
	 * x1:目标的基础防御值
	 * </pre>
	 */
	public static final int Parser1140 = 1140;
	
	/**
	 * 加防御力
	 * 提高目标的防御力(在目标的当前防御力的基础上面加成)
	 * <pre>
	 * x1*0.1
	 * x1:目标的当前防御值
	 * </pre>
	 */
	public static final int Parser11400 = 11400;
	
	/**
	 * 加防御力
	 * 提高目标的防御值(根据施法者的防御力计算增加值)
	 * <pre>
	 * x1*0.2
	 * x1:施法者的基础防御值
	 * </pre>
	 */
	public static final int Parser1141 = 1141;
	
	/**
	 * 加防御力
	 * 提高目标的防御值(根据施法者的当前防御力计算增加值)
	 * <pre>
	 * x1*0.2
	 * x1:施法者的当前防御值
	 * </pre>
	 */
	public static final int Parser11410 = 11410;
	
	/**
	 * 减防御
	 * 减少目标的防御力(在目标的基础防御力的基础上计算减少量)
	 * <pre>
	 * x1*0.3
	 * x1:目标的基础防御值
	 * </pre>
	 */
	public static final int Parser1142 = 1142;
	
	/**
	 * 减防御
	 * 减少目标的防御力(在目标的当前防御力的基础上计算减少量)
	 * <pre>
	 * x1*0.3
	 * x1:目标的当前防御值
	 * </pre>
	 */
	public static final int Parser11420 = 11420;
	
	/**
	 * 减防御
	 * 减少目标的防御力(在施法者的基础防御力的基础上计算减少量)
	 * <pre>
	 * x1*0.3
	 * x1:施法者的基础防御值
	 * </pre>
	 */
	public static final int Parser1143 = 1143;
	
	/**
	 * 减防御
	 * 减少目标的防御力(在施法者的当前防御力的基础上计算减少量)
	 * <pre>
	 * x1*0.3
	 * x1:施法者的当前防御值
	 * </pre>
	 */
	public static final int Parser11430 = 11430;
	
	/**
	 * 加防御力和血量
	 * <pre>
	 * x2==1?x1*0.2:x1*0.3
	 * x1:目标的基础防御力或者基础生命值上限
	 * x2:标识x1参数的类型:1=防御力 2=生命值上限
	 * <pre>
	 */
	public static final int Parser1144 = 1144;
	
	/**
	 * 加攻击力和防御力
	 * <pre>
	 * x2==1?x1*0.2:x1*0.3
	 * x1:目标的基础攻击力或者基础防御力
	 * x2:标识x1参数的类型:1=攻击力 2=防御力
	 * <pre>
	 */
	public static final int Parser1145 = 1145;
	
	//-----------------------  加减攻击力  --------------------------
	
	/**
	 * 加攻击力
	 * 提高目标的攻击力(根据施法者的基础攻击力计算加成值)
	 * <pre>
	 * x1*0.15
	 * x1:施法者的基础攻击力
	 * </pre>
	 */
	public static final int Parser1150 = 1150;
	
	/**
	 * 加攻击力
	 * 提高目标的攻击力(根据施法者的当前攻击力计算加成值)
	 * <pre>
	 * x1*0.15
	 * x1:施法者的当前攻击力
	 * </pre>
	 */
	public static final int Parser11500 = 11500;
	
	/**
	 * 加攻击力
	 * 提高目标的攻击力(根据目标的当前攻击力计算加成值,包括自身的时候，目标会以增加之前的值来计算)
	 * <pre>
	 * x1*0.15
	 * x1:目标的当前攻击力
	 * </pre>
	 */
	public static final int Parser11501 = 11501;
	
	/**
	 * 加攻击力
	 * 提高目标的攻击力(根据目标的基础攻击力计算加成值)
	 * <pre>
	 * x1*0.15
	 * x1:目标的基础攻击力
	 * </pre>
	 */
	public static final int Parser1151 = 1151;
	
	/**
	 * 加攻击力
	 * 提高目标的攻击力(根据目标的当前攻击力计算加成值)
	 * <pre>
	 * x1*0.15
	 * x1:目标的当前攻击力
	 * </pre>
	 */
	public static final int Parser11510 = 11510;
	
	/**
	 * 加攻击力和血量
	 * <pre>
	 * x2==1?x1*0.2:x1*0.3
	 * x1:目标的基础攻击力或者基础生命值上限
	 * x2:标识x1参数的类型:1=攻击力 2=生命值上限
	 * <pre>
	 */
	public static final int Parser1152 = 1152;
	
	/**
	 * 减少攻击力
	 * 减少目标的攻击力(根据施法者的当前攻击力计算减少值)
	 * <pre>
	 * x1*0.15
	 * x1:施法者的当前攻击力
	 * </pre>
	 */
	public static final int Parser1153 = 1153;
	//-----------------------  加射程    --------------------------
	
	/**
	 * 加射程
	 * 提高目标的射程
	 * <pre>
	 * 1
	 * 表示射程增加1格
	 * </pre>
	 */
	public static final int Parser1160 = 1160;
	
	//-----------------------  行动受制    --------------------------
	
	/**
	 * 定身(不需要填写表达式)
	 * effectExpr="m"
	 *  m：定身状态  {@code ImmobilezeState}
	 */
	public static final int Parser1170 = 1170;
	
	/**
	 * 伤害+定身
	 * <pre>
	 *  伤害计算
	 *  effectExpr="m|x1-x2/x3"
	 *  m：定身状态  {@code ImmobilezeState}
	 *  x1:仙人的当前攻击力
	 *  x2:被攻击者的当前防御力
	 *  x3:防御系数
	 *  定身持续时间:
	 *  effectCycle="1" effectEndType="2"
	 * </pre>
	 */
	public static final int Parser1171 = 1171;
		
	//-----------------------  加闪避   --------------------------
	/**
	 * 加闪避
	 */
	public static final int Parser1180 = 1180;
	/**
	 * 加免疫
	 * 表达式为要闪避的效果id
	 * effectExpr="id1|id2|..."
	 */
	public static final int Parser1181 = 1181;
	
	
	//-----------------------  加普攻次数   --------------------------
	
	/**
	 * 增加普攻次数
	 * <pre>
	 * 1
	 * 表示目标的普攻次数+1
	 * </pre>
	 */
	public static final int Parser1190 = 1190;
	
	//-----------------------  复活   --------------------------
	
	/**
	 * <pre>
	 * 无需表达式
	 * </pre>
	 */
	public static final int Parser1200 = 1200;
	
	/**
	 * 有概率复活一个已经死亡的仙人到随机的空位
	 * <pre>
	 * 复活后生命值：m|x1*n
	 * m:施法者属性（4：攻，5：防，7：血上限）
	 * n:系数百分比（小数）
	 * </pre>
	 */
	public static final int Parser12002 = 12002;
	
	//------------------------ 致使目标混乱  ---------------------------
	
	/**
	 * 致使目标混乱(混乱者会攻击队友)
	 * <pre>
	 * 不需要表达式
	 * </pre>
	 */
	public static final int Parser1220 = 1220;
	/**
	 * 致使目标混乱(混乱者会攻击队友)
	 * <pre>
	 * 混乱者产生的伤害千分比
	 * </pre>
	 */
	public static final int Parser1221 = 1221;
	
	//--------------------------改变目标的位置--------------------------	
	
	/**
	 * 交换位置(效果表现为瞬移)
	 * <pre>
	 * 	不需要表达式
	 * <pre>
	 */
	public static final int Parser1230 = 1230;
	
	/**
	 * 与防御最低的交换位置（带跳跃）
	 * <pre>
	 * 	不需要表达式
	 * <pre>
	 */
	public static final int Parser12300 = 12300;
	
	/**
	 * 自己生命低于一定比例时，与后排换位（带跳跃效果）
	 * <pre>
	 * x1*n
	 * x1:施法者最大生命
	 * n:生命百分比
	 * <pre>
	 */
	public static final int Parser12301 = 12301;
	
	/**
	 * 移位(目的地没有仙人占位时才执行移动)（带跳跃效果）
	 * <pre>
	 * 	1
	 *  1:代表往前移动的1格,负数代表后移
	 *  前移后移是相对敌人来说的,即:
	 *  如果你想把敌人拉近，就让它前移。
	 *	想把敌人击退，就让它后移
	 * </pre>
	 * 
	 */
	public static final int Parser1231 = 1231;
	
	/**
	 * 移位+伤害（带跳跃效果）
	 * 移位是可选的,即目的地没有仙人占位时才执行移动
	 * <pre>
	 * 	1|(x1-x2/x3)*0.1
	 *  1:代表前后移,参见Parser1231
	 *  x1:攻击者的当前攻击力
	 *  x2:被攻击者的当前防御力
	 *  x3:防御系数
	 * </pre>
	 */
	public static final int Parser1232 = 1232;
	
	//-------------------------buffer跳动时触发技能-----------------------	
	/**
	 * A往目标身上释放一个buffer, buffer跳动时会引发A释放另一个技能
	 * <pre>
	 *  技能id
	 * </pre>
	 */
	public static final int Parser1240 = 1240;
	
	//-----------------------  特殊类型   --------------------------
	/**
	 * 对同一列的多个敌人发到M次攻击(只有前一个敌人死亡时才会攻击下一个)
	 * <pre>
	 * 	aninationId|3|(x1-x2/x3)
	 *  aninationId对应客户端特效id
	 *  3: 攻击次数
	 *  x1:攻击者的当前攻击力
	 *  x2:被攻击者的当前防御力
	 *  x3:防御系数
	 * </pre>
	 */
	public static final int Parser1990 = 1990;
	
	/** 敲击(击退,若后方有敌人则对2个敌人都造成伤害,并且弹回到原来的位置,否则只是被击中者有伤害)
	 * <pre>
	 * 	伤害值:(x1-x2/x3)
	 * 	x1:仙人的攻击力
	 *  x2:被攻击人的防御力
	 *  x3:防御系数
	 * </pre>
	 */
	public static final int Parser1991 = 1991;	 
	 
	/**
	 * 银角贪婪
	 * <pre>
	 *  x1:攻击者等级
	 *  x2：被攻击者等级
	 *  x2*3*x1
	 * </pre>
	 */
	public static final int Parser1992 = 1992;
	
	/**
	 * 羊撞(击退,后面没有敌人才后退,击退不会造成伤害)
	 * <pre>
	 * 无需配置表达式
	 * </pre>
	 */
	public static final int Parser1993 = 1993;
	
	/**
	 * 蟹钳(伤害并且将敌人拖到身后)（带跳跃）
	 * <pre>
	 * 	伤害值:(x1-x2/x3)
	 * 	x1:仙人的当前攻击力
	 *  x2:被攻击人的当前防御力
	 *  x3:防御系数
	 * </pre>
	 */
	public static final int Parser1994 = 1994;
	
	/**
	 * 降低属性并且将敌人拖到身后（带跳跃）
	 * <pre>
	 * 降低值:m|x1 * n|k
	 * m:施法者属性（4：攻，5：防）
	 * n:系数
	 * k:被施法者属性（4：攻，5：防）
	 * </pre>
	 */
	public static final int Parser19941 = 19941;
	
	/**
	 * 感化
	 * <pre>
	 * x1 * x2
	 * x1:最大血值
	 * x2：系数
	 * </pre>
	 */
	public static final int Parser1995 = 1995;
	
	/**
	 * 鹿拼：鹿力大仙的生命值每减少100，攻击力提升2%(在基础攻击力的基础上计算加成值)
	 * <pre>
	 * 100|x1*0.2
	 * x1:攻击力
	 * </pre>
	 */
	public static final int Parser1996 = 1996;
		
	/**
	 * 文殊菩萨-合击
	 * <pre>
	 * x1 - x2 / x3
	 * x1:友方所有人的攻击力之和
	 * x2：被击者防御力
	 * x3：防御系数
	 * </pre>
	 */
	public static final int Parser1998 = 1998;
	
	/**
	 * 大巫穿云箭
	 * <pre> 
	 * 	x1*（1+x2*0.1）-x3/x4
	 * 	x1:仙人的当前攻击力
	 *  x2:弓箭经过的友方英雄数
	 *  x3:受击方的当前防御力
	 *  x4:防御系数
	 * </pre>
	 */
	public static final int Parser1999 = 1999;
	
	/**
	 * 三味火-三味真火
	 * <pre>
	 * 	伤害值:(x1-x2/x3)
	 * 	x1:仙人的当前攻击力
	 *  x2:被攻击人的当前防御力
	 *  x3:防御系数
	 * </pre>
	 */
	public static final int Parser2000 = 2000;
	
	/**
	 * 射程范围内躲在敌人身后的敌人只受递减伤害（六耳猕猴技能）
	 * <pre>
	 * x4 == 1?(x1-x2/x3)*0.3:(x1-x2/x3)*1
	 * x4:前面是否有活人 1:有人，0：无人或者有人已死亡
	 * </pre>
	 */
	public static final int Parser2001 = 2001;
	
	/**
	 * 射程范围内躲在敌人身后的敌人只受递减伤害（孙悟空技能）
	 * <pre>
	 * x4 == 1?(x1-x2/x3)*0.5:(x1-x2/x3)*1
	 * x4:前面是否有活人 1:有2人，0：无人或者有人已死亡或者有1人
	 * </pre>
	 */
	public static final int Parser2002 = 2002;

	/**
	 * 减防御
	 * 减少目标的防御力(在施法者的基础攻击力的基础上计算减少量)
	 * <pre>
	 * x1*0.3
	 * x1:施法者的基础攻击值
	 * </pre>
	 */
	public static final int Parser1146 = 1146;

	/**
	 * 减防御
	 * 减少目标的防御力(在施法者的攻击力的基础上计算减少量)
	 * <pre>
	 * x1*0.3
	 * x1:施法者的攻击值
	 * </pre>
	 */
	public static final int Parser11460 = 11460;

	/**
	 * 复活并给队友加防御
	 * <pre>
	 * 表达式:m|x1*n
	 * m:复活者血量系数
	 * x1:复活者防御值
	 * n:系数
	 * </pre>
	 */
	public static final int Parser12001 = 12001;

	/**
	 * 免疫效果
	 */
	public static final int Parser3000 = 3000;

	/**
	 * 每回合减少属性值
	 * <pre>
	 * 表达式:m|x1*n
	 * m(1.减少攻击2.减少防御3.损失生命）
	 * x1:被减少仙人对应属性
	 * n:系数
	 * </pre>
	 */
	public static final int Parser2003 = 2003;

	/**
	 * 会给所有友方加属性（攻，防，血）这里的血指的是同时增加临时的生命上限和生命值，增加的次数有上限
	 * <pre>
	 * n|x1*m|l|k
	 * n:目标属性（4：攻，5：防，7：血上限）
	 * x1:施法者对应属性值
	 * m:系数
	 * l:被施法者次数上限
	 * k:施法者属性（4：攻，5：防，7：血上限）
	 * </pre>
	 */
	public static final int Parser2004 = 2004;

	/**
	 * 给前方的友方临时提高生命，给后方的友方加攻击, 提高值由施法者属性决定
	 * <pre>
	 * m|x1*n
	 * m:施法者属性（4：攻，5：防，7：血上限）
	 * x1:施法者对应属性值
	 * n:系数
	 * </pre>
	 */
	public static final int Parser2005 = 2005;

	/**
	 * 标记
	 */
	public static final int Parser2006 = 2006;
	
	/**
	 * 移动次数
	 * 提高目标的移动次数
	 * <pre>
	 * m
	 * 移动次数m次(m>=1)
	 * </pre>
	 */
	public static final int Parser4000 = 4000;

	/**
	 * 受到治疗时给目标加属性
	 * <pre>
	 * m|x1*n
	 * m:施法者属性（4：攻，5：防，7：血上限）
	 * x1:施法者对应属性值
	 * n:系数
	 * </pre>
	 */
	public static final int Parser2007 = 2007;
	
	/**
	 * 伤害改变，修改目标受到的伤害（小于0表示增加伤害，大于0表示减少伤害）
	 * <pre>
	 * 正数为增加伤害，负数为减小伤害
	 * 增加伤害：m|-x1*n
	 * 减少伤害：m|x1*n
	 * m：施法者属性（4：攻，5：防，7：血上限）
	 * n：系数
	 * </pre>
	 */
	public static final int Parser2008 = 2008;
	
	/**
	 * 必杀（无视防御，闪躲)
	 */
	public static final int Parser5000 = 5000;

	/**
	 * 根据被击者的属性 计算百分比伤害
	 * <pre>
	 * 伤害:m|x1 * n|l
	 * m:被施法者属性
	 * x1:被施法者属性值
	 * n:百分比(小数)
	 * l:上限
	 * </pre>
	 */
	public static final int Parser11001 = 11001;

	




}
