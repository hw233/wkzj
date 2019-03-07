package com.jtang.core.protocol;

/**
 * 附带角色id响应包头类
 * @author 0x737263
 *
 */
public class ActorResponse extends Response {
	private long actorId;
	
	public long getActorId() {
		return this.actorId;
	}
	
	public void setActorId(long actorId) {
		this.actorId = actorId;
	}
	
	public static ActorResponse valueOf(byte module, byte cmd) {
		ActorResponse response = new ActorResponse();
		response.setModule(module);
		response.setCmd(cmd);
		return response;
	}

}
