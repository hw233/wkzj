import com.jtang.core.context.SpringContext;
import com.jtang.gameserver.module.ladder.facade.impl.LadderFacadeImpl;


public class LadderTestCase {

	public static void main(String[] args) {
		LadderFacadeImpl facade = (LadderFacadeImpl)SpringContext.getBean(LadderFacadeImpl.class);
		
//		facade.getInfo(84141933698L);
		
//		facade.getRank(84141933698L);
		facade.getAllRankFromCache(10);
	}
}
