package com.jtang.gameserver.module.user.handler.response;

import org.apache.mina.core.buffer.IoBuffer;

import com.jtang.core.protocol.IoBufferSerializer;
import com.jtang.core.utility.BufferFactory;
import com.jtang.core.utility.ZipUtil;
import com.jtang.gameserver.module.adventures.achievement.handler.response.AchieveListResponse;
import com.jtang.gameserver.module.adventures.bable.handler.response.BableDataResponse;
import com.jtang.gameserver.module.adventures.shop.trader.handler.response.ShopOpenResponse;
import com.jtang.gameserver.module.adventures.shop.vipshop.handler.response.VipShopResponse;
import com.jtang.gameserver.module.adventures.vipactivity.handler.response.MainHeroResponse;
import com.jtang.gameserver.module.adventures.vipactivity.handler.response.VipActivityInfoResponse;
import com.jtang.gameserver.module.ally.handler.response.AllyListResponse;
import com.jtang.gameserver.module.app.handler.response.GetAppGlobalResponse;
import com.jtang.gameserver.module.app.handler.response.GetAppRecordResponse;
import com.jtang.gameserver.module.chat.handler.response.HistoryChatResponse;
import com.jtang.gameserver.module.dailytask.handler.response.DailyTaskInfoResponse;
import com.jtang.gameserver.module.demon.handler.response.DemonTimeResponse;
import com.jtang.gameserver.module.equip.handler.response.EquipListResponse;
import com.jtang.gameserver.module.extapp.basin.handler.response.BasinStateResponse;
import com.jtang.gameserver.module.extapp.beast.handler.response.BeastStatusResponse;
import com.jtang.gameserver.module.extapp.craftsman.handler.response.CraftsmanStatusResponse;
import com.jtang.gameserver.module.extapp.deitydesc.handler.response.DeityDescendStatusResponse;
import com.jtang.gameserver.module.extapp.ernie.handler.response.ErnieStatusResponse;
import com.jtang.gameserver.module.extapp.invite.handler.response.InviteResponse;
import com.jtang.gameserver.module.extapp.monthcard.handler.response.MonthCardResponse;
import com.jtang.gameserver.module.extapp.onlinegifts.handler.response.OnlineGiftsInfoResponse;
import com.jtang.gameserver.module.extapp.questions.handler.response.AnswerStateResponse;
import com.jtang.gameserver.module.extapp.randomreward.handler.response.RandomRewardResponse;
import com.jtang.gameserver.module.extapp.vipbox.handler.response.VipBoxConfigResponse;
import com.jtang.gameserver.module.extapp.vipbox.handler.response.VipBoxResponse;
import com.jtang.gameserver.module.gift.handler.response.GiftInfoResponse;
import com.jtang.gameserver.module.goods.handler.response.GoodsListResponse;
import com.jtang.gameserver.module.hero.handler.response.HeroListResponse;
import com.jtang.gameserver.module.hero.handler.response.HeroSoulListResponse;
import com.jtang.gameserver.module.hole.handler.response.HoleResponse;
import com.jtang.gameserver.module.icon.hander.response.IconResponse;
import com.jtang.gameserver.module.lineup.handler.response.LineupInfoResponse;
import com.jtang.gameserver.module.love.handler.response.LoveInfoResponse;
import com.jtang.gameserver.module.msg.handler.response.MsgListResponse;
import com.jtang.gameserver.module.notify.handler.response.NotifyListResponse;
import com.jtang.gameserver.module.recruit.handler.response.GetInfoResponse;
import com.jtang.gameserver.module.sign.handler.response.SignInfoResponse;
import com.jtang.gameserver.module.snatch.handler.response.SnatchInfoResponse;
import com.jtang.gameserver.module.sprintgift.handler.response.SprintGiftStatusListResponse;
import com.jtang.gameserver.module.story.handler.response.StoryResponse;
import com.jtang.gameserver.module.sysmail.handler.response.SysmailListResponse;
import com.jtang.gameserver.module.treasure.handler.response.TreasureGoodsResponse;
import com.jtang.gameserver.module.treasure.handler.response.TreasureResponse;
import com.jtang.gameserver.module.trialcave.handler.response.TrialCaveInfoResponse;

public class ActorNecessaryDataResponse extends IoBufferSerializer {
	
	private static final int ZIP_SIZE = 2048;
	
	private GetInfoResponse getRecruitInfoResponse;
	private StoryResponse storyResponse;
	private HeroListResponse heroListResponse;
	private HeroSoulListResponse heroSoulListResponse;
	private LineupInfoResponse lineupInfoResponse;
	private EquipListResponse equipListResponse;
	private SnatchInfoResponse snatchInfoResponse;
	private AllyListResponse allyListResponse;
	private GiftInfoResponse giftInfoResponse;
	private GoodsListResponse goodsListResponse;
	private MsgListResponse msgListResponse;
	private NotifyListResponse notifyListResponse;
	private SysmailListResponse sysmailListResponse;
	private AchieveListResponse achieveListResponse;
	private BableDataResponse bableDataResponse;
	private SignInfoResponse signInfoResponse;
	private DailyTaskInfoResponse dailyTaskInfoResponse;
	private MonthCardResponse monthCardResponse;
	private SprintGiftStatusListResponse sprintGiftStatusListResponse;
	private ActorBuyResponse actorBuyResponse;
	private MainHeroResponse mainHeroResponse;
	private GetAppGlobalResponse getAppGlobalResponse;
	private GetAppRecordResponse getAppRecordResponse;
	private IconResponse iconResponse;
	private VipActivityInfoResponse vipActivityInfoResponse;
	private ShopOpenResponse shopOpenResponse;
	private AnswerStateResponse answerStateResponse;
	private BasinStateResponse basinStateResponse;
	private DeityDescendStatusResponse deityDescendStatusResponse;
	private LoveInfoResponse loveInfoResponse;
	private HistoryChatResponse histChatResponse;
	private VipShopResponse vipShopResponse;
	private TreasureGoodsResponse treasureGoodsResponse;
	private CraftsmanStatusResponse craftsmanStatusResponse;
	private DemonTimeResponse demonTimeResponse;
	private ErnieStatusResponse ernieStatusResponse;
	private BeastStatusResponse beastStatusResponse;
	private HoleResponse holeResponse;
	private VipBoxConfigResponse vipBoxConfigResponse;
	
	//-------------------------可选字段-----------------
	private TrialCaveInfoResponse trialCaveInfoResponse;
	private TreasureResponse treasureResponse;
	private InviteResponse inviteResponse;
	private RandomRewardResponse randomRewardResponse;
	private OnlineGiftsInfoResponse onlineGiftsInfoResponse;
	private VipBoxResponse vipBoxResponse;


	public ActorNecessaryDataResponse(GetInfoResponse getRecruitInfoResponse,
			StoryResponse storyResponse, HeroListResponse heroListResponse,
			HeroSoulListResponse heroSoulListResponse,
			LineupInfoResponse lineupInfoResponse,
			EquipListResponse equipListResponse,
			SnatchInfoResponse snatchInfoResponse,
			AllyListResponse allyListResponse,
			GiftInfoResponse giftInfoResponse,
			GoodsListResponse goodsListResponse,
			MsgListResponse msgListResponse,
			NotifyListResponse notifyListResponse,
			SysmailListResponse sysmailListResponse,
			AchieveListResponse achieveListResponse,
			BableDataResponse bableDataResponse,
			SignInfoResponse signInfoResponse,
			DailyTaskInfoResponse dailyTaskInfoResponse,
			MonthCardResponse monthCardResponse,
			SprintGiftStatusListResponse sprintGiftStatusListResponse,
			ActorBuyResponse actorBuyResponse,
			MainHeroResponse mainHeroResponse,
			GetAppGlobalResponse getAppGlobalResponse,
			GetAppRecordResponse getAppRecordResponse,
			IconResponse iconResponse,
			VipActivityInfoResponse vipActivityInfoResponse,
			ShopOpenResponse shopOpenResponse,
			AnswerStateResponse answerStateResponse,
			BasinStateResponse basinStateResponse,
			DeityDescendStatusResponse deityDescendStatusResponse,
			LoveInfoResponse loveInfoResponse,
			HistoryChatResponse histChatResponse,
			VipShopResponse vipShopResponse,
			TreasureGoodsResponse treasureGoodsResponse,
			CraftsmanStatusResponse craftsmanStatusResponse,
			DemonTimeResponse demonTimeResponse,
			ErnieStatusResponse ernieStatusResponse,
			BeastStatusResponse beastStatusResponse,
			HoleResponse holeResponse,
			VipBoxConfigResponse vipBoxConfigResponse
			) {
		super();
		this.getRecruitInfoResponse = getRecruitInfoResponse;
		this.storyResponse = storyResponse;
		this.heroListResponse = heroListResponse;
		this.heroSoulListResponse = heroSoulListResponse;
		this.lineupInfoResponse = lineupInfoResponse;
		this.equipListResponse = equipListResponse;
		this.snatchInfoResponse = snatchInfoResponse;
		this.allyListResponse = allyListResponse;
		this.giftInfoResponse = giftInfoResponse;
		this.goodsListResponse = goodsListResponse;
		this.msgListResponse = msgListResponse;
		this.notifyListResponse = notifyListResponse;
		this.sysmailListResponse = sysmailListResponse;
		this.achieveListResponse = achieveListResponse;
		this.bableDataResponse = bableDataResponse;
		this.signInfoResponse = signInfoResponse;
		this.dailyTaskInfoResponse = dailyTaskInfoResponse;
		this.monthCardResponse = monthCardResponse;
		this.sprintGiftStatusListResponse = sprintGiftStatusListResponse;
		this.actorBuyResponse = actorBuyResponse;
		this.mainHeroResponse = mainHeroResponse;
		this.getAppGlobalResponse = getAppGlobalResponse;
		this.getAppRecordResponse = getAppRecordResponse;
		this.iconResponse = iconResponse;
		this.vipActivityInfoResponse = vipActivityInfoResponse;
		this.shopOpenResponse = shopOpenResponse;
		this.answerStateResponse = answerStateResponse;
		this.basinStateResponse = basinStateResponse;
		this.deityDescendStatusResponse = deityDescendStatusResponse;
		this.loveInfoResponse = loveInfoResponse;
		this.histChatResponse = histChatResponse;
		this.vipShopResponse = vipShopResponse;
		this.treasureGoodsResponse = treasureGoodsResponse;
		this.craftsmanStatusResponse = craftsmanStatusResponse;
		this.demonTimeResponse = demonTimeResponse;
		this.ernieStatusResponse = ernieStatusResponse;
		this.beastStatusResponse = beastStatusResponse;
		this.holeResponse = holeResponse;
		this.vipBoxConfigResponse = vipBoxConfigResponse;
	}


	@Override
	protected void setWriteBuffer() {
		writeBuffer = BufferFactory.getIoBuffer(2048, true);
	}



	@Override
	public void write() {
		this.writeBytes(this.getRecruitInfoResponse.getBytes());
		this.writeBytes(this.storyResponse.getBytes());
		this.writeBytes(this.heroListResponse.getBytes());
		this.writeBytes(this.heroSoulListResponse.getBytes());
		this.writeBytes(this.lineupInfoResponse.getBytes());
		this.writeBytes(this.equipListResponse.getBytes());
		this.writeBytes(this.snatchInfoResponse.getBytes());
		this.writeBytes(this.allyListResponse.getBytes());
		this.writeBytes(this.giftInfoResponse.getBytes());
		this.writeBytes(this.goodsListResponse.getBytes());
		this.writeBytes(this.msgListResponse.getBytes());
		this.writeBytes(this.notifyListResponse.getBytes());
		this.writeBytes(this.sysmailListResponse.getBytes());
		this.writeBytes(this.achieveListResponse.getBytes());
		this.writeBytes(this.bableDataResponse.getBytes());
		this.writeBytes(this.signInfoResponse.getBytes());
		this.writeBytes(this.dailyTaskInfoResponse.getBytes());
		this.writeBytes(this.monthCardResponse.getBytes());
		this.writeBytes(this.sprintGiftStatusListResponse.getBytes());
		this.writeBytes(this.actorBuyResponse.getBytes());
		this.writeBytes(this.mainHeroResponse.getBytes());
		this.writeBytes(this.getAppGlobalResponse.getBytes());
		this.writeBytes(this.getAppRecordResponse.getBytes());
		this.writeBytes(this.iconResponse.getBytes());
		this.writeBytes(this.vipActivityInfoResponse.getBytes());
		this.writeBytes(this.shopOpenResponse.getBytes());
		this.writeBytes(this.answerStateResponse.getBytes());
		this.writeBytes(this.basinStateResponse.getBytes());
		this.writeBytes(this.deityDescendStatusResponse.getBytes());
		this.writeBytes(this.loveInfoResponse.getBytes());
		this.writeBytes(this.histChatResponse.getBytes());
		this.writeBytes(this.vipShopResponse.getBytes());
		this.writeBytes(this.treasureGoodsResponse.getBytes());
		this.writeBytes(this.craftsmanStatusResponse.getBytes());
		this.writeBytes(this.demonTimeResponse.getBytes());
		this.writeBytes(this.ernieStatusResponse.getBytes());
		this.writeBytes(this.holeResponse.getBytes());
		
		if (this.trialCaveInfoResponse != null) {
			this.writeByte((byte)1);
			this.writeBytes(this.trialCaveInfoResponse.getBytes());
		} else {
			this.writeByte((byte)0);
		}
		
		if (this.treasureResponse != null) {
			this.writeByte((byte)1);
			this.writeBytes(this.treasureResponse.getBytes());
		} else {
			this.writeByte((byte)0);
		}
		if (this.inviteResponse != null) {
			this.writeByte((byte)1);
			this.writeBytes(this.inviteResponse.getBytes());
		} else {
			this.writeByte((byte)0);
		}
		if (this.randomRewardResponse != null) {
			this.writeByte((byte)1);
			this.writeBytes(this.randomRewardResponse.getBytes());
		} else {
			this.writeByte((byte)0);
		}
		if (this.onlineGiftsInfoResponse != null) {
			this.writeByte((byte)1);
			this.writeBytes(this.onlineGiftsInfoResponse.getBytes());
		} else {
			this.writeByte((byte)0);
		}
		
		this.writeBytes(this.beastStatusResponse.getBytes());
		
		this.writeBytes(this.vipBoxConfigResponse.getBytes());
		
		if(this.vipBoxResponse != null){
			this.writeByte((byte)1);
			this.writeBytes(this.vipBoxResponse.getBytes());
		}else{
			this.writeByte((byte)0);
		}
		
	}
	
	public byte[] getBytes() {
		write();
		byte[] bytes = null;
		if (writeBuffer.limit() == 0) {
			bytes = writeBuffer.array();
		} else {
			bytes = new byte[writeBuffer.position()];
			writeBuffer.flip();
			writeBuffer.get(bytes);
		}
		writeBuffer.clear();
		// 压缩处理
		// 首先放入压缩后的大小，不压缩放-1
		// 再放入原始数据大小
//		byte[] temp;
//		int len = bytes.length;
//		if (bytes.length >= ZIP_SIZE) {
//			temp = ZipUtil.compress(bytes);
//			writeBuffer.putShort((short) temp.length);
//			writeBuffer.putShort(len);
//			writeBuffer.put(temp);
//		} else {
//			writeBuffer.putShort(len);
//			writeBuffer.putShort((short) -1);
//			writeBuffer.put(bytes);
//		}
//		bytes = new byte[writeBuffer.position()];
//		writeBuffer.flip();
//		writeBuffer.get(bytes);
//		writeBuffer.clear();
		return splitByteArray(bytes);
	}
	
	private byte[] splitByteArray(byte[] src) {
		
		IoBuffer ioBuffer = BufferFactory.getIoBuffer(2048, true);
		if (src.length <= Short.MAX_VALUE) {
		
			ioBuffer.put(compress(src));
			ioBuffer.put((byte)0);
		}
		else
		{
			int has = src.length;
			int index = 0;
			do
			{
				int readed = Math.min(has, Short.MAX_VALUE);
				
				byte[] split = new byte[readed];
				System.arraycopy(src, index, split, 0, readed);
				has -= readed;
				index += readed;
				ioBuffer.put(compress(split));
				
				if (has > 0)
				{
					ioBuffer.put((byte)1);
				}
				else
				{
					ioBuffer.put((byte)0);
					break;
				}
				
			}while(true);

		}
		
		byte[] b = new byte[ioBuffer.position()];
		ioBuffer.flip();
		ioBuffer.get(b);
		return b;
		
	}
	
	private byte[] compress(byte[] src) {
		byte[] bytes = src;
		byte[] temp;
		short len = (short) bytes.length;
		if (bytes.length >= ZIP_SIZE) {
			temp = ZipUtil.compress(bytes);
			writeBuffer.putShort((short) temp.length);
			writeBuffer.putShort(len);
			writeBuffer.put(temp);
		} else {
			writeBuffer.putShort(len);
			writeBuffer.putShort((short) -1);
			writeBuffer.put(bytes);
		}
		bytes = new byte[writeBuffer.position()];
		writeBuffer.flip();
		writeBuffer.get(bytes);
		writeBuffer.clear();
		return bytes;

	}
	
/*
	public byte[] getBytes() {
		write();
		byte[] bytes = null;
		if (writeBuffer.limit() == 0) {
			bytes = writeBuffer.array();
		} else {
			bytes = new byte[writeBuffer.position()];
			writeBuffer.flip();
			writeBuffer.get(bytes);
		}
		writeBuffer.clear();
		// 压缩处理
		// 首先放入压缩后的大小，不压缩放-1
		// 再放入原始数据大小
		long len = bytes.length;
		if(len >= ZIP_SIZE) {
			
			writeBuffer.put((byte) 1);
			writeBuffer.putLong(len);
			byte[] re = ZipUtil.compressLZMA(bytes);
			writeBuffer.put(re);
			bytes = new byte[writeBuffer.position()];
			writeBuffer.flip();
			writeBuffer.get(bytes);
			writeBuffer.clear();
			writeBuffer.putInt(bytes.length);
			writeBuffer.put(bytes);
		} else {
			writeBuffer.putInt(bytes.length);
			writeBuffer.put((byte) 0);
			writeBuffer.put(bytes);
		}
		bytes = new byte[writeBuffer.position()];
		writeBuffer.flip();
		writeBuffer.get(bytes);
		writeBuffer.clear();
		return bytes;
	}
*/

	public void setTrialCaveInfoResponse(TrialCaveInfoResponse res) {
		this.trialCaveInfoResponse = res;
	}


	public void setTreasureResponse(TreasureResponse treasureResponse) {
		this.treasureResponse = treasureResponse;
		
	}


	public void setInviteResponse(InviteResponse inviteResponse) {
		this.inviteResponse = inviteResponse;
		
	}


	public void setRandomRewardResponse(RandomRewardResponse randomRewardResponse) {
		this.randomRewardResponse = randomRewardResponse;
		
	}

	public void setOnlineGiftsInfoResponse(OnlineGiftsInfoResponse onlineGiftsInfoResponse) {
		this.onlineGiftsInfoResponse = onlineGiftsInfoResponse;
		
	}
	
	public void setVipBoxResponse(VipBoxResponse vipBoxResponse){
		this.vipBoxResponse = vipBoxResponse;
	}
}
