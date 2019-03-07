package com.jtang.gameserver.server.firewall;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * 防火墙bean配置.gateway.firewallconfig.xml
 * 
 * @author 0x737263
 * 
 */
@Component
public class FirewallConfig {
	
	/**
	 * 单个连接每分钟最大包数量. 1分钟最大发送3500个包
	 */
	@Autowired
	@Qualifier("firewall.max.packages.minute")
	public Integer maxPacksPerMinute;

	/**
	 * 单个连接每分钟最大发送包内容数量. 1分钟则为600K,默认:614400=600K
	 */
	@Autowired
	@Qualifier("firewall.max.bytes.minute")
	public Integer maxBytesPerMinute;
	
	/**
	 * 单个连接每分钟最大验证码错误次数. 如果超过则加入防火墙,默认:10
	 */
	@Autowired
	@Qualifier("firewall.max.authcode.errors.minute")
	public Integer maxAuthCodeErrorsPerMinute;

	/**
	 * 检测到多少次洪水包后,禁止链接,默认:5
	 */
	@Autowired
	@Qualifier("firewall.block.detect.count")
	public Integer blockDetectCount;

	/**
	 * 禁止IP连接的分钟数,默认:5,单位:分钟
	 */
	@Autowired
	@Qualifier("firewall.block.ip.minutes")
	public Integer blockIpMinutes;

	/**
	 * 禁止用户连接的分钟数,默认:10,单位:分钟
	 */
	@Autowired
	@Qualifier("firewall.block.user.minutes")
	public Integer blockUserMinutes;

	/**
	 * 允许的最大连接,默认:10000
	 */
	@Autowired
	@Qualifier("firewall.max.clients.limit")
	public Integer maxClientsLimit;

	/**
	 * 是否开启防火墙,默认是ture
	 */
	@Autowired
	@Qualifier("firewall.enable")
	public Boolean enableFirewall;

}
