package com.jtang.gameserver.module.gift.handler.response;

import java.util.HashSet;
import java.util.Set;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.gameserver.dbproxy.entity.Gift;

public class GiftInfoResponse extends IoBufferSerializer {
	/**
	 * 已经送我礼物的盟友(未领取)
	 */
	private Set<Long> allysGivenMeGift;;
	
	/**
	 * 已经收到我礼物的盟友
	 */
	private Set<Long> allysReceiveMyGift;
	
	/**
	 * 我已经接受其礼物的盟友
	 */
	private Set<Long> allysIAcceptedGift;
	
	/**
	 * 是否已经领取大礼包(0:没有 1：已领取)
	 */
	private byte hasOpenGiftPackage;
	
	@Override
	public void write() {
		this.writeLongSet(allysGivenMeGift);
		this.writeLongSet(allysReceiveMyGift);
		this.writeLongSet(allysIAcceptedGift);
		this.writeByte(hasOpenGiftPackage);
	}
	
	public GiftInfoResponse(Gift gift) {
		this.allysReceiveMyGift = gift.getAllysReceivedMyGiftSet();
		this.allysIAcceptedGift = gift.getAcceptedAllysSet();
		this.allysGivenMeGift = new HashSet<>();
		this.allysGivenMeGift.addAll(gift.getReceivedGiftsMap().keySet());
		this.hasOpenGiftPackage =(byte) (gift.acceptGiftPackage ? 1 : 0);
	}

}
